package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.player.attachment.api.IDeathInventoryData;
import io.github.shm1131.taoism.player.attachment.api.IJingLiData;
import io.github.shm1131.taoism.player.attachment.api.ITaoismData;
import io.github.shm1131.taoism.player.attachment.api.ICultivationData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TaoismAttachments {
  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TaoismMain.MODID);

  public static final Supplier<AttachmentType<ITaoismData>> TAOISM_DATA =
      ATTACHMENT_TYPES.register("taoism_data", () -> AttachmentType.builder(() -> ITaoismData.EMPTY)
          .serialize(ITaoismData.MAP_CODEC)
          .copyOnDeath()
          .sync(ITaoismData.STREAM_CODEC)
          .build()
      );

    public static final Supplier<AttachmentType<ICultivationData>> CULTIVATION_DATA =
        ATTACHMENT_TYPES.register("cultivation_data", () -> AttachmentType.builder(() -> ICultivationData.EMPTY)
            .serialize(ICultivationData.MAP_CODEC)
            .copyOnDeath()
            .sync(ICultivationData.STREAM_CODEC)
            .build()
        );

    public static final Supplier<AttachmentType<IDeathInventoryData>> DEATH_INVENTORY =
        ATTACHMENT_TYPES.register("death_inventory", () -> AttachmentType.builder(() -> IDeathInventoryData.EMPTY)
            .serialize(IDeathInventoryData.MAP_CODEC)
            .copyOnDeath()
            .build()
        );

    public static final Supplier<AttachmentType<IJingLiData>> JINGLI_DATA =
        ATTACHMENT_TYPES.register("jing_li_data", () -> AttachmentType.builder(() -> IJingLiData.EMPTY)
            .serialize(IJingLiData.MAP_CODEC)
            .copyOnDeath()
            .sync(IJingLiData.STREAM_CODEC)
            .build()
        );

}
