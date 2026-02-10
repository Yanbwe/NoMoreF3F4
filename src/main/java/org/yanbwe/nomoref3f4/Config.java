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
    

    
    static {
        BUILDER.push("服务器配置");
        
        ALLOWED_ADMINS = BUILDER
            .comment("允许使用旁观模式的玩家名称列表")
            .translation("config.nomoref3f4.allowed_admins")
            .defineList("allowed_admins", Arrays.asList("Player", "Dev", "Admin", "Yanbwe"), obj -> obj instanceof String);
            
        DISABLED_DIMENSIONS = BUILDER
            .comment("禁用旁观模式的维度列表，格式如：[\"minecraft:the_nether\", \"minecraft:the_end\"]")
            .translation("config.nomoref3f4.disabled_dimensions")
            .defineList("disabled_dimensions", Arrays.asList(), obj -> obj instanceof String);
            
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

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getModId().equals(Nomoref3f4.MODID)) {
            System.out.println("[DEBUG] 配置加载事件触发: " + event.getConfig().getFileName());
            System.out.println("[DEBUG] 当前配置值 - allowed_admins: " + ALLOWED_ADMINS.get());
        }
    }
}