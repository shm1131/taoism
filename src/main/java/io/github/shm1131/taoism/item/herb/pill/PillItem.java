//ADDED:新建类BasePillItem

package io.github.shm1131.taoism.item.herb.pill;

import io.github.shm1131.taoism.client.ClientInputTracker;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.PropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class PillItem extends Item {

    public PillItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
        ItemStack stack,
        TooltipContext context,
        TooltipDisplay display,
        Consumer<Component> builder,
        TooltipFlag flag
    ) {
        HerbProperties props =
            PropertiesHelper.getProperties(stack);

        builder.accept(Component.translatable("tooltip.taoism.pill.flavor",
            Component.translatable(props.flavor().translationKey())));
        builder.accept(Component.translatable("tooltip.taoism.pill.nature",
            Component.translatable(props.nature().translationKey())));

        if (ClientInputTracker.isShiftDown()) {
            builder.accept(CommonComponents.EMPTY);
            builder.accept(Component.translatable("tooltip.taoism.pill.toxicity",
                    String.format("%.0f", props.toxicity()))
                .withStyle(ChatFormatting.RED));
            builder.accept(Component.translatable("tooltip.taoism.pill.potency",
                    String.format("%.1f", props.potency()))
                .withStyle(ChatFormatting.GOLD));
        } else {
            builder.accept(Component.translatable("tooltip.taoism.pill.hold_shift")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
