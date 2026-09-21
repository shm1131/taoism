package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.block.incubator.recipe.IncubatorRecipe;
import io.github.shm1131.taoism.init.BlockEntitiesRegister;
import io.github.shm1131.taoism.init.RecipeTypesRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IncubatorBlockEntity extends BlockEntity implements MenuProvider, Container {

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT_1 = 1;
    private static final int SLOT_OUTPUT_2 = 2;
    private static final int NUM_SLOTS = 3;
    private static final int WATER_CHECK_INTERVAL = 20;

    private final ItemStack[] items = new ItemStack[NUM_SLOTS];
    private int progress = 0;
    private int totalIncubationTime = 0;
    @Nullable
    private RecipeHolder<IncubatorRecipe> currentRecipe = null;

    private boolean hasWaterSource = false;
    private int waterCheckTimer = 0;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> totalIncubationTime;
                case 2 -> hasWaterSource ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> totalIncubationTime = value;
                case 2 -> hasWaterSource = (value == 1);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegister.INCUBATOR.get(), pos, state);
        for (int i = 0; i < NUM_SLOTS; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public int getContainerSize() {
        return NUM_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            return items[slot];
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot >= 0 && slot < NUM_SLOTS && !items[slot].isEmpty()) {
            ItemStack result = items[slot].split(amount);
            if (!result.isEmpty()) {
                this.setChanged();
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            ItemStack stack = items[slot];
            items[slot] = ItemStack.EMPTY;
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            items[slot] = stack;
            if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize(stack)) {
                stack.setCount(this.getMaxStackSize(stack));
            }
            this.setChanged();
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            items[i] = ItemStack.EMPTY;
        }
        this.setChanged();
    }


    public static void serverTick(Level level, BlockPos pos, BlockState state, IncubatorBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        boolean changed = false;

        if (++be.waterCheckTimer >= WATER_CHECK_INTERVAL) {
            be.waterCheckTimer = 0;
            boolean oldHasWater = be.hasWaterSource;
            be.hasWaterSource = checkForWaterSource(level, pos);
            if (oldHasWater != be.hasWaterSource) changed = true;
        }

        ItemStack input = be.getItem(SLOT_INPUT);

        if (!input.isEmpty()) {
            if (be.currentRecipe == null || !matchesRecipe(be.currentRecipe, input)) {
                Optional<RecipeHolder<IncubatorRecipe>> opt = findRecipe(serverLevel, input);
                if (opt.isPresent()) {
                    be.currentRecipe = opt.get();
                    be.totalIncubationTime = be.currentRecipe.value().getIncubationTime();
                } else {
                    be.currentRecipe = null;
                    be.totalIncubationTime = 0;
                }
                be.progress = 0;
                changed = true;
            }
        } else if (be.currentRecipe != null) {
            be.currentRecipe = null;
            be.progress = 0;
            be.totalIncubationTime = 0;
            changed = true;
        }

        if (be.hasWaterSource && be.currentRecipe != null && canOutput(be, be.currentRecipe)) {
            be.progress++;
            if (be.progress >= be.totalIncubationTime) {
                completeRecipe(be, be.currentRecipe);
                be.progress = 0;
                changed = true;
            }
        }

        if (changed) {
            be.setChanged();
            be.notifyMenu();
            serverLevel.getChunkSource().blockChanged(pos);
        }
    }

    private void notifyMenu() {
        if (!(this.level instanceof ServerLevel serverLevel)) return;

        for (var player : serverLevel.players()) {
            if (player.containerMenu instanceof IncubatorMenu menu
                && menu.getBE() == this) {
                menu.slotsChanged(this);
                menu.broadcastChanges();
            }
        }
    }

    private static boolean checkForWaterSource(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            var fluid = level.getFluidState(pos.relative(dir));
            if (fluid.isSource() && fluid.getType() == Fluids.WATER) return true;
        }
        return false;
    }

    private static Optional<RecipeHolder<IncubatorRecipe>> findRecipe(ServerLevel level, ItemStack input) {
        return level.recipeAccess().getRecipeFor(
            RecipeTypesRegister.INCUBATOR_TYPE.get(),
            new SingleRecipeInput(input),
            level
        );
    }

    private static boolean matchesRecipe(RecipeHolder<IncubatorRecipe> recipe, ItemStack input) {
        return recipe.value().input().test(input);
    }

    private static boolean canOutput(IncubatorBlockEntity be, RecipeHolder<IncubatorRecipe> recipe) {
        ItemStack result = recipe.value().assemble(new SingleRecipeInput(be.getItem(SLOT_INPUT)));

        for (int slot : new int[]{SLOT_OUTPUT_1, SLOT_OUTPUT_2}) {
            ItemStack output = be.getItem(slot);
            if (output.isEmpty()) return true;
            if (ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private static void completeRecipe(IncubatorBlockEntity be, RecipeHolder<IncubatorRecipe> recipe) {
        ItemStack result = recipe.value().assemble(new SingleRecipeInput(be.getItem(SLOT_INPUT)));

        boolean placed = false;
        for (int slot : new int[]{SLOT_OUTPUT_1, SLOT_OUTPUT_2}) {
            ItemStack output = be.getItem(slot);
            if (output.isEmpty()) {
                be.setItem(slot, result.copy());
                placed = true;
                break;
            } else if (ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                output.grow(result.getCount());
                placed = true;
                break;
            }
        }

        if (!placed) {
            throw new IllegalStateException("Incubator output full despite canOutput check");
        }
        be.removeItem(SLOT_INPUT, 1);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        var itemList = output.list("Items", ItemStack.CODEC);
        for (ItemStack stack : items) {
            itemList.add(stack);
        }
        output.putInt("Progress", progress);
        output.putInt("TotalIncubationTime", totalIncubationTime);
        output.putBoolean("HasWaterSource", hasWaterSource);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        var itemList = input.listOrEmpty("Items", ItemStack.CODEC);
        int index = 0;
        for (ItemStack stack : itemList) {
            if (index < NUM_SLOTS) {
                items[index] = stack;
            }
            index++;
        }
        while (index < NUM_SLOTS) {
            items[index] = ItemStack.EMPTY;
            index++;
        }
        progress = input.getIntOr("Progress", 0);
        totalIncubationTime = input.getIntOr("TotalIncubationTime", 0);
        hasWaterSource = input.getBooleanOr("HasWaterSource", false);
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("container.taoism.incubator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        if (this.level == null || this.level.isClientSide()) {
            return null;
        }
        return new IncubatorMenu(containerId, playerInventory, this, this.dataAccess);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
