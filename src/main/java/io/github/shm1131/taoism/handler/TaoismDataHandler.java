package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.datagen.dim.ModLevelStems;
import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncJingLiDataPayload;
import io.github.shm1131.taoism.player.attachment.api.IDeathInventoryData;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import io.github.shm1131.taoism.player.attachment.api.ITaoismData;
import io.github.shm1131.taoism.player.attachment.helper.JingLiHelper;
import io.github.shm1131.taoism.player.attachment.helper.TaoismHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.network.chat.Component.literal;

@EventBusSubscriber
public class TaoismDataHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var player = event.getEntity();

        IJingLiData data1 = player.getData(TaoismAttachments.JINGLI_DATA.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncJingLiDataPayload(data1));

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) {
            int chengFu = player.getRandom().nextInt(100);
            TaoismHelper.initYangWorld(player, chengFu);
            player.sendSystemMessage(literal("§6✦ 天道已定，你的承负为：" + chengFu));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;
        var player = event.getEntity();
        if (player.tickCount % 20 != 0) return;

        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) return;

        if (data.getShouMing() <= 0) {
            boolean actuallyInRealm = player.level().dimension()
                .equals(ModLevelStems.TAOISM_REALM_KEY);
            TaoismHelper.setInRealm(player, actuallyInRealm);
            player.kill((ServerLevel) player.level());
            return;
        }
        TaoismHelper.addShouMing(player, -20);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        if (!data.isChengFuInit()) return;

        boolean actuallyInRealm = player.level().dimension()
            .equals(ModLevelStems.TAOISM_REALM_KEY);
        TaoismHelper.setInRealm(player, actuallyInRealm);

        if (!actuallyInRealm) {
            List<ItemStack> snapshot = new ArrayList<>();

            for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
                if (!stack.isEmpty()) {
                    snapshot.add(stack.copy());
                }
            }

            for (EquipmentSlot slot : EquipmentSlot.VALUES) {
                if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) continue;

                ItemStack stack = player.getItemBySlot(slot);

                if (!stack.isEmpty()) {
                    snapshot.add(stack.copy());
                }
            }

            player.setData(TaoismAttachments.DEATH_INVENTORY,
                new IDeathInventoryData.DeathInventoryData(snapshot));
            player.getInventory().clearContent();
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (event.getEntity().level().isClientSide()) return;

        ServerPlayer oldPlayer = (ServerPlayer) event.getOriginal();
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();

        // 手动深拷贝死亡物品快照
        IDeathInventoryData deathData = oldPlayer.getData(TaoismAttachments.DEATH_INVENTORY);
        if (!deathData.getItems().isEmpty()) {
            List<ItemStack> safeCopy = deathData.getItems().stream()
                .map(ItemStack::copy)
                .collect(Collectors.toList());

            newPlayer.setData(TaoismAttachments.DEATH_INVENTORY,
                new IDeathInventoryData.DeathInventoryData(safeCopy));

            TaoismMain.LOGGER.debug("Clone: manually copied {} death items for player {}",
                safeCopy.size(), newPlayer.getName().getString());
        }

        // 复制 isInRealm 状态
        ITaoismData oldData = oldPlayer.getData(TaoismAttachments.TAOISM_DATA);
        TaoismHelper.setInRealm(newPlayer, oldData.isInRealm());

        TaoismMain.LOGGER.debug("Clone: copied isInRealm={} from dead player", oldData.isInRealm());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.isEndConquered()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ITaoismData data = player.getData(TaoismAttachments.TAOISM_DATA);
        int chengFu = data.getChengFu();
        boolean targetIsYin = !data.isInRealm();

        if (targetIsYin) {
            ServerLevel yinLevel = player.level().getServer().getLevel(ModLevelStems.TAOISM_REALM_KEY);

            if (yinLevel == null) {
                player.sendSystemMessage(literal("§c✦ 道境未加载，无法轮回！"));
                    player.level().getServer().levelKeys().stream()
                        .map(ResourceKey::toString)
                        .collect(Collectors.joining(", "));
                return;
            }

            final int SPAWN_Y = 150;

            player.sendSystemMessage(literal("§5✦ 魂归幽冥，承负：" + chengFu
                + "，阴寿：" + (chengFu * 600)));

            player.level().getServer().execute(() -> {
                player.teleportTo(yinLevel, 0, SPAWN_Y, 0,
                    java.util.Set.of(), 0f, 0f, false);

                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.SLOW_FALLING,
                    chengFu * 600, 0, false, true, true));

                TaoismHelper.resetForYinWorld(player, chengFu);
            });
        } else {
            TaoismHelper.resetForYangWorld(player);
            JingLiHelper.init(player);

            IDeathInventoryData deathData = player.getData(TaoismAttachments.DEATH_INVENTORY);
            List<ItemStack> savedItems = deathData.getItems();

            if (!savedItems.isEmpty()) {
                for (ItemStack stack : savedItems) {
                    if (!player.getInventory().add(stack.copy())) {
                        player.drop(stack.copy(), false, true);
                    }
                }

                player.sendSystemMessage(literal("§6✦ 重入人间，承负：" + chengFu
                    + "，已归还 " + savedItems.size() + " 件遗物"));

                player.setData(TaoismAttachments.DEATH_INVENTORY,
                    new IDeathInventoryData.DeathInventoryData(List.of()));

                TaoismMain.LOGGER.debug("Player {} respawned in Yang world, restored and cleared death inventory",
                    player.getName().getString());
            } else {
                player.sendSystemMessage(literal("§6✦ 重入人间，承负：" + chengFu));
            }
        }

        TaoismHelper.setInRealm(player, targetIsYin);
    }
}
