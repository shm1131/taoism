package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.ItemRegister;
import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.item.herb.base.BaseHerbItem;
import io.github.shm1131.taoism.player.attachment.api.ICoolDownData;
import io.github.shm1131.taoism.player.attachment.helper.TaoismHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = TaoismMain.MODID)
public class ProximityDropEventHandler {

    private static final Set<EntityType<?>> LAND_ANIMALS = Set.of(
        EntityType.COW,
        EntityType.PIG,
        EntityType.SHEEP,
        EntityType.CHICKEN,
        EntityType.RABBIT,
        EntityType.HORSE,
        EntityType.DONKEY,
        EntityType.MULE,
        EntityType.LLAMA,
        EntityType.CAT,
        EntityType.WOLF,
        EntityType.FOX,
        EntityType.GOAT,
        EntityType.CAMEL,
        EntityType.SNIFFER
    );

    private static List<Item> baseHerbsCache;

    private static List<Item> getBaseHerbs() {
        if (baseHerbsCache == null) {
            baseHerbsCache = ItemRegister.SIMPLE_ITEMS.values().stream()
                .filter(holder -> holder.get() instanceof BaseHerbItem)
                .map(holder -> (Item) holder.get())
                .toList();
        }
        return baseHerbsCache;
    }

    private static final int DROP_COOLDOWN_TICKS = 200;
    private static final double DETECT_RANGE = 3.0D;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (TaoismHelper.getChengFu(player) >= 50) return;

        long currentTick = player.level().getGameTime();

        ICoolDownData coolDownData = event.getEntity().getData(TaoismAttachments.COOL_DOWN_DATA);
        if (coolDownData == null) {
            coolDownData = new ICoolDownData.CoolDownData(0L);
            event.getEntity().setData(TaoismAttachments.COOL_DOWN_DATA, coolDownData);
        }

        if (currentTick - coolDownData.getDropCoolDown() < DROP_COOLDOWN_TICKS) return;

        var aabb = player.getBoundingBox().inflate(DETECT_RANGE);
        // ⭐ 使用 Set.contains() 替代 mob.is(Tag)
        var nearbyMobs = player.level().getEntitiesOfClass(
            Entity.class,
            aabb,
            mob -> mob.isAlive() && LAND_ANIMALS.contains(mob.getType())
        );

        if (nearbyMobs.isEmpty()) return;

        event.getEntity().setData(
            TaoismAttachments.COOL_DOWN_DATA,
            new ICoolDownData.CoolDownData(currentTick)
        );

        Entity nearest = nearbyMobs.stream()
            .min((a, b) -> Double.compare(a.distanceToSqr(player), b.distanceToSqr(player)))
            .orElse(null);

        if (nearest == null) return;

        if (!getBaseHerbs().isEmpty()) {
            Item randomHerb = getBaseHerbs().get(player.getRandom().nextInt(getBaseHerbs().size()));
            ItemStack dropStack = new ItemStack(randomHerb, 1);

            var itemEntity = new ItemEntity(nearest.level(),
                nearest.getX(), nearest.getY(0.5D), nearest.getZ(), dropStack);
            itemEntity.setDefaultPickUpDelay();
            nearest.level().addFreshEntity(itemEntity);
        }
    }
}
