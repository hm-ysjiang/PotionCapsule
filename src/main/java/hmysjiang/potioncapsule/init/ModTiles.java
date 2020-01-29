package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.blocks.gelatin_extractor.TileEntityGelatinExtractor;
import hmysjiang.potioncapsule.blocks.gelatin_former.TileEntityGelatinFormer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModTiles {
	
	@SubscribeEvent
	public static void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
		PotionCapsule.Logger.info("TileEntities Registering");
		event.getRegistry().registerAll(TileEntityGelatinExtractor.TYPE,
										TileEntityGelatinFormer.TYPE,
										TileEntityAutoBrewer.TYPE);
	}
	
}
