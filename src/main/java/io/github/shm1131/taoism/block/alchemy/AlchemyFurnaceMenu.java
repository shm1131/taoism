package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.init.BlockRegister;
import io.github.shm1131.taoism.init.ModMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AlchemyFurnaceMenu extends AbstractContainerMenu {

    // 槽位索引常量
    public static final int FUEL_SLOT = 0;
    public static final int INPUT_SLOT_1 = 1;
    public static final int INPUT_SLOT_2 = 2;
    public static final int INPUT_SLOT_3 = 3;
    public static final int OUTPUT_SLOT = 4;

    private final AlchemyFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    // ==================== 客户端构造器 ====================
    public AlchemyFurnaceMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    // ==================== 服务端/通用构造器 ====================
    public AlchemyFurnaceMenu(int containerId, Inventory inv, BlockEntity be) {
        super(ModMenuTypes.ALCHEMY_FURNACE.get(), containerId);
        checkContainerSize(inv, 5);
        this.blockEntity = (AlchemyFurnaceBlockEntity) be;
        this.data = new SimpleContainerData(4); // [burnProgress, maxBurnTime, fuelRemaining, maxFuel]

        if (be instanceof AlchemyFurnaceBlockEntity furnace) {
            addDataSlots(furnace.getDataAccess());
        } else {
            addDataSlots(this.data); // 客户端回退
        }

        int startX = 78; // 中心格 X
        int startY = 70; // 中心格 Y
        int slotSize = 18; // 标准槽位偏移量

        this.addSlot(new Slot(blockEntity, 0, startX, startY));
        this.addSlot(new Slot(blockEntity, 1, startX, startY - slotSize));
        this.addSlot(new Slot(blockEntity, 2, startX - slotSize, startY));
        this.addSlot(new Slot(blockEntity, 3, startX + slotSize, startY));
        this.addSlot(new Slot(blockEntity, 4, startX, startY + slotSize));

        // === 玩家背包 ===
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // 玩家快捷栏
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
            player, BlockRegister.ALCHEMY_FURNACE_BLOCK.get());
    }

    // ==================== Shift-Click 逻辑 ====================
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        // 产物槽 → 玩家背包
        if (index == OUTPUT_SLOT) {
            if (!moveItemStackTo(stack, 5, 41, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, original);
        }
        // 玩家背包 → 丹炉
        else if (index >= 5) {
            // 优先尝试放入燃料槽
            if (isFuel(stack) && !moveItemStackTo(stack, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                // 再尝试放入原料槽
                if (!moveItemStackTo(stack, INPUT_SLOT_1, OUTPUT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        // 丹炉槽位 → 玩家背包
        else {
            if (!moveItemStackTo(stack, 5, 41, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    // ==================== 辅助方法 ====================

    /**
     * 判断物品是否可作为燃料
     * ⚠️ 1.21.4+ 推荐使用 FuelRegistry 或自定义标签
     */
    private boolean isFuel(ItemStack stack) {

        return stack.is(ItemTags.FURNACE_MINECART_FUEL);
    }

    // ==================== 客户端数据访问器 ====================

    /** 燃烧进度 (0~maxBurnTime) */
    public int getBurnProgress() {
        return data.get(0);
    }

    /** 最大燃烧时间 */
    public int getTotalBurnTime() {
        return data.get(1);
    }

    /** 当前燃料剩余 ticks */
    public int getFuelRemaining() {
        return data.get(2);
    }

    /** 当前燃料最大 ticks */
    public int getFuelMaxTime() {
        return data.get(3);
    }

    public int getProgress() { return data.get(0); }

    /** 获取燃烧进度百分比 (0.0 ~ 1.0) */
    public float getBurnProgressFraction() {
        int max = getTotalBurnTime();
        return max > 0 ? (float) getBurnProgress() / max : 0f;
    }

    /** 获取燃料剩余百分比 (0.0 ~ 1.0) */
    public float getFuelFraction() {
        int max = getFuelMaxTime();
        return max > 0 ? (float) getFuelRemaining() / max : 0f;
    }

    // ==================== 产物槽（禁止放入） ====================
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
