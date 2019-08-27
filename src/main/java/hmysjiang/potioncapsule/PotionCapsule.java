package hmysjiang.potioncapsule;

import org.apache.logging.log4j.LogManager;

import hmysjiang.potioncapsule.configs.ConfigManager;
import hmysjiang.potioncapsule.proxy.ClientProxy;
import hmysjiang.potioncapsule.proxy.ISidedProxy;
import hmysjiang.potioncapsule.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Reference.MOD_ID)
public class PotionCapsule {
	
	public static PotionCapsule INSTANCE_REF;
	public PotionCapsule() {
		INSTANCE_REF = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientRegistries);
		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(Type.SERVER, ConfigManager.SConfig);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ConfigManager.CConfig);

		ConfigManager.loadServerConfigFromPath(FMLPaths.CONFIGDIR.get().resolve("potioncapsule-server.toml").toString());
		ConfigManager.loadClientConfigFromPath(FMLPaths.CONFIGDIR.get().resolve("potioncapsule-client.toml").toString());
		
		Logger.info("Hello Minecraft!");
	}
	
	public static ISidedProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	
	public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.POTION);
		}
	};
	
	private void onCommonSetup(final FMLCommonSetupEvent event) {
		proxy.init();
	}
	
	private void onClientRegistries(final FMLClientSetupEvent event) {
		
	}
	
	public static class Logger {
		private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

		public static void info(Object obj) {
			LOGGER.info(obj == null ? "null" : obj.toString());
		}
		
		public static void warn(Object obj) {
			LOGGER.warn(obj == null ? "null" : obj.toString());
		}
		
		public static void error(Object obj) {
			LOGGER.error(obj == null ? "null" : obj.toString());
		}
	}
	
}
