package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.world.FeatureFieryLilypad;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.MOD, modid=Reference.MOD_ID)
public class ModFeatures {
	
	@SubscribeEvent
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event) {
		PotionCapsule.Logger.info("Features Registering");
		event.getRegistry().registerAll(FeatureFieryLilypad.INSTANCE);
	}
	
	public static void addFeatureToBiomes() {
		Biomes.NETHER.addFeature(Decoration.TOP_LAYER_MODIFICATION, Biome.createDecoratedFeature(FeatureFieryLilypad.INSTANCE, new NoFeatureConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
	}
	
}
