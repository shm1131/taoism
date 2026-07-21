package io.github.shm1131.taoism.datagen.world.biomes;

import com.mojang.serialization.MapCodec;
import io.github.shm1131.taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BiomeSourceRegister {

  public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES =
      DeferredRegister.create(Registries.BIOME_SOURCE, TaoismMain.MODID);

  public static final Supplier<MapCodec<? extends BiomeSource>> RING_BIOME_SOURCE =
      BIOME_SOURCES.register("ring", () -> RingBiomeSource.CODEC);
}