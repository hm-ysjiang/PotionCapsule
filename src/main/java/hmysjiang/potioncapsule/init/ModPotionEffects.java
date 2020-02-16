package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.potions.PotionBloodlust;
import hmysjiang.potioncapsule.potions.PotionFieryLily;
import hmysjiang.potioncapsule.potions.PotionLilypad;
import hmysjiang.potioncapsule.potions.PotionMercy;
import hmysjiang.potioncapsule.potions.PotionNekomimiParadise;
import hmysjiang.potioncapsule.potions.PotionSpikyObi;
import hmysjiang.potioncapsule.potions.PotionThorn;
import hmysjiang.potioncapsule.potions.PotionWithering;
import hmysjiang.potioncapsule.potions.effects.EffectBloodlust;
import hmysjiang.potioncapsule.potions.effects.EffectLilypad;
import hmysjiang.potioncapsule.potions.effects.EffectMercy;
import hmysjiang.potioncapsule.potions.effects.EffectNekomimiParadise;
import hmysjiang.potioncapsule.potions.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.potions.effects.EffectThorn;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModPotionEffects {
	
	@SubscribeEvent
	public static void onEffectRegister(RegistryEvent.Register<Effect> event) {
		PotionCapsule.Logger.info("Effects Registering");
		event.getRegistry().registerAll(EffectNightVisionNF.INSTANCE,
										EffectThorn.INSTANCE,
										EffectLilypad.INSTANCE,
										EffectNekomimiParadise.INSTANCE,
										EffectMercy.INSTANCE,
										EffectBloodlust.INSTANCE);
	}
	
	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<Potion> event) {
		PotionCapsule.Logger.info("Potions Registering");
		IForgeRegistry<Potion> reg = event.getRegistry();
		PotionThorn.register(reg);
		PotionSpikyObi.register(reg);
		PotionLilypad.register(reg);
		PotionFieryLily.register(reg);
		PotionNekomimiParadise.register(reg);
		PotionWithering.register(reg);
		PotionMercy.register(reg);
		PotionBloodlust.register(reg);
		
		PotionThorn.registerRecipes();
		PotionSpikyObi.registerRecipes();
		PotionLilypad.registerRecipes();
		PotionFieryLily.registerRecipes();
		PotionNekomimiParadise.registerRecipes();
		PotionWithering.registerRecipes();
		PotionMercy.registerRecipes();
		PotionBloodlust.registerRecipes();
	}
	
}
