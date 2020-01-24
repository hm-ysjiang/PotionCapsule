package hmysjiang.potioncapsule.world;

import java.util.Random;

import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.WorldHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeatureFieryLilypad extends Feature<NoFeatureConfig> {
	
	@SuppressWarnings("unchecked")
	public static final Feature<NoFeatureConfig> INSTANCE = (Feature<NoFeatureConfig>) new FeatureFieryLilypad().setRegistryName(Defaults.modPrefix.apply("feature_fiery_lilypad"));

	public FeatureFieryLilypad() {
		super(NoFeatureConfig::deserialize);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
			BlockPos pos, NoFeatureConfig config) {
		if (CommonConfigs.worldgen_fieryLilySpawnRate.get() == 0)
			return true;
		if (rand.nextInt(CommonConfigs.worldgen_fieryLilySpawnRate.get()) == 0) {
			BlockPos surface = WorldHelper.getFirstAirBlock(worldIn, pos, 35).down();
			if (worldIn.getBlockState(surface).getBlock() == Blocks.LAVA) {
				worldIn.setBlockState(surface.up(1), Blocks.NETHER_BRICK_FENCE.getDefaultState(), 2);
				worldIn.setBlockState(surface.up(2), Blocks.NETHER_BRICK_FENCE.getDefaultState(), 2);
				worldIn.setBlockState(surface.up(3), Blocks.GLOWSTONE.getDefaultState(), 2);
				
				for (int i = -1 ; i<=1 ; i++) {
					for (int j = -1 ; j<=1 ; j++) {
						if (i == 0 && j == 0) {
							worldIn.setBlockState(surface, Blocks.MAGMA_BLOCK.getDefaultState(), 2);
							continue;
						}
						worldIn.setBlockState(surface.add(i, 0, j), Blocks.NETHER_BRICKS.getDefaultState(), 2);
					}
				}
				for (int i = -3 ; i<=3 ; i++) {
					for (int j = -3 ; j<=3 ; j++) {
						if (i >= -1 && i <= 1 && j >= -1 && j <= 1)
							continue;
						if (rand.nextInt(MathHelper.abs(i) + MathHelper.abs(j)) == 0)
							if (ModBlocks.FIERY_LILY.isValidPosition(ModBlocks.FIERY_LILY.getDefaultState(), worldIn, surface.add(i, 0, j).up()))
								worldIn.setBlockState(surface.add(i, 0, j).up(), ModBlocks.FIERY_LILY.getDefaultState(), 2);
					}
				}
			}
		}
		return true;
	}

}
