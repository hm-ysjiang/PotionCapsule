package hmysjiang.potioncapsule.blocks.gelatin_former;

import hmysjiang.potioncapsule.blocks.HorizontalBaseMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockGelatinFormer extends HorizontalBaseMachineBlock {

	public BlockGelatinFormer() {
		super(TileEntityGelatinFormer::new);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) worldIn.getTileEntity(pos), pos);
		}
		return true;
	}

}
