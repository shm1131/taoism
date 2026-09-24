package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.init.BlockEntitiesRegister;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlockEntity extends BlockEntity implements MenuProvider, Container {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int NUM_SLOTS = 2;
    public static final int WATER_CAPACITY = 1000;
    public static final int WATER_PER_TICK = 1;

    private static final int INCUBATION_TIME = 200;
    private static final int OUTPUT_COUNT = 1;

    private final ItemStack[] items = new ItemStack[NUM_SLOTS];
    private SimpleFluidContent waterContent = SimpleFluidContent.EMPTY;

    private int progress = 0;
    private boolean hasValidInput = false;

    private final ContainerData dataAccess = new ContainerData() {
        @Override public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> INCUBATION_TIME;
                case 2 -> waterContent.getAmount();
                case 3 -> WATER_CAPACITY;
                default -> 0;
            };
        }
        @Override public void set(int index, int value) {
            if (index == 0) progress = value;
        }
        @Override public int getCount() { return 4; }
    };

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegister.INCUBATOR.get(), pos, state);
        for (int i = 0; i < NUM_SLOTS; i++) items[i] = ItemStack.EMPTY;
    }

    public int getWaterAmount() { return waterContent.getAmount(); }

    public int fillWater(FluidStack resource, boolean simulate) {
        if (!resource.is(Fluids.WATER)) return 0;
        int space = WATER_CAPACITY - waterContent.getAmount();
        int toFill = Math.min(resource.getAmount(), space);
        if (toFill <= 0) return 0;

        if (!simulate) {
            FluidStack newStack = waterContent.isEmpty()
                ? new FluidStack(Fluids.WATER, toFill)
                : waterContent.copy();
            if (!waterContent.isEmpty()) newStack.grow(toFill);

            this.waterContent = SimpleFluidContent.copyOf(newStack);
            setChanged();
            notifyMenu();
        }
        return toFill;
    }

    public boolean consumeWater(int amount) {
        if (waterContent.getAmount() < amount) return false;
        FluidStack newStack = waterContent.copy();
        newStack.shrink(amount);
        this.waterContent = newStack.isEmpty()
            ? SimpleFluidContent.EMPTY
            : SimpleFluidContent.copyOf(newStack);
        setChanged();
        return true;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, IncubatorBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        boolean changed = false;
        ItemStack input = be.getItem(SLOT_INPUT);

        boolean validNow = !input.isEmpty();
        if (validNow != be.hasValidInput) {
            be.hasValidInput = validNow;
            be.progress = 0;
            changed = true;
        }

        if (be.hasValidInput && canOutput(be)) {
            if (be.consumeWater(WATER_PER_TICK)) {
                be.progress++;
                if (be.progress >= INCUBATION_TIME) {
                    produceOutput(be);
                    be.progress = 0;
                }
                changed = true;
            }
        } else if (be.progress > 0) {
            be.progress = 0;
            changed = true;
        }

        if (changed) {
            be.setChanged();
            be.notifyMenu();
            serverLevel.getChunkSource().blockChanged(pos);
        }
    }

    private static boolean canOutput(IncubatorBlockEntity be) {
        ItemStack result = new ItemStack(net.minecraft.world.item.Items.EGG, OUTPUT_COUNT);
        ItemStack out = be.getItem(SLOT_OUTPUT);
        if (out.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(out, result)
            && out.getCount() + result.getCount() <= out.getMaxStackSize();
    }

    private static void produceOutput(IncubatorBlockEntity be) {
        ItemStack result = new ItemStack(net.minecraft.world.item.Items.EGG, OUTPUT_COUNT);
        ItemStack out = be.getItem(SLOT_OUTPUT);

        if (out.isEmpty()) {
            be.setItem(SLOT_OUTPUT, result.copy());
        } else if (ItemStack.isSameItemSameComponents(out, result)) {
            out.grow(result.getCount());
        }
        be.removeItem(SLOT_INPUT, 1);
    }

    private void notifyMenu() {
        if (!(this.level instanceof ServerLevel sl)) return;
        for (var p : sl.players()) {
            if (p.containerMenu instanceof IncubatorMenu m && m.getBlockEntity() == this) {
                m.slotsChanged(this);
                m.broadcastChanges();
            }
        }
    }

    @Override public int getContainerSize() { return NUM_SLOTS; }
    @Override public boolean isEmpty() {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }
    @Override public ItemStack getItem(int slot) {
        return (slot >= 0 && slot < NUM_SLOTS) ? items[slot] : ItemStack.EMPTY;
    }
    @Override public ItemStack removeItem(int slot, int amount) {
        if (slot >= 0 && slot < NUM_SLOTS && !items[slot].isEmpty()) {
            ItemStack split = items[slot].split(amount);
            if (!split.isEmpty()) setChanged();
            return split;
        }
        return ItemStack.EMPTY;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            ItemStack s = items[slot];
            items[slot] = ItemStack.EMPTY;
            return s;
        }
        return ItemStack.EMPTY;
    }
    @Override public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < NUM_SLOTS) {
            items[slot] = stack;
            if (!stack.isEmpty() && stack.getCount() > getMaxStackSize(stack))
                stack.setCount(getMaxStackSize(stack));
            setChanged();
        }
    }
    @Override public int getMaxStackSize(ItemStack stack) { return stack.getMaxStackSize(); }
    @Override public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
    @Override public void clearContent() {
        for (int i = 0; i < NUM_SLOTS; i++) items[i] = ItemStack.EMPTY;
        this.waterContent = SimpleFluidContent.EMPTY;
        this.progress = 0;
        this.hasValidInput = false;
        setChanged();
    }

    @Override public Component getDisplayName() { return Component.translatable("container.taoism.incubator"); }

    @Nullable @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        if (level == null || level.isClientSide()) return null;
        return new IncubatorMenu(id, inv, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        var itemList = output.list("Items", ItemStack.OPTIONAL_CODEC);
        for (ItemStack stack : items) {
            itemList.add(stack);
        }

        if (!waterContent.isEmpty()) {
            output.store("Water", SimpleFluidContent.CODEC, waterContent);
        }
        output.putInt("Progress", progress);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        var list = input.listOrEmpty("Items", ItemStack.OPTIONAL_CODEC);
        int idx = 0;
        for (ItemStack stack : list) {
            if (idx < NUM_SLOTS) {
                items[idx++] = stack;
            }
        }
        while (idx < NUM_SLOTS) {
            items[idx++] = ItemStack.EMPTY;
        }

        try {
            this.waterContent = input.read("Water", SimpleFluidContent.CODEC)
                .orElse(SimpleFluidContent.EMPTY);
        } catch (Exception e) {
            TaoismMain.LOGGER.error("Failed to load water content at {}, resetting to empty", getBlockPos(), e);
            this.waterContent = SimpleFluidContent.EMPTY;
        }

        progress = input.getIntOr("Progress", 0);
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Nullable @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
