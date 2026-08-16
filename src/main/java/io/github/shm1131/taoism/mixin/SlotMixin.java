package io.github.shm1131.taoism.mixin;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.inventory.Slot.class)
public abstract class SlotMixin {

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void taoism$checkJingLi(Player player, CallbackInfoReturnable<Boolean> cir) {

        if (!((Object) this instanceof ResultSlot
            || (Object) this instanceof FurnaceResultSlot)) {
            return;
        }

        if (!player.level().isClientSide()) {
            IJingLiData data = player.getData(TaoismAttachments.JINGLI_DATA);
            if (data.getJingLiEat() <= 0 && data.getJingLiSleep() <= 0) {
                cir.setReturnValue(false);
            }
        }
    }
}
