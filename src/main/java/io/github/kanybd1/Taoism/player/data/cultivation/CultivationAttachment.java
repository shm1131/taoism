package io.github.kanybd1.Taoism.player.data.cultivation;

import io.github.kanybd1.Taoism.TaoismMain;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CultivationAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TaoismMain.MODID);

    public static final Supplier<AttachmentType<ICultivationData>> TAOISM_DATA =
            ATTACHMENT_TYPES.register("cultivation_data", () -> AttachmentType.<ICultivationData>builder(() -> ICultivationData.EMPTY)
                    .serialize(ICultivationData.MAP_CODEC)
                    .copyOnDeath()
                    .sync(ICultivationData.STREAM_CODEC)
                    .build()
            );
}
