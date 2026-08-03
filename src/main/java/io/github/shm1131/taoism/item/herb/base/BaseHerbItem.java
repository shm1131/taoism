package io.github.shm1131.taoism.item.herb.base;

import io.github.shm1131.taoism.client.ClientInputTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;

import net.minecraft.world.item.Item;


public class BaseHerbItem extends Item implements IHerbBase {
  private final HerbProperties properties;

  public BaseHerbItem(Properties itemProps, HerbProperties props) {
    super(itemProps);
    this.properties = props;
  }

  @Override
  public HerbProperties getHerbProperties() {
    return properties;
  }

    @Override
    public void appendHoverText(
        ItemStack stack,
        TooltipContext context,
        TooltipDisplay display,
        Consumer<Component> builder,
        TooltipFlag flag
    ) {
        HerbProperties props = this.getHerbProperties();

        builder.accept(Component.translatable("tooltip.taoism.pill.flavor",
            Component.translatable(props.flavor().translationKey())));
        builder.accept(Component.translatable("tooltip.taoism.pill.nature",
            Component.translatable(props.nature().translationKey())));

        if (ClientInputTracker.isShiftDown()) {
            builder.accept(CommonComponents.EMPTY);
            builder.accept(Component.translatable("tooltip.taoism.pill.toxicity",
                    String.format("%.2f", props.toxicity()))
                .withStyle(ChatFormatting.RED));
            builder.accept(Component.translatable("tooltip.taoism.pill.potency",
                    String.format("%.2f", props.potency()))
                .withStyle(ChatFormatting.GOLD));
        } else {
            builder.accept(Component.translatable("tooltip.taoism.pill.hold_shift")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
