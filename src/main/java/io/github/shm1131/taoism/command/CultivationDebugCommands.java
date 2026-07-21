package io.github.shm1131.taoism.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.shm1131.taoism.data.cultivation.CultivationAttachment;
import io.github.shm1131.taoism.data.cultivation.CultivationHelper;
import io.github.shm1131.taoism.data.cultivation.ICultivationData;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class CultivationDebugCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tao")
                // 增加灵力
                .then(Commands.literal("addsp")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    CultivationHelper.addSp(player, IntegerArgumentType.getInteger(ctx, "amount"));
                                    return 1;
                                })))
                // 查看状态
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ICultivationData data = player.getData(CultivationAttachment.TAOISM_DATA);
                            if (data == null) data = ICultivationData.EMPTY;

                            ICultivationData finalData = data;
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                    String.format("境界:%s | 灵力:%d/%d | 可突破:%b",
                                            finalData.getRealm().getDisplayName(),
                                            finalData.getCurrentSp(),
                                            finalData.getRealm().getMaxSpiritualPower(),
                                            CultivationHelper.canLevelUp(player))), false);
                            return 1;
                        }))
                // 突破
                .then(Commands.literal("levelup")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            boolean success = CultivationHelper.levelUp(player);
                            ctx.getSource().sendSuccess(() -> Component.literal(success ? "突破成功" : "无法突破"), false);
                            return success ? 1 : 0;
                        }))
        );
    }
}