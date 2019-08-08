package hmysjiang.potioncapsule;

import org.apache.logging.log4j.LogManager;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class PotionCapsule {
	
	public static PotionCapsule INSTANCE_REF;
	public PotionCapsule() {
		INSTANCE_REF = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientRegistries);
		MinecraftForge.EVENT_BUS.register(this);
		
		Logger.info("Hello Minecraft!");
	}
	
	public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.POTION);
		}
	};
	
	private void onCommonSetup(final FMLCommonSetupEvent event) {
		
	}
	
	private void onClientRegistries(final FMLClientSetupEvent event) {
		
	}
	
	public static class Logger {
		private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

		public static void info(Object obj) {
			LOGGER.info(obj.toString());
		}
		
		public static void warn(Object obj) {
			LOGGER.warn(obj.toString());
		}
		
		public static void error(Object obj) {
			LOGGER.error(obj.toString());
		}
	}
	
}
