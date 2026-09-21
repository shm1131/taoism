package io.github.shm1131.taoism.block.incubator;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.MenuTypesRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class IncubatorMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final ContainerData data;
    private final IncubatorBlockEntity blockEntity;

    public IncubatorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory,
            (IncubatorBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()),
            new SimpleContainerData(3));
    }

    public IncubatorMenu(int containerId, Inventory playerInventory,
                         IncubatorBlockEntity blockEntity, ContainerData data) {
        super(MenuTypesRegister.INCUBATOR_MENU.get(), containerId);

        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = data;

        this.addSlot(new Slot(blockEntity, 0, 26, 36));

        this.addSlot(new Slot(blockEntity, 1, 134, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
        });

        this.addSlot(new Slot(blockEntity, 2, 112, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                    8 + col * 18, 140 + row * 18));
            }
        }
        // Slot 30~38: 玩家快捷栏 (9)
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col,
                8 + col * 18, 198));
        }
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 1 || index == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    if (index < 30) {
                        if (!this.moveItemStackTo(itemstack1, 30, 39, false)) { // ✅ 29→30, 38→39
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 3, 30, false)) { // ✅ 2→3, 29→30
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockRegister.INCUBATOR.get());
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getTotalIncubationTime() {
        return this.data.get(1);
    }

    public boolean hasWaterSource() {
        return this.data.get(2) == 1;
    }

    public IncubatorBlockEntity getBE() {
        return this.blockEntity;
    }
}
