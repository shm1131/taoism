package io.github.shm1131.taoism.player.attachment.cultivation;

import io.github.shm1131.taoism.init.TaoismAttachments;
import io.github.shm1131.taoism.network.SyncCultivationDataPayload;
import io.github.shm1131.taoism.player.attachment.api.ICultivationData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import static io.github.shm1131.taoism.init.TaoismAttachments.CULTIVATION_DATA;

public class CultivationHelper {

  private static ICultivationData getData(Player player) {
    ICultivationData data = player.getData(CULTIVATION_DATA);
    return data != null ? data : ICultivationData.EMPTY;
  }

  private static void setData(Player player, ICultivationData data) {
    player.setData(CULTIVATION_DATA, data);
    if (!player.level().isClientSide() && player instanceof ServerPlayer sp) {
      PacketDistributor.sendToPlayer(sp, new SyncCultivationDataPayload(data));
    }
  }


  public static int getMaxSp(Player player) {
    return getData(player).getRealm().getMaxSpiritualPower();
  }


  public static boolean canLevelUp(Player player) {
    ICultivationData data = getData(player);
    if (data.getRealm().next().isEmpty()) {
      return false;
    }
    return data.getCurrentSp() >= data.getRealm().getMaxSpiritualPower();
  }


  public static void addSp(Player player, int amount) {
    if (player.getData(TaoismAttachments.TAOISM_DATA).isInRealm()) {
      return;
    }
    if (amount <= 0) return;

    ICultivationData oldData = getData(player);
    int maxSp = oldData.getRealm().getMaxSpiritualPower();
    int newSp = Math.min(maxSp, oldData.getCurrentSp() + amount);

    if (newSp != oldData.getCurrentSp()) {
      setData(player, new ICultivationData.CultivationData(
          oldData.getRealm(), newSp
      ));
    }
  }


  public static boolean levelUp(Player player) {
    if (!canLevelUp(player)) return false;

    ICultivationData oldData = getData(player);
    var nextRealm = oldData.getRealm().next();
    if (nextRealm.isEmpty()) return false;

    setData(player, new ICultivationData.CultivationData(
        nextRealm.get(), 0
    ));
    return true;
  }
}
