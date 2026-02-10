package org.yanbwe.nomoref3f4;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 本地化消息管理工具类
 * 统一管理模组中的所有本地化文本
 */
public class LangManager {
    
    // 消息键名常量
    private static final String PREFIX = "message.nomoref3f4.";
    
    public static final String NO_PERMISSION = PREFIX + "no_permission";
    public static final String DIMENSION_DISABLED = PREFIX + "dimension_disabled";
    public static final String INSUFFICIENT_CREDIT = PREFIX + "insufficient_credit";
    public static final String EXIT_SPECTATOR = PREFIX + "exit_spectator";
    public static final String CONFIG_RELOADED = PREFIX + "config_reloaded";
    public static final String MOD_INITIALIZED = PREFIX + "mod_initialized";
    public static final String SERVER_STARTED = PREFIX + "server_started";
    public static final String SANCTION_BROADCAST = PREFIX + "sanction_broadcast";
    
    /**
     * 获取本地化组件消息
     * @param key 消息键名
     * @return 本地化组件
     */
    public static MutableComponent getMessage(String key) {
        return Component.translatable(key);
    }
    
    /**
     * 获取带参数的本地化组件消息
     * @param key 消息键名
     * @param args 参数
     * @return 本地化组件
     */
    public static MutableComponent getMessage(String key, Object... args) {
        return Component.translatable(key, args);
    }
}