package io.github.shm1131.taoism.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

public class HerbDropModifier extends LootModifier {

    public static final MapCodec<HerbDropModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(
                    inst.group(
                            Identifier.CODEC.fieldOf("herb").forGetter(m -> m.herbId),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.count)
                    )
            ).apply(inst, HerbDropModifier::new)
    );

    private final Identifier herbId;
    private final int count;

    protected HerbDropModifier(LootItemCondition[] conditions, int priority,
                               Identifier herbId, int count) {
        super(conditions, priority);
        this.herbId = herbId;
        this.count = count;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
                                                 LootContext context) {
        Optional<Holder.Reference<Item>> holderOpt = BuiltInRegistries.ITEM.get(this.herbId);
        Item item = holderOpt.map(Holder.Reference::value).orElse(Items.AIR);

        if (item != Items.AIR) {
            generatedLoot.add(new ItemStack(item, this.count));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}