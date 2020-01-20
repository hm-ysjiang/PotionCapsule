package hmysjiang.potioncapsule.blocks.fiery_lily;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockFieryLilypad extends LilyPadBlock {

	public BlockFieryLilypad() {
		super(Properties.create(Material.PLANTS).hardnessAndResistance(0F).sound(SoundType.PLANT));
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getBlock() == Blocks.LAVA;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (fromPos.up().equals(pos))
			if (!isValidPosition(state, worldIn, pos))
				worldIn.destroyBlock(pos, true);
	}

}
