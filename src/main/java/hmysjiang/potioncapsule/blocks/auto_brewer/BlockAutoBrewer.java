package hmysjiang.potioncapsule.blocks.auto_brewer;

import java.util.Random;

import hmysjiang.potioncapsule.blocks.HorizontalBaseMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BlockAutoBrewer extends HorizontalBaseMachineBlock {

	public BlockAutoBrewer() {
		super(TileEntityAutoBrewer::new);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (handIn == Hand.MAIN_HAND && player.getHeldItemMainhand().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getFace());
		}
		else if (!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) worldIn.getTileEntity(pos), pos);
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityAutoBrewer) {
				ItemStackHandler inventory = ((TileEntityAutoBrewer) tileentity).getInventory();
				for (int i = 0 ; i<inventory.getSlots() ; i++) {
					InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), inventory.getStackInSlot(i));
				}
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		double d0 = (double) ((float) pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
		double d1 = (double) ((float) pos.getY() + 0.7F + rand.nextFloat() * 0.3F);
		double d2 = (double) ((float) pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
		worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
	
}
