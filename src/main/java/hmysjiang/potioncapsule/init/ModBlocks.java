package hmysjiang.potioncapsule.init;

import java.util.Random;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.Reference.BlockRegs;
import hmysjiang.potioncapsule.blocks.gelatin_extractor.BlockGelatinExtractor;
import hmysjiang.potioncapsule.blocks.gelatin_former.BlockGelatinFormer;
import hmysjiang.potioncapsule.blocks.tiny_cactus.BlockTinyCactus;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static final Block GELATIN_EXTRACTOR = new BlockGelatinExtractor().setRegistryName(Defaults.modPrefix.apply(BlockRegs.GELATIN_EXTRACTOR));
	public static final Block GELATIN_FORMER = new BlockGelatinFormer().setRegistryName(Defaults.modPrefix.apply(BlockRegs.GELATIN_FORMER));
	public static final Block LIGHT = new Block(Block.Properties.create(new Material(MaterialColor.YELLOW, false, false, false, false, true, false, true, PushReaction.DESTROY))
																.doesNotBlockMovement()
																.lightValue(15)
																.noDrops()
																.sound(SoundType.CLOTH)) {
		@Override public boolean canSpawnInBlock() { return true; }
		@Override public VoxelShape getShape(BlockState p1, IBlockReader p2, BlockPos p3, ISelectionContext p4) { return VoxelShapes.empty(); }
		@Override public boolean allowsMovement(BlockState p1, IBlockReader p2, BlockPos p3, PathType p4) { return true; }
		@Override @OnlyIn(Dist.CLIENT) 
		public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
			if (rand.nextInt(3) == 0) {
				double d0 = (double)((float)pos.getX() + 0.3F + rand.nextFloat() * 0.4F);
				double d1 = (double)((float)pos.getY() + 0.6F);
				double d2 = (double)((float)pos.getZ() + 0.3F + rand.nextFloat() * 0.4F);
				worldIn.addParticle(ParticleTypes.END_ROD, d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		};
	}.setRegistryName(Defaults.modPrefix.apply(BlockRegs.LIGHT));
	public static final Block TINY_CACTI = new BlockTinyCactus().setRegistryName(Defaults.modPrefix.apply(BlockRegs.TINY_CACTI));
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
		PotionCapsule.Logger.info("Blocks Registering");
		event.getRegistry().registerAll(GELATIN_EXTRACTOR,
										GELATIN_FORMER
										,
										LIGHT,
										TINY_CACTI);
	}
	
}
