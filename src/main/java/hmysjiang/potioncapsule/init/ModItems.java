package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static final Item CAPSULE = new ItemCapsule().setRegistryName(Defaults.modPrefix.apply(ItemRegs.CAPSULE));
	public static final Item PENDANT = new ItemCapsulePendant().setRegistryName(Defaults.modPrefix.apply(ItemRegs.PENDANT));
	
	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
		PotionCapsule.Logger.info("Items Registering");
		IForgeRegistry<Item> reg = event.getRegistry();
		reg.registerAll(CAPSULE,
						PENDANT);
	}
	
}
