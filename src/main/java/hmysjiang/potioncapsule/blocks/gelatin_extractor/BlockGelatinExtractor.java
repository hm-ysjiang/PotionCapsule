package hmysjiang.potioncapsule.blocks.gelatin_extractor;

import hmysjiang.potioncapsule.blocks.HorizontalBaseMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BlockGelatinExtractor extends HorizontalBaseMachineBlock {

	public BlockGelatinExtractor() {
		super(TileEntityGelatinExtractor::new);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) worldIn.getTileEntity(pos), pos);
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityGelatinExtractor) {
				ItemStackHandler inventory = ((TileEntityGelatinExtractor) tileentity).getHandler();
				for (int i = 0 ; i<inventory.getSlots() ; i++) {
					InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), inventory.getStackInSlot(i));
				}
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

}
