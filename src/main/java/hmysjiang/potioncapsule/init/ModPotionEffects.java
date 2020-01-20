package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.potions.PotionLilypad;
import hmysjiang.potioncapsule.potions.PotionThorn;
import hmysjiang.potioncapsule.potions.effects.EffectLilypad;
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
										EffectLilypad.INSTANCE);
	}
	
	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<Potion> event) {
		PotionCapsule.Logger.info("Potions Registering");
		IForgeRegistry<Potion> reg = event.getRegistry();
		PotionThorn.register(reg);
		PotionLilypad.register(reg);
		
		PotionThorn.registerRecipes();
		PotionLilypad.registerRecipes();
	}
	
}
