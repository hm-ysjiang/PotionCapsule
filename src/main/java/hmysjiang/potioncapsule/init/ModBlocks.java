package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.Reference.BlockRegs;
import hmysjiang.potioncapsule.blocks.gelatin_extractor.BlockGelatinExtractor;
import hmysjiang.potioncapsule.blocks.gelatin_former.BlockGelatinFormer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static final Block GELATIN_EXTRACTOR = new BlockGelatinExtractor().setRegistryName(Defaults.modPrefix.apply(BlockRegs.GELATIN_EXTRACTOR));
	public static final Block GELATIN_FORMER = new BlockGelatinFormer().setRegistryName(Defaults.modPrefix.apply(BlockRegs.GELATIN_FORMER));
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
		PotionCapsule.Logger.info("Blocks Registering");
		event.getRegistry().registerAll(GELATIN_EXTRACTOR,
										GELATIN_FORMER);
	}
	
}
