package org.yanbwe.nomoref3f4;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/**
 * 信用管理系统
 * 管理玩家的旁观信用值，控制旁观模式使用权限
 */
public class CreditManager {
    
    // 信用值存储键名
    private static final String CREDIT_KEY = "nomoref3f4.credit";
    
    /**
     * 获取玩家当前信用值
     * @param player 服务器玩家对象
     * @return 当前信用值，若无记录则返回配置的最大值
     */
    public static int getCredit(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(CREDIT_KEY)) {
            // 新玩家默认满信用点
            return Config.CREDIT_MAX.get();
        }
        return persistentData.getInt(CREDIT_KEY);
    }
    
    /**
     * 设置玩家信用值
     * @param player 服务器玩家对象
     * @param value 信用值，自动限制在合理范围内
     */
    public static void setCredit(ServerPlayer player, int value) {
        CompoundTag persistentData = player.getPersistentData();
        // 限制信用值范围：配置的下限到上限
        int clampedValue = Math.max(Math.min(value, Config.CREDIT_MAX.get()), Config.CREDIT_MIN.get());
        persistentData.putInt(CREDIT_KEY, clampedValue);
    }
    
    /**
     * 在旁观模式下设置信用值（允许负数，最低-1200）
     * @param player 服务器玩家对象
     * @param value 信用值
     */
    public static void setCreditInSpectator(ServerPlayer player, int value) {
        CompoundTag persistentData = player.getPersistentData();
        // 旁观模式下信用值可为负数，最低为配置的下限值
        int clampedValue = Math.max(Math.min(value, Config.CREDIT_MAX.get()), Config.CREDIT_MIN.get());
        persistentData.putInt(CREDIT_KEY, clampedValue);
    }
    
    /**
     * 修改玩家信用值
     * @param player 服务器玩家对象
     * @param delta 变化量（正数增加，负数减少）
     */
    public static void modifyCredit(ServerPlayer player, int delta) {
        int currentCredit = getCredit(player);
        int newCredit = currentCredit + delta;
        setCredit(player, newCredit);
    }
    
    /**
     * 在旁观模式下修改信用值（有下限保护）
     * @param player 服务器玩家对象
     * @param delta 变化量
     */
    public static void modifyCreditInSpectator(ServerPlayer player, int delta) {
        int currentCredit = getCredit(player);
        int newCredit = currentCredit + delta;
        setCreditInSpectator(player, newCredit);
    }
}