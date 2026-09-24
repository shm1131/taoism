package io.github.shm1131.taoism.block.alchemy;

import io.github.shm1131.taoism.init.BlockEntitiesRegister;
import io.github.shm1131.taoism.item.herb.PropertiesHelper;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.IHerbBase;
import io.github.shm1131.taoism.item.herb.base.Nature;
import io.github.shm1131.taoism.item.herb.pill.PillItem;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class AlchemyFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT_1 = 1; // 君药 (Monarch)
    public static final int SLOT_INPUT_2 = 2; // 臣药 (Minister)
    public static final int SLOT_INPUT_3 = 3; // 佐药 (Assistant)
    public static final int SLOT_OUTPUT = 4;
    public static final int NUM_SLOTS = 5;

    // 君臣佐权重
    private static final int WEIGHT_MONARCH = 3;
    private static final int WEIGHT_MINISTER = 2;
    private static final int WEIGHT_ASSISTANT = 1;

    // 默认炼丹时间（刻），20刻 = 1秒
    private static final int DEFAULT_BURN_TIME = 200;

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
        boolean wasBurning = be.fuelRemaining > 0;

        if (wasBurning) {
            be.fuelRemaining--;
            changed = true;
        }

        // 检查是否满足“必须三个药材”的炼丹条件
        boolean canCraft = be.canCraftPill();

        if (canCraft) {
            if (be.fuelRemaining <= 0) {
                int burnTime = getFuelBurnTime(sl, be.items[SLOT_FUEL]);
                if (burnTime > 0) {
                    be.fuelRemaining = burnTime;
                    be.fuelMaxTime = burnTime;
                    be.items[SLOT_FUEL].shrink(1);
                    wasBurning = true;
                    changed = true;
                }
            }

            if (be.fuelRemaining > 0) {
                be.totalBurnTime = DEFAULT_BURN_TIME;
                be.progress++;

                if (be.progress >= be.totalBurnTime) {
                    be.craftPill();
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

        boolean shouldBeLit = be.fuelRemaining > 0;
        if (wasBurning != shouldBeLit) {
            changed = true;
            level.setBlock(pos, state.setValue(AlchemyFurnaceBlock.LIT, shouldBeLit), 3);
        }

        if (changed) {
            be.setChanged();
        }
    }

    // ==================== 自定义炼丹逻辑 ====================

    /**
     * 检查是否可以炼丹：
     * 1. 必须放满三个输入槽
     * 2. 三个槽位必须全部都是有效药材
     * 3. 输出槽能容纳丹药
     */
    private boolean canCraftPill() {
        // 1. 必须三个槽都有物品
        if (items[SLOT_INPUT_1].isEmpty() || items[SLOT_INPUT_2].isEmpty() || items[SLOT_INPUT_3].isEmpty()) {
            return false;
        }

        // 2. 必须全是药材 (不能混入非药材物品)
        if (!isHerb(items[SLOT_INPUT_1]) || !isHerb(items[SLOT_INPUT_2]) || !isHerb(items[SLOT_INPUT_3])) {
            return false;
        }

        // 3. 检查输出槽
        ItemStack output = items[SLOT_OUTPUT];
        if (!output.isEmpty()) {
            if (!(output.getItem() instanceof PillItem)) return false;
            if (output.getCount() >= output.getMaxStackSize()) return false;
        }
        return true;
    }

    private boolean isHerb(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // 只要不是返回默认空属性，或者实现了 IHerbBase 接口，就认为是药材
        HerbProperties p = PropertiesHelper.getProperties(stack);
        return p != PropertiesHelper.DEFAULT_PROPS || stack.getItem() instanceof IHerbBase;
    }

    private void craftPill() {
        HerbProperties monarchProps = PropertiesHelper.getProperties(items[SLOT_INPUT_1]);
        HerbProperties ministerProps = PropertiesHelper.getProperties(items[SLOT_INPUT_2]);
        HerbProperties assistantProps = PropertiesHelper.getProperties(items[SLOT_INPUT_3]);

        // 核心算法：君臣佐加权计算
        HerbProperties pillProps = calculatePillProperties(monarchProps, ministerProps, assistantProps);

        ItemStack pillStack = PillItem.createPill(pillProps);

        if (items[SLOT_OUTPUT].isEmpty()) {
            items[SLOT_OUTPUT] = pillStack;
        } else {
            items[SLOT_OUTPUT].grow(pillStack.getCount());
        }

        // 消耗三个药材
        items[SLOT_INPUT_1].shrink(1);
        items[SLOT_INPUT_2].shrink(1);
        items[SLOT_INPUT_3].shrink(1);
    }

    /**
     * 核心算法：君臣佐加权计算
     */
    private HerbProperties calculatePillProperties(HerbProperties monarch, HerbProperties minister, HerbProperties assistant) {

        // 1. 计算 Flavor (味)
        Flavor finalFlavor = calculateWeightedEnum(
            monarch.flavor(), minister.flavor(), assistant.flavor(), Flavor.class
        );

        // 2. 计算 Nature (性)
        Nature finalNature = calculateWeightedEnum(
            monarch.nature(), minister.nature(), assistant.nature(), Nature.class
        );

        // 3. 计算 Toxicity (毒性) - 累加并限制上限
        float totalToxicity = monarch.toxicity() + minister.toxicity() + assistant.toxicity();
        float finalToxicity = Math.min(totalToxicity, 1.0f);

        // 4. 计算 Potency (药效) - 加权平均 (凸显君药的重要性)
        // 公式：(君*3 + 臣*2 + 佐*1) / 6
        float totalWeight = WEIGHT_MONARCH + WEIGHT_MINISTER + WEIGHT_ASSISTANT;
        float finalPotency = (monarch.potency() * WEIGHT_MONARCH +
            minister.potency() * WEIGHT_MINISTER +
            assistant.potency() * WEIGHT_ASSISTANT) / totalWeight;

        return new HerbProperties(finalFlavor, finalNature, finalToxicity, finalPotency);
    }

    /**
     * 通用加权枚举计算器 (支持 Flavor 和 Nature)
     * 权重：君(3) > 臣(2) > 佐(1)
     * 平局规则：取佐药(Assistant)的值
     */
    private <E extends Enum<E>> E calculateWeightedEnum(E monarch, E minister, E assistant, Class<E> enumClass) {
        Map<E, Integer> weights = new EnumMap<>(enumClass);

        // 初始化所有枚举值为 0 分
        for (E e : enumClass.getEnumConstants()) {
            weights.put(e, 0);
        }

        // 累加权重
        weights.put(monarch, weights.get(monarch) + WEIGHT_MONARCH);
        weights.put(minister, weights.get(minister) + WEIGHT_MINISTER);
        weights.put(assistant, weights.get(assistant) + WEIGHT_ASSISTANT);

        // 找出最高分
        int maxScore = 0;
        for (int score : weights.values()) {
            if (score > maxScore) maxScore = score;
        }

        // 检查是否平局 (有多个枚举值等于最高分)
        int countMax = 0;
        for (int score : weights.values()) {
            if (score == maxScore) countMax++;
        }

        // 如果平局，直接返回佐药(assistant)的属性
        if (countMax > 1) {
            return assistant;
        }

        // 否则返回最高分的枚举值
        for (Map.Entry<E, Integer> entry : weights.entrySet()) {
            if (entry.getValue() == maxScore) {
                return entry.getKey();
            }
        }

        // 兜底 (理论上不会走到这里)
        return assistant;
    }

    // ==================== 辅助与原版接口实现 (与之前相同) ====================

    private static int getFuelBurnTime(Level level, ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        var fuelValues = level.fuelValues();
        if (fuelValues == null) return 0;
        return fuelValues.burnDuration(fuel);
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
