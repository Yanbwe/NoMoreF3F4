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
    
    // 命令相关消息
    public static final String COMMAND_CHECK_OWN_CREDIT = PREFIX + "command_check_own_credit";
    public static final String COMMAND_PLAYER_ONLY = PREFIX + "command_player_only";
    public static final String COMMAND_CHECK_PLAYER_CREDIT = PREFIX + "command_check_player_credit";
    public static final String COMMAND_PLAYER_NOT_FOUND = PREFIX + "command_player_not_found";
    public static final String COMMAND_ADMIN_REQUIRED = PREFIX + "command_admin_required";
    public static final String COMMAND_SET_CREDIT_SUCCESS = PREFIX + "command_set_credit_success";
    public static final String COMMAND_SET_CREDIT_NOTIFY = PREFIX + "command_set_credit_notify";
    public static final String COMMAND_EXECUTE_FAILED = PREFIX + "command_execute_failed";
    public static final String COMMAND_ADD_CREDIT_SUCCESS = PREFIX + "command_add_credit_success";
    public static final String COMMAND_ADD_CREDIT_NOTIFY = PREFIX + "command_add_credit_notify";
    public static final String COMMAND_REMOVE_CREDIT_SUCCESS = PREFIX + "command_remove_credit_success";
    public static final String COMMAND_REMOVE_CREDIT_NOTIFY = PREFIX + "command_remove_credit_notify";
    public static final String COMMAND_RELOAD_SUCCESS = PREFIX + "command_reload_success";
    public static final String COMMAND_RELOAD_NOTIFY = PREFIX + "command_reload_notify";
    public static final String COMMAND_RELOAD_FAILED = PREFIX + "command_reload_failed";
    
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
    
    /**
     * 获取带颜色的本地化消息
     * @param key 消息键名
     * @param color 颜色代码
     * @return 带颜色的本地化组件
     */
    public static MutableComponent getColoredMessage(String key, String color) {
        return Component.translatable(key).withStyle(style -> style.withColor(net.minecraft.network.chat.TextColor.parseColor(color)));
    }
    
    /**
     * 获取带参数和颜色的本地化消息
     * @param key 消息键名
     * @param color 颜色代码
     * @param args 参数
     * @return 带颜色和参数的本地化组件
     */
    public static MutableComponent getColoredMessage(String key, String color, Object... args) {
        return Component.translatable(key, args).withStyle(style -> style.withColor(net.minecraft.network.chat.TextColor.parseColor(color)));
    }
}