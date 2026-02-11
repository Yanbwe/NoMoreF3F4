package org.yanbwe.nomoref3f4;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.List;

// 旁观时间管理大师模组主类
@Mod(Nomoref3f4.MODID)
public class Nomoref3f4 {

    // 定义模组ID供各处引用
    public static final String MODID = "nomoref3f4";
    // 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();

    public Nomoref3f4() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册通用设置方法用于模组加载
        modEventBus.addListener(this::commonSetup);

        // 注册服务器和其他游戏事件
        MinecraftForge.EVENT_BUS.register(this);
        
        // 注册事件监听器
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        // 注册通用配置（支持单机和服务器）
        System.out.println("[DEBUG] 开始注册COMMON配置");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        System.out.println("[DEBUG] COMMON配置注册完成");
        
        LOGGER.info("模组构造函数执行完毕，配置已注册");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 通用设置代码
        LOGGER.info(LangManager.MOD_INITIALIZED);
    }

    // 服务器启动事件
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时执行的操作
        LOGGER.info(LangManager.SERVER_STARTED);
        
        // 通过访问配置值来触发配置文件生成
        try {
            List<? extends String> admins = Config.ALLOWED_ADMINS.get();
            LOGGER.info("访问配置值触发文件生成 - allowed_admins: " + admins);
        } catch (Exception e) {
            LOGGER.error("访问配置值失败: " + e.getMessage());
        }
        
        LOGGER.info("服务器启动完成");
    }
    
    // 配置加载事件
    @SubscribeEvent
    public void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(MODID)) {
            LOGGER.info("配置加载事件触发: " + event.getConfig().getFileName());
            LOGGER.info("当前配置值 - allowed_admins: " + Config.ALLOWED_ADMINS.get());
        }
    }
    
    // 配置重载事件
    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(MODID)) {
            LOGGER.info(LangManager.CONFIG_RELOADED);
            LOGGER.info("配置重载后值 - allowed_admins: " + Config.ALLOWED_ADMINS.get());
        }
    }
}