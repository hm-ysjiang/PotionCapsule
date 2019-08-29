package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.Reference.BlockRegs;
import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static final Item CAPSULE = new ItemCapsule().setRegistryName(Defaults.modPrefix.apply(ItemRegs.CAPSULE));
	public static final Item PENDANT = new ItemCapsulePendant().setRegistryName(Defaults.modPrefix.apply(ItemRegs.PENDANT));

	public static final Item BLOCK_GELATIN_EXTRACTOR = new BlockItem(ModBlocks.GELATIN_EXTRACTOR, Defaults.itemProp.get()).setRegistryName(BlockRegs.GELATIN_EXTRACTOR);
	public static final Item BLOCK_GELATIN_FORMER = new BlockItem(ModBlocks.GELATIN_FORMER, Defaults.itemProp.get()).setRegistryName(BlockRegs.GELATIN_FORMER);

	public static final Item GELATIN_POWDER = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.GELATIN_POWDER));
	public static final Item WART_DUST = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.WART_DUST));
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		PotionCapsule.Logger.info("Items Registering");
		IForgeRegistry<Item> reg = event.getRegistry();
		reg.registerAll(CAPSULE,
						PENDANT
						,
						BLOCK_GELATIN_EXTRACTOR,
						BLOCK_GELATIN_FORMER
						,
						GELATIN_POWDER,
						WART_DUST);
	}
	
}
