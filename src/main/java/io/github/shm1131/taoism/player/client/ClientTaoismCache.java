package io.github.shm1131.taoism.player.client;

import io.github.shm1131.taoism.player.data.attachment.ITaoismData;

public class ClientTaoismCache {

    private static ITaoismData cachedData = ITaoismData.EMPTY;

    public static void update(ITaoismData data) {
        cachedData = data;
    }

    public static ITaoismData get() {
        return cachedData;
    }

    public static void clear() {
        cachedData = ITaoismData.EMPTY;
    }
}