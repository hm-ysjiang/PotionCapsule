package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.container.ContainerPendant;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModContainers {
	
	@SubscribeEvent
	public static void onContainerTypeRegister(RegistryEvent.Register<ContainerType<?>> event) {
		PotionCapsule.Logger.info("Containers Registering");
		event.getRegistry().registerAll(ContainerPendant.TYPE,
										ContainerGelatinExtractor.TYPE);
	}
	
}
