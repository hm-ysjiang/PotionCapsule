package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.effects.EffectNightVisionNF;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEffects {
	
	@SubscribeEvent
	public static void onEffectRegister(RegistryEvent.Register<Effect> event) {
		event.getRegistry().registerAll(EffectNightVisionNF.INSTANCE);
	}
	
}
