package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.MenuTypesRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class IncubatorMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private final IncubatorBlockEntity blockEntity;
    private final ContainerData data;

    public IncubatorMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public IncubatorMenu(int containerId, Inventory inv, BlockEntity be) {
        super(MenuTypesRegister.INCUBATOR_MENU.get(), containerId);

        Container safeContainer;
        if (be instanceof IncubatorBlockEntity incubatorBE) {
            this.blockEntity = incubatorBE;
            this.data = incubatorBE.getDataAccess();
            safeContainer = incubatorBE;
        } else {
            this.blockEntity = null;
            this.data = new SimpleContainerData(4);
            safeContainer = new net.minecraft.world.SimpleContainer(2);
        }

        this.addSlot(new Slot(safeContainer, INPUT_SLOT, 26, 36));
        this.addSlot(new Slot(safeContainer, OUTPUT_SLOT, 134, 36) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }

        this.addDataSlots(this.data);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.blockEntity == null) return false;
        return stillValid(
            ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
            player,
            BlockRegister.INCUBATOR.get()
        );
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index == OUTPUT_SLOT) {
            if (!this.moveItemStackTo(stack, 2, 38, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, original);
        } else if (index >= 2) {
            if (!this.moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, 2, 38, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }


    public IncubatorBlockEntity getBlockEntity() { return this.blockEntity; }

    public int getProgress() { return this.data.get(0); }

    public int getMaxProgress() { return this.data.get(1); }

    public int getWaterAmount() { return this.data.get(2); }

    public int getWaterCapacity() { return this.data.get(3); }

    public float getProgressFraction() {
        int max = getMaxProgress();
        return max > 0 ? (float) getProgress() / max : 0f;
    }

    public boolean hasWater() { return getWaterAmount() > 0; }
}
