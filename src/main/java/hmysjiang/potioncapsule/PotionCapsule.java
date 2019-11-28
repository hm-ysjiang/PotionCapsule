package hmysjiang.potioncapsule;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

import hmysjiang.potioncapsule.compact.curio.DummyCurioProxy;
import hmysjiang.potioncapsule.compact.curio.ICurioProxy;
import hmysjiang.potioncapsule.configs.ConfigManager;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.proxy.ClientProxy;
import hmysjiang.potioncapsule.proxy.ISidedProxy;
import hmysjiang.potioncapsule.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Reference.MOD_ID)
public class PotionCapsule {
	
	public static PotionCapsule INSTANCE_REF;
	public PotionCapsule() {
		INSTANCE_REF = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEnqueueIMC);
		// FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientRegistries);
		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(Type.COMMON, ConfigManager.SConfig);
		ConfigManager.loadCommonConfigFromPath(FMLPaths.CONFIGDIR.get().resolve("potioncapsule-common.toml").toString());
        ItemSpecialCapsule.init();
        
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->{
        	ModLoadingContext.get().registerConfig(Type.CLIENT, ConfigManager.CConfig);
        	ConfigManager.loadClientConfigFromPath(FMLPaths.CONFIGDIR.get().resolve("potioncapsule-client.toml").toString());
		});
		
		Logger.info("Hello Minecraft!");
	}
	
	public static ISidedProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	public static ICurioProxy curioProxy;
	public static Optional<? extends ModContainer> curioOpt;
	
	public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.CAPSULE);
		}
	};
	
	private void onCommonSetup(final FMLCommonSetupEvent event) {
		proxy.init();
		
		curioOpt = ModList.get().getModContainerById("curios");
		try {
			curioProxy = ICurioProxy.isCurioLoaded() ? 
							Class.forName("hmysjiang.potioncapsule.compact.curio.CurioProxy").asSubclass(ICurioProxy.class).newInstance() :
							new DummyCurioProxy();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Logger.error("Wierd thing happened while trying to instantiate proxy for Curios, report this to the issue tracker.");
			e.printStackTrace();
			curioProxy = new DummyCurioProxy();
		}
	}
	
	private void onEnqueueIMC(final InterModEnqueueEvent event) {
		curioProxy.enqueueIMC();
	}
	
	@SubscribeEvent
	public void onAttachCaps(AttachCapabilitiesEvent<ItemStack> event) {
		curioProxy.attachCaps(event);
	}
	
	public static class Logger {
		private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
		private static String flat(Object obj) { return obj == null ? "null" : obj.toString(); }
		public static void info(Object... objs) { Arrays.stream(objs).map(Logger::flat).collect(Collectors.toList()).forEach(LOGGER::info); }
		public static void warn(Object... objs) { Arrays.stream(objs).map(Logger::flat).forEach(LOGGER::warn); }
		public static void error(Object... objs) { Arrays.stream(objs).map(Logger::flat).forEach(LOGGER::error); }
	}
	
}
