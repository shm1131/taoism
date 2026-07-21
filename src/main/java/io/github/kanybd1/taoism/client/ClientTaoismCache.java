package io.github.kanybd1.taoism.client;

import io.github.kanybd1.taoism.data.attachment.ITaoismData;

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
