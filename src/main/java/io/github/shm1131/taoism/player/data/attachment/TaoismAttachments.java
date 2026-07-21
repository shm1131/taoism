package io.github.shm1131.taoism.player.data.attachment;

import io.github.shm1131.taoism.TaoismMain;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TaoismAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TaoismMain.MODID);

    public static final Supplier<AttachmentType<ITaoismData>> TAOISM_DATA =
            ATTACHMENT_TYPES.register("taoism_data", () -> AttachmentType.<ITaoismData>builder(() -> ITaoismData.EMPTY)
                    .serialize(ITaoismData.MAP_CODEC)
                    .copyOnDeath()
                    .sync(ITaoismData.STREAM_CODEC)
                    .build()
            );


}