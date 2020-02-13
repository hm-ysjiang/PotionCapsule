package hmysjiang.potioncapsule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;

import hmysjiang.potioncapsule.compat.curio.DummyCurioProxy;
import hmysjiang.potioncapsule.compat.curio.ICurioProxy;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.configs.ConfigManager;
import hmysjiang.potioncapsule.init.ModFeatures;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.potions.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.proxy.ClientProxy;
import hmysjiang.potioncapsule.proxy.ISidedProxy;
import hmysjiang.potioncapsule.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
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
	
	public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.CAPSULE);
		}
	};
	
	
	public static ICurioProxy curioProxy;
	public static Optional<? extends ModContainer> curioOpt;
	private void onCommonSetup(final FMLCommonSetupEvent event) {
		proxy.init();
		
		curioOpt = ModList.get().getModContainerById("curios");
		try {
			curioProxy = ICurioProxy.isCurioLoaded() ? 
							Class.forName("hmysjiang.potioncapsule.compat.curio.CurioProxy").asSubclass(ICurioProxy.class).newInstance() :
							new DummyCurioProxy();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Logger.error("Wierd thing happened while trying to instantiate proxy for Curios, report this to the issue tracker.");
			e.printStackTrace();
			curioProxy = new DummyCurioProxy();
		}
		
		ModFeatures.addFeatureToBiomes();
	}
	
	private void onEnqueueIMC(final InterModEnqueueEvent event) {
		curioProxy.enqueueIMC();
	}
	
	@SubscribeEvent
	public void onAttachCaps(AttachCapabilitiesEvent<ItemStack> event) {
		curioProxy.attachCaps(event);
	}
	
	private static Set<EffectInstance> queryedEffects;
	private static List<EffectInstance> effectsPool;
	@SuppressWarnings("deprecation")
	private static void queryEffects() {
		PotionCapsule.Logger.info("Start querying effect instances from existing potion");
		queryedEffects = new TreeSet<>(ItemCapsule.EFFECT_CMP);
		effectsPool = new ArrayList<>();
		for (Potion potion: Registry.POTION) {
			for (EffectInstance effect: potion.getEffects()) {
				EffectInstance toadd = new EffectInstance(effect);
				if (!toadd.getPotion().isInstant())
					toadd.duration = CommonConfigs.capsule_capacity.get();
				if (toadd.getPotion() == Effects.NIGHT_VISION && CommonConfigs.misc_replaceNvWithNvnf.get())
					toadd = new EffectInstance(EffectNightVisionNF.INSTANCE, toadd.getDuration(), toadd.getAmplifier(), toadd.isAmbient(), toadd.doesShowParticles(), toadd.isShowIcon());
				if (queryedEffects.add(toadd)) {
					if (toadd.getPotion().getEffectType() != EffectType.HARMFUL)
						effectsPool.add(toadd);
					PotionCapsule.Logger.info(toadd.getAmplifier() > 0 ? new TranslationTextComponent(toadd.getEffectName()).getFormattedText() + " x " + (toadd.getAmplifier() + 1) : new TranslationTextComponent(toadd.getEffectName()).getFormattedText());
				}
			}
		}
		PotionCapsule.Logger.info("Effect Querying complete");
	}
	public static Set<EffectInstance> getQueryedEffects() {
		if (queryedEffects == null)
			queryEffects();
		return queryedEffects;
	}
	public static List<EffectInstance> getEffectsPool() {
		if (effectsPool == null)
			queryEffects();
		return effectsPool;
	}
	
	public static class Logger {
		private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
		private static String flat(Object obj) { return obj == null ? "null" : obj.toString(); }
		public static void info(Object... objs) { Arrays.stream(objs).map(Logger::flat).forEach(LOGGER::info); }
		public static void warn(Object... objs) { Arrays.stream(objs).map(Logger::flat).forEach(LOGGER::warn); }
		public static void error(Object... objs) { Arrays.stream(objs).map(Logger::flat).forEach(LOGGER::error); }
	}
	
}
