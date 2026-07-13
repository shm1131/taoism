package io.github.kanybd1.Taoism.block;

import io.github.kanybd1.Taoism.TaoismMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TaoismMain.MODID);
}
