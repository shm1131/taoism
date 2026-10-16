package io.github.shm1131.taoism.handler;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.attachment.helper.TaoismHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = TaoismMain.MODID)
public class ChengFuHandler {

    private static final long CHENG_FU_COOLDOWN_MS = 1000L;

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        ServerLevel level = (ServerLevel) event.getEntity().level();
        BlockPos pos = event.getEntity().blockPosition();
        if (!level.isPositionEntityTicking(pos)) return;
        if ("outOfWorld".equals(event.getSource().getMsgId())) return;

        DamageSource source = event.getSource();

        ServerPlayer responsiblePlayer = null;
        if (source.getEntity() instanceof ServerPlayer p) responsiblePlayer = p;
        else if (source.getDirectEntity() instanceof ServerPlayer p) responsiblePlayer = p;

        if (responsiblePlayer == null) {
            Player nearest = level.getNearestPlayer(
                event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                -1.0D, player -> !player.isSpectator() && player.isAlive()
            );
            if (nearest instanceof ServerPlayer sp) responsiblePlayer = sp;
        }

        if (responsiblePlayer == null) return;

        if (!TaoismHelper.isChengFuReady(responsiblePlayer, CHENG_FU_COOLDOWN_MS)) return;

        MobCategory category = event.getEntity().getType().getCategory();


        boolean isDirectKillByPlayer = (source.getEntity() == responsiblePlayer || source.getDirectEntity() == responsiblePlayer);
        if (isDirectKillByPlayer && category == MobCategory.MONSTER) {
            int redemption = getRedemptionValue(event.getEntity());
            if (redemption > 0) {
                TaoismHelper.addChengFu(responsiblePlayer, -redemption);
                TaoismHelper.setChengFuCoolDown(responsiblePlayer, System.currentTimeMillis());
            }
            return;
        }

        boolean isFriendlyOrNeutral = switch (category) {
            case CREATURE, WATER_AMBIENT, WATER_CREATURE, AMBIENT -> true;
            default -> false;
        };
        if (!isFriendlyOrNeutral) return;

        int penalty = switch (category) {
            case CREATURE       -> 5;
            case WATER_CREATURE -> 4;
            case WATER_AMBIENT  -> 3;
            case AMBIENT        -> 1;
            default             -> 0;
        };

        if (penalty > 0) {
            TaoismHelper.addChengFu(responsiblePlayer, penalty);
            TaoismHelper.setChengFuCoolDown(responsiblePlayer, System.currentTimeMillis());
        }
    }

    private static int getRedemptionValue(net.minecraft.world.entity.Entity entity) {

        if (entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss ||
            entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon ||
            entity instanceof net.minecraft.world.entity.raid.Raider) {
            return 20;
        }

        if (entity instanceof Monster) {
            return 2;
        }
        return 0;
    }
}
