package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceInput;
import io.github.shm1131.taoism.block.alchemy.recipe.AlchemyFurnaceRecipe;
import io.github.shm1131.taoism.init.BlockEntitiesRegister;
import io.github.shm1131.taoism.init.RecipeTypesRegister;
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
        super(BlockEntitiesRegister.ALCHEMY_FURNACE.get(), pos, state);
        for (int i = 0; i < NUM_SLOTS; i++) items[i] = ItemStack.EMPTY;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyFurnaceBlockEntity be) {
        if (!(level instanceof ServerLevel sl)) return;

        boolean changed = false;
        boolean isBurning = be.fuelRemaining > 0;

        if (isBurning) {
            be.fuelRemaining--;
            changed = true;
        }

        AlchemyFurnaceInput recipeInput = be.createRecipeInput();
        Optional<RecipeHolder<AlchemyFurnaceRecipe>> opt = sl.recipeAccess()
            .getRecipeFor(RecipeTypesRegister.ALCHEMY_FURNACE_TYPE.get(), recipeInput, sl);

        if (opt.isPresent()) {
            RecipeHolder<AlchemyFurnaceRecipe> recipeHolder = opt.get();
            AlchemyFurnaceRecipe recipe = recipeHolder.value();

            if (canOutput(be, recipeHolder)) {
                if (be.fuelRemaining <= 0) {
                    int burnTime = getFuelBurnTime(sl,be.items[SLOT_FUEL]);
                    if (burnTime > 0) {
                        be.fuelRemaining = burnTime;
                        be.fuelMaxTime = burnTime;
                        be.items[SLOT_FUEL].shrink(1);
                        isBurning = true;
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
                if (be.progress != 0) {
                    be.progress = 0;
                    changed = true;
                }
            }
        } else {
            if (be.progress != 0) {
                be.progress = 0;
                changed = true;
            }
        }

        boolean shouldBeLit = be.fuelRemaining > 0;
        if (isBurning != shouldBeLit) {
            changed = true;
            level.setBlock(pos, state.setValue(AlchemyFurnaceBlock.LIT, shouldBeLit), 3);
        }

        if (changed) {
            be.setChanged();
        }
    }


    private AlchemyFurnaceInput createRecipeInput() {
        List<ItemStack> inputs = List.of(
            items[SLOT_INPUT_1],
            items[SLOT_INPUT_2],
            items[SLOT_INPUT_3]
        );
        return new AlchemyFurnaceInput(items[SLOT_FUEL], inputs);
    }

    private static int getFuelBurnTime(Level level, ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        var fuelValues = level.fuelValues();
        if (fuelValues == null) return 0; // 安全兜底
        return fuelValues.burnDuration(fuel);
    }

    private static boolean canOutput(AlchemyFurnaceBlockEntity be, RecipeHolder<AlchemyFurnaceRecipe> recipe) {
        AlchemyFurnaceInput input = be.createRecipeInput();
        ItemStack result = recipe.value().assemble(input);

        if (result == null || result.isEmpty()) return false;

        ItemStack output = be.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
            && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void completeRecipe(AlchemyFurnaceRecipe recipe, AlchemyFurnaceInput input) {
        ItemStack result = recipe.assemble(input);
        if (result == null || result.isEmpty()) return; // 防御性检查

        if (items[SLOT_OUTPUT].isEmpty()) {
            items[SLOT_OUTPUT] = result.copy();
        } else {
            items[SLOT_OUTPUT].grow(result.getCount());
        }

        items[SLOT_INPUT_1].shrink(1);
        items[SLOT_INPUT_2].shrink(1);
        items[SLOT_INPUT_3].shrink(1);
    }

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

    @Override public int getContainerSize() { return NUM_SLOTS; }
    @Override public boolean isEmpty() { for(var s:items) if(!s.isEmpty()) return false; return true; }
    @Override public ItemStack getItem(int slot) { return (slot>=0&&slot<NUM_SLOTS)?items[slot]:ItemStack.EMPTY; }

    @Override public ItemStack removeItem(int slot, int amount) {
        if(slot>=0&&slot<NUM_SLOTS&&!items[slot].isEmpty()){
            var r = items[slot].split(amount);
            setChanged();
            return r;
        }
        return ItemStack.EMPTY;
    }

    @Override public ItemStack removeItemNoUpdate(int slot) {
        if(slot>=0&&slot<NUM_SLOTS){
            var s = items[slot];
            items[slot] = ItemStack.EMPTY;
            return s;
        }
        return ItemStack.EMPTY;
    }

    @Override public void setItem(int slot, ItemStack stack) {
        if(slot>=0&&slot<NUM_SLOTS){
            items[slot] = stack;
            if(!stack.isEmpty()&&stack.getCount()>getMaxStackSize(stack)) stack.setCount(getMaxStackSize(stack));
            setChanged();
        }
    }

    @Override public int getMaxStackSize(ItemStack s){return s.getMaxStackSize();}
    @Override public boolean stillValid(Player p){return Container.stillValidBlockEntity(this,p);}
    @Override public void clearContent(){for(int i=0;i<NUM_SLOTS;i++) items[i]=ItemStack.EMPTY; setChanged();}

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
        int idx = 0;
        for (var s : list) { if(idx<NUM_SLOTS) items[idx++]=s; }
        while(idx<NUM_SLOTS) items[idx++]=ItemStack.EMPTY;

        progress = input.getIntOr("Progress",0);
        totalBurnTime = input.getIntOr("TotalBurnTime",0);
        fuelRemaining = input.getIntOr("FuelRemaining",0);
        fuelMaxTime = input.getIntOr("FuelMaxTime",0);
    }
}
