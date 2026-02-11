package org.yanbwe.nomoref3f4;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.MinecraftServer;

/**
 * 游戏事件监听器
 * 处理玩家游戏模式变更和tick事件
 */
public class EventHandler {
    
    /**
     * 监听玩家游戏模式变更事件
     * 控制旁观模式的使用权限
     */
    @SubscribeEvent
    public static void onPlayerChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        // 只处理服务器端事件
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        
        GameType newGameMode = event.getNewGameMode();
        GameType oldGameMode = event.getCurrentGameMode();
        
        // 尝试进入旁观模式时的检查
        if (newGameMode == GameType.SPECTATOR) {
            String playerName = player.getName().getString();
            
            // 检查当前维度是否被禁用
            String dimension = player.level().dimension().location().toString();
            if (Config.DISABLED_DIMENSIONS.get().contains(dimension)) {
                event.setCanceled(true);
                player.sendSystemMessage(LangManager.getMessage(LangManager.DIMENSION_DISABLED));
                // 发送当前信用点提示
                sendCreditInfoMessage(player);
                return;
            }
            
            // 检查信用值是否足够（这才是真正的限制条件）
            int currentCredit = CreditManager.getCredit(player);
            
            if (currentCredit < 0) {
                event.setCanceled(true);
                player.sendSystemMessage(LangManager.getMessage(LangManager.NO_PERMISSION));
                
                // 向所有在线玩家广播制裁消息
                broadcastSanctionMessage(player);
                // 发送当前信用点提示
                sendCreditInfoMessage(player);
                return;
            }
            
            // 发送当前信用点提示
            sendCreditInfoMessage(player);
        }
        // 从旁观模式退出时扣除信用值
        else if (oldGameMode == GameType.SPECTATOR && newGameMode != GameType.SPECTATOR) {
            int cost = Config.SPECTATOR_COST.get();
            CreditManager.modifyCredit(player, -cost);
            player.sendSystemMessage(LangManager.getMessage(LangManager.EXIT_SPECTATOR, cost));
            // 发送当前信用点提示
            sendCreditInfoMessage(player);
        }
        // 其他模式切换也发送信用点提示
        else {
            sendCreditInfoMessage(player);
        }
    }
    
    /**
     * 监听玩家tick事件
     * 实时更新信用值
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // 只处理服务器端结束阶段的事件
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }
        
        // 只处理服务器玩家
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }
        
        // 根据游戏模式更新信用值
        if (player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
            // 旁观状态下每tick消耗配置的信用值，可为负数
            CreditManager.modifyCreditInSpectator(player, Config.TICK_CONSUMPTION.get());
        } else {
            // 非旁观状态下每tick恢复配置的信用值
            CreditManager.modifyCredit(player, Config.TICK_RECOVERY.get());
        }
    }
    
    /**
     * 向所有在线玩家广播制裁消息
     * 使用通用方法确保1.19和1.20版本兼容
     * @param player 被制裁的玩家
     */
    private static void broadcastSanctionMessage(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            String playerName = player.getName().getString();
            Component message = LangManager.getMessage(LangManager.SANCTION_BROADCAST, playerName);
            // 使用遍历方法替代broadcastSystemMessage，确保版本兼容性
            for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
                onlinePlayer.sendSystemMessage(message);
            }
        }
    }
    
    /**
     * 向玩家发送当前信用点信息
     * @param player 目标玩家
     */
    private static void sendCreditInfoMessage(ServerPlayer player) {
        int currentCredit = CreditManager.getCredit(player);
        // 使用灰色字体，减少视觉干扰
        Component message = Component.literal("§7[信用点] " + currentCredit);
        player.sendSystemMessage(message);
    }
}