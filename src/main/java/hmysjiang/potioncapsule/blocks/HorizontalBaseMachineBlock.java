package hmysjiang.potioncapsule.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class HorizontalBaseMachineBlock extends BaseMachineBlock {
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

	public HorizontalBaseMachineBlock(ITileCreator<?> tileCreatorIn) {
		super(tileCreatorIn);

		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING);
	}
	
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}
	
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
	}

}
