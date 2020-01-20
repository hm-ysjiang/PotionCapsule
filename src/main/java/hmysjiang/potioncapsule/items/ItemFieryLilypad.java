package hmysjiang.potioncapsule.items;

import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LilyPadItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.world.World;

public class ItemFieryLilypad extends LilyPadItem {
	
	public ItemFieryLilypad() {
		super(ModBlocks.FIERY_LILY, Defaults.itemProp.get());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		BlockRayTraceResult raytraceresult = (BlockRayTraceResult) rayTrace(worldIn, playerIn, FluidMode.SOURCE_ONLY);

		if (raytraceresult == null) {
			return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
		}
		else {
			BlockPos lava = raytraceresult.getPos();

			if (!worldIn.isBlockModifiable(playerIn, lava) || !playerIn.canPlayerEdit(lava.offset(raytraceresult.getFace()), raytraceresult.getFace(), itemstack)) {
				return new ActionResult<ItemStack>(ActionResultType.FAIL, itemstack);
			}

			BlockPos lily = lava.up();
			BlockState lavastate = worldIn.getBlockState(lava);

			if (lavastate.getMaterial() == Material.LAVA && ((Integer)lavastate.get(FlowingFluidBlock.LEVEL)).intValue() == 0 && worldIn.isAirBlock(lily)) {
				worldIn.setBlockState(lily, ModBlocks.FIERY_LILY.getDefaultState(), 11);

				if (!playerIn.isCreative()) {
					itemstack.shrink(1);
				}

				playerIn.addStat(Stats.ITEM_USED.get(this));
				worldIn.playSound(playerIn, lava, SoundEvents.BLOCK_LILY_PAD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
			}
			return new ActionResult<ItemStack>(ActionResultType.FAIL, itemstack);
		}
	}
	
}
