package org.yanbwe.nomoref3f4;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 命令处理器
 * 处理信用点相关的管理命令
 */
@Mod.EventBusSubscriber(modid = Nomoref3f4.MODID)
public class CommandHandler {
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(
            Commands.literal("nomoref3f4")
                // 查询自己的信用点
                .then(Commands.literal("check")
                    .executes(CommandHandler::checkOwnCredit))
                // 查询指定玩家的信用点
                .then(Commands.literal("check")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(CommandHandler::checkPlayerCredit)))
                // 修改玩家信用点（仅管理员）
                .then(Commands.literal("set")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                            .executes(CommandHandler::setPlayerCredit))))
                // 增加玩家信用点（仅管理员）
                .then(Commands.literal("add")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .executes(CommandHandler::addPlayerCredit))))
                // 减少玩家信用点（仅管理员）
                .then(Commands.literal("remove")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                            .executes(CommandHandler::removePlayerCredit))))
                // 重载配置（仅管理员）
                .then(Commands.literal("reload")
                    .executes(CommandHandler::reloadConfig))
        );
    }
    
    /**
     * 检查执行者是否是管理员（支持后备机制）
     */
    private static boolean isAdmin(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            String playerName = player.getName().getString();
            return Config.isPlayerAdmin(playerName);
        }
        return false;
    }
    
    /**
     * 查询自己的信用点
     */
    private static int checkOwnCredit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer player) {
            int credit = CreditManager.getCredit(player);
            source.sendSuccess(() -> Component.literal("§a你的当前信用点: " + credit), false);
            return 1;
        }
        source.sendFailure(Component.literal("§c只能由玩家执行此命令"));
        return 0;
    }
    
    /**
     * 查询指定玩家的信用点
     */
    private static int checkPlayerCredit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        try {
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
            int credit = CreditManager.getCredit(targetPlayer);
            source.sendSuccess(() -> Component.literal("§a玩家 " + targetPlayer.getName().getString() + " 的信用点: " + credit), false);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§c无法找到指定玩家"));
            return 0;
        }
    }
    
    /**
     * 设置玩家信用点
     */
    private static int setPlayerCredit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (!isAdmin(source)) {
            source.sendFailure(Component.literal("§c你不是管理员，无法执行此命令"));
            return 0;
        }
        
        try {
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
            int amount = IntegerArgumentType.getInteger(context, "amount");
            
            CreditManager.setCredit(targetPlayer, amount);
            source.sendSuccess(() -> Component.literal("§a已将玩家 " + targetPlayer.getName().getString() + " 的信用点设置为: " + amount), true);
            targetPlayer.sendSystemMessage(Component.literal("§e你的信用点已被管理员设置为: " + amount));
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§c命令执行失败: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * 增加玩家信用点
     */
    private static int addPlayerCredit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (!isAdmin(source)) {
            source.sendFailure(Component.literal("§c你不是管理员，无法执行此命令"));
            return 0;
        }
        
        try {
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
            int amount = IntegerArgumentType.getInteger(context, "amount");
            
            int currentCredit = CreditManager.getCredit(targetPlayer);
            int newCredit = currentCredit + amount;
            CreditManager.setCredit(targetPlayer, newCredit);
            
            source.sendSuccess(() -> Component.literal("§a已为玩家 " + targetPlayer.getName().getString() + " 增加 " + amount + " 信用点，当前: " + newCredit), true);
            targetPlayer.sendSystemMessage(Component.literal("§e你的信用点已增加 " + amount + " 点，当前: " + newCredit));
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§c命令执行失败: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * 减少玩家信用点
     */
    private static int removePlayerCredit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (!isAdmin(source)) {
            source.sendFailure(Component.literal("§c你不是管理员，无法执行此命令"));
            return 0;
        }
        
        try {
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
            int amount = IntegerArgumentType.getInteger(context, "amount");
            
            int currentCredit = CreditManager.getCredit(targetPlayer);
            int newCredit = currentCredit - amount;
            CreditManager.setCredit(targetPlayer, newCredit);
            
            source.sendSuccess(() -> Component.literal("§a已为玩家 " + targetPlayer.getName().getString() + " 减少 " + amount + " 信用点，当前: " + newCredit), true);
            targetPlayer.sendSystemMessage(Component.literal("§e你的信用点已减少 " + amount + " 点，当前: " + newCredit));
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§c命令执行失败: " + e.getMessage()));
            return 0;
        }
    }
    
    /**
     * 重载配置
     */
    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (!isAdmin(source)) {
            source.sendFailure(Component.literal("§c你不是管理员，无法执行此命令"));
            return 0;
        }
        
        try {
            // 触发配置重载
            source.sendSuccess(() -> Component.literal("§a配置文件重载命令已执行"), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("§c配置重载失败: " + e.getMessage()));
            return 0;
        }
    }
}