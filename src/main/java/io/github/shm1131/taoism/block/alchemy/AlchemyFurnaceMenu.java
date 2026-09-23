package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.MenuTypesRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AlchemyFurnaceMenu extends AbstractContainerMenu {

    public static final int FUEL_SLOT = 0;
    public static final int INPUT_SLOT_1 = 1; // 上方原料
    public static final int INPUT_SLOT_2 = 2; // 左侧原料
    public static final int INPUT_SLOT_3 = 3; // 右侧原料
    public static final int OUTPUT_SLOT = 4;  // 中心产物

    private final AlchemyFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public AlchemyFurnaceMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public AlchemyFurnaceMenu(int containerId, Inventory inv, BlockEntity be) {
        super(MenuTypesRegister.ALCHEMY_FURNACE.get(), containerId);

        if (be instanceof AlchemyFurnaceBlockEntity furnaceBE) {
            this.blockEntity = furnaceBE;
            checkContainerSize(this.blockEntity, 5);
            this.data = furnaceBE.getDataAccess();
        } else {
            this.blockEntity = null;
            this.data = new SimpleContainerData(4);
        }

        addDataSlots(this.data);

        int centerX = 80;
        int centerY = 72;
        int offset = 18;

        this.addSlot(new Slot(blockEntity, FUEL_SLOT, centerX, centerY + offset));

        this.addSlot(new Slot(blockEntity, INPUT_SLOT_1, centerX, centerY - offset));

        this.addSlot(new Slot(blockEntity, INPUT_SLOT_2, centerX - offset, centerY));

        this.addSlot(new Slot(blockEntity, INPUT_SLOT_3, centerX + offset, centerY));

        this.addSlot(new OutputSlot(blockEntity, OUTPUT_SLOT, centerX, centerY));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.blockEntity == null) return false;
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
            player, BlockRegister.ALCHEMY_FURNACE_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index == OUTPUT_SLOT) {
            if (!moveItemStackTo(stack, 5, 41, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, original);
        }
        else if (index >= 5) {
            if (isFuel(stack)) {
                if (!moveItemStackTo(stack, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                    if (!moveItemStackTo(stack, INPUT_SLOT_1, OUTPUT_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!moveItemStackTo(stack, INPUT_SLOT_1, OUTPUT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        else {
            if (!moveItemStackTo(stack, 5, 41, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    public static boolean isFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return stack.is(net.minecraft.tags.ItemTags.COALS);
    }

    public int getBurnProgress() { return data.get(0); }
    public int getTotalBurnTime() { return data.get(1); }
    public int getFuelRemaining() { return data.get(2); }
    public int getFuelMaxTime() { return data.get(3); }
    public int getProgress() { return data.get(0); }

    public float getBurnProgressFraction() {
        int max = getTotalBurnTime();
        return max > 0 ? (float) getBurnProgress() / max : 0f;
    }

    public float getFuelFraction() {
        int max = getFuelMaxTime();
        return max > 0 ? (float) getFuelRemaining() / max : 0f;
    }

    private static class OutputSlot extends Slot {
        public OutputSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
