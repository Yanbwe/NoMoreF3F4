package org.yanbwe.nomoref3f4;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.Arrays;
import java.util.List;

// 服务器配置类，管理旁观权限和禁用维度设置
@Mod.EventBusSubscriber(modid = Nomoref3f4.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 允许使用旁观模式的管理员列表
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_ADMINS;
    
    // 禁用旁观模式的维度列表
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLED_DIMENSIONS;
    
    // 信用点上限
    public static ForgeConfigSpec.IntValue CREDIT_MAX;
    
    // 信用点下限
    public static ForgeConfigSpec.IntValue CREDIT_MIN;
    
    // 切换到旁观模式的扣除量
    public static ForgeConfigSpec.IntValue SPECTATOR_COST;
    
    // 非旁观模式下每tick恢复量
    public static ForgeConfigSpec.IntValue TICK_RECOVERY;
    
    // 旁观模式下每tick消耗量
    public static ForgeConfigSpec.IntValue TICK_CONSUMPTION;
    

    
    static {
        BUILDER.push("服务器配置");
        
        ALLOWED_ADMINS = BUILDER
            .comment("允许使用旁观模式的玩家名称列表")
            .translation("config.nomoref3f4.allowed_admins")
            .defineList("allowed_admins", Arrays.asList("Player", "Dev", "Admin", "Yanbwe"), obj -> obj instanceof String);
            
        DISABLED_DIMENSIONS = BUILDER
            .comment("禁用旁观模式的维度列表，格式如：[\"minecraft:the_nether\", \"minecraft:the_end\"]")
            .translation("config.nomoref3f4.disabled_dimensions")
            .defineList("disabled_dimensions", Arrays.asList("twilightforest:twilight_forest"), obj -> obj instanceof String);
            
        BUILDER.pop();
        
        BUILDER.push("信用点配置");
        
        CREDIT_MAX = BUILDER
            .comment("信用点上限值")
            .translation("config.nomoref3f4.credit_max")
            .defineInRange("credit_max", 12000, 1, Integer.MAX_VALUE);
            
        CREDIT_MIN = BUILDER
            .comment("信用点下限值（可为负数）")
            .translation("config.nomoref3f4.credit_min")
            .defineInRange("credit_min", -2400, Integer.MIN_VALUE, -1);
            
        SPECTATOR_COST = BUILDER
            .comment("切换到旁观模式时扣除的信用点数量")
            .translation("config.nomoref3f4.spectator_cost")
            .defineInRange("spectator_cost", 2400, 0, Integer.MAX_VALUE);
            
        TICK_RECOVERY = BUILDER
            .comment("非旁观模式下每tick恢复的信用点数量")
            .translation("config.nomoref3f4.tick_recovery")
            .defineInRange("tick_recovery", 1, 0, Integer.MAX_VALUE);
            
        TICK_CONSUMPTION = BUILDER
            .comment("旁观模式下每tick消耗的信用点数量（应为负数）")
            .translation("config.nomoref3f4.tick_consumption")
            .defineInRange("tick_consumption", -1, Integer.MIN_VALUE, -1);
            
        BUILDER.pop();
    }
    
    static final ForgeConfigSpec SPEC = BUILDER.build();

    /**
     * 检查玩家是否是管理员
     * @param playerName 玩家名称
     * @return 是否是管理员
     */
    public static boolean isPlayerAdmin(String playerName) {
        List<? extends String> admins = ALLOWED_ADMINS.get();
        return admins != null && admins.contains(playerName);
    }

    /**
     * 手动触发配置重载
     */
    public static void onReload() {
        System.out.println("[INFO] 手动触发配置重载");
        System.out.println("[INFO] 重载后的配置值 - allowed_admins: " + ALLOWED_ADMINS.get());
        System.out.println("[INFO] 重载后的配置值 - credit_max: " + CREDIT_MAX.get());
        System.out.println("[INFO] 重载后的配置值 - credit_min: " + CREDIT_MIN.get());
        System.out.println("[INFO] 重载后的配置值 - spectator_cost: " + SPECTATOR_COST.get());
        System.out.println("[INFO] 重载后的配置值 - tick_recovery: " + TICK_RECOVERY.get());
        System.out.println("[INFO] 重载后的配置值 - tick_consumption: " + TICK_CONSUMPTION.get());
    }
    
    /**
     * 强制保存配置到磁盘
     */
    public static void forceSaveConfig() {
        System.out.println("[INFO] 强制保存配置到磁盘");
        try {
            // 通过访问配置值来触发保存
            ALLOWED_ADMINS.save();
            DISABLED_DIMENSIONS.save();
            CREDIT_MAX.save();
            CREDIT_MIN.save();
            SPECTATOR_COST.save();
            TICK_RECOVERY.save();
            TICK_CONSUMPTION.save();
            System.out.println("[INFO] 配置保存完成");
        } catch (Exception e) {
            System.err.println("[ERROR] 配置保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getModId().equals(Nomoref3f4.MODID)) {
            System.out.println("[DEBUG] 配置加载事件触发: " + event.getConfig().getFileName());
            System.out.println("[DEBUG] 当前配置值 - allowed_admins: " + ALLOWED_ADMINS.get());
        }
    }
}