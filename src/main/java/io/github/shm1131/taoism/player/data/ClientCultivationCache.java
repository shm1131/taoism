package io.github.shm1131.taoism.player.data;

import io.github.shm1131.taoism.player.attachment.cultivation.ICultivationData;

public class ClientCultivationCache {

  private static ICultivationData cachedData = ICultivationData.EMPTY;

  public static void update(ICultivationData data) {
    cachedData = data;
  }

  public static ICultivationData get() {
    return cachedData;
  }

  public static void clear() {
    cachedData = ICultivationData.EMPTY;
  }
}
