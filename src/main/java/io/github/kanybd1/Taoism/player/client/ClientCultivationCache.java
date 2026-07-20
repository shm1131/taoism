package io.github.kanybd1.Taoism.player.client;

import io.github.kanybd1.Taoism.player.data.cultivation.ICultivationData;

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