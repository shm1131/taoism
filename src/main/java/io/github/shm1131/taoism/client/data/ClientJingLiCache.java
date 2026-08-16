package io.github.shm1131.taoism.client.data;

import io.github.shm1131.taoism.player.attachment.api.IJingLiData;

public class ClientJingLiCache {

    private static IJingLiData cachedData = IJingLiData.EMPTY;

    public static void update(IJingLiData data) {
        cachedData = data;
    }

    public static IJingLiData get() {
        return cachedData;
    }

    public static void clear() {
        cachedData = IJingLiData.EMPTY;
    }
}
