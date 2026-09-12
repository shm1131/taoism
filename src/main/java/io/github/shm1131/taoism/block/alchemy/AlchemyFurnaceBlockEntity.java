package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceInput;
import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceRecipe;
import io.github.shm1131.taoism.init.ModBlockEntities;
import io.github.shm1131.taoism.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AlchemyFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_INPUT_2 = 2;
    public static final int SLOT_INPUT_3 = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int NUM_SLOTS = 5;

    private final ItemStack[] items = new ItemStack[NUM_SLOTS];

    private int progress = 0;
    private int totalBurnTime = 0;
    private int fuelRemaining = 0;
    private int fuelMaxTime = 0;

    @Nullable private RecipeHolder<AlchemyFurnaceRecipe> currentRecipe = null;

    private final ContainerData dataAccess = new ContainerData() {
        @Override public int get(int i) { return switch(i) {
            case 0 -> progress; case 1 -> totalBurnTime;
            case 2 -> fuelRemaining; case 3 -> fuelMaxTime; default -> 0; }; }
        @Override public void set(int i, int v) { switch(i) {
            case 0 -> progress = v; case 1 -> totalBurnTime = v;
            case 2 -> fuelRemaining = v; case 3 -> fuelMaxTime = v; } }
        @Override public int getCount() { return 4; }
    };

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    public AlchemyFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_FURNACE.get(), pos, state);
        for (int i = 0; i < NUM_SLOTS; i++) items[i] = ItemStack.EMPTY;
    }

    // ==================== Tick 逻辑 ====================

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyFurnaceBlockEntity be) {
        if (!(level instanceof ServerLevel sl)) return;
        boolean changed = false;

        // 1. 燃料消耗
        if (be.fuelRemaining > 0) {
            be.fuelRemaining--;
            changed = true;
        }

        AlchemyFurnaceInput recipeInput = be.createRecipeInput();
        Optional<RecipeHolder<AlchemyFurnaceRecipe>> opt = sl.recipeAccess()
            .getRecipeFor(ModRecipeTypes.ALCHEMY_FURNACE_TYPE.get(), recipeInput, sl);

        if (opt.isPresent()) {
            RecipeHolder<AlchemyFurnaceRecipe> recipeHolder = opt.get();
            AlchemyFurnaceRecipe recipe = recipeHolder.value();

            if (canOutput(be, recipeHolder)) {
                if (be.fuelRemaining <= 0) {
                    int burnTime = getFuelBurnTime(be.items[SLOT_FUEL]);
                    if (burnTime > 0) {
                        be.fuelRemaining = burnTime;
                        be.fuelMaxTime = burnTime;
                        be.items[SLOT_FUEL].shrink(1);
                        changed = true;
                    }
                }

                if (be.fuelRemaining > 0) {
                    be.totalBurnTime = recipe.burnTime();
                    be.progress++;
                    if (be.progress >= be.totalBurnTime) {
                        be.completeRecipe(recipe, recipeInput);
                        be.progress = 0;
                    }
                    changed = true;
                }
            } else {
                changed = true;
            }
        } else {
            if (be.progress != 0) {
                be.progress = 0;
                changed = true;
            }
        }

        if (changed) {
            be.setChanged();
            sl.getChunkSource().blockChanged(pos);
        }
    }

    // ==================== 辅助方法 (已修复) ====================

    private AlchemyFurnaceInput createRecipeInput() {
        List<ItemStack> inputs = List.of(
            items[SLOT_INPUT_1],
            items[SLOT_INPUT_2],
            items[SLOT_INPUT_3]
        );
        return new AlchemyFurnaceInput(items[SLOT_FUEL], inputs);
    }

    private static int getFuelBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        // 建议替换为: return net.neoforged.neoforge.common.FurnaceFuelRegistry.getBurnTime(fuel, null);
        if (fuel.is(net.minecraft.tags.ItemTags.COALS)) return 1600;
        if (fuel.is(net.minecraft.world.item.Items.BLAZE_ROD)) return 2400;
        return 0;
    }

    private static boolean canOutput(AlchemyFurnaceBlockEntity be, RecipeHolder<AlchemyFurnaceRecipe> recipe) {
        AlchemyFurnaceInput input = be.createRecipeInput();
        ItemStack result = recipe.value().assemble(input);

        ItemStack output = be.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
            && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void completeRecipe(AlchemyFurnaceRecipe recipe, AlchemyFurnaceInput input) {
        ItemStack result = recipe.assemble(input);

        if (items[SLOT_OUTPUT].isEmpty()) {
            items[SLOT_OUTPUT] = result.copy();
        } else {
            items[SLOT_OUTPUT].grow(result.getCount());
        }

        items[SLOT_INPUT_1].shrink(1);
        items[SLOT_INPUT_2].shrink(1);
        items[SLOT_INPUT_3].shrink(1);
    }

    // ==================== MenuProvider (已修复) ====================
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.taoism.alchemy_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
        if (level == null || level.isClientSide()) return null;
        return new AlchemyFurnaceMenu(id, inv, this);
    }

    // ==================== 辅助方法 ====================

    private void completeRecipe(AlchemyFurnaceRecipe recipe) {
        ItemStack result = recipe.resultTemplate().create();
        if (items[SLOT_OUTPUT].isEmpty()) {
            items[SLOT_OUTPUT] = result;
        } else {
            items[SLOT_OUTPUT].grow(result.getCount());
        }
        // 消耗3个原料各1个
        items[SLOT_INPUT_1].shrink(1);
        items[SLOT_INPUT_2].shrink(1);
        items[SLOT_INPUT_3].shrink(1);
    }

    // ==================== Container 接口实现 ====================

    @Override public int getContainerSize() { return NUM_SLOTS; }
    @Override public boolean isEmpty() { for(var s:items) if(!s.isEmpty()) return false; return true; }
    @Override public ItemStack getItem(int slot) { return (slot>=0&&slot<NUM_SLOTS)?items[slot]:ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int amount) {
        if(slot>=0&&slot<NUM_SLOTS&&!items[slot].isEmpty()){var r=items[slot].split(amount);setChanged();return r;}return ItemStack.EMPTY;}
    @Override public ItemStack removeItemNoUpdate(int slot) {
        if(slot>=0&&slot<NUM_SLOTS){var s=items[slot];items[slot]=ItemStack.EMPTY;return s;}return ItemStack.EMPTY;}
    @Override public void setItem(int slot, ItemStack stack) {
        if(slot>=0&&slot<NUM_SLOTS){items[slot]=stack;if(!stack.isEmpty()&&stack.getCount()>getMaxStackSize(stack))stack.setCount(getMaxStackSize(stack));setChanged();}}
    @Override public int getMaxStackSize(ItemStack s){return s.getMaxStackSize();}
    @Override public boolean stillValid(Player p){return Container.stillValidBlockEntity(this,p);}
    @Override public void clearContent(){for(int i=0;i<NUM_SLOTS;i++)items[i]=ItemStack.EMPTY;setChanged();}

    // ==================== NBT & MenuProvider ====================
    @Override protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        var list = output.list("Items", ItemStack.CODEC);
        for (ItemStack s : items) list.add(s);
        output.putInt("Progress", progress);
        output.putInt("TotalBurnTime", totalBurnTime);
        output.putInt("FuelRemaining", fuelRemaining);
        output.putInt("FuelMaxTime", fuelMaxTime);
    }

    @Override protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        var list = input.listOrEmpty("Items", ItemStack.CODEC);
        int idx = 0; for (var s : list) { if(idx<NUM_SLOTS) items[idx++]=s; }
        while(idx<NUM_SLOTS) items[idx++]=ItemStack.EMPTY;
        progress = input.getIntOr("Progress",0);
        totalBurnTime = input.getIntOr("TotalBurnTime",0);
        fuelRemaining = input.getIntOr("FuelRemaining",0);
        fuelMaxTime = input.getIntOr("FuelMaxTime",0);
    }
}
