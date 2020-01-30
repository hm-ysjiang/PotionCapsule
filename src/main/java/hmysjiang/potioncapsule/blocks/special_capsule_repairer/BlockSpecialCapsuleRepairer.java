package hmysjiang.potioncapsule.blocks.special_capsule_repairer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import hmysjiang.potioncapsule.blocks.HorizontalBaseMachineBlock;
import hmysjiang.potioncapsule.container.ContainerSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.recipe.RecipeSpecialCapsuleRepairer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSpecialCapsuleRepairer extends HorizontalBaseMachineBlock {
	
	private static final Map<EnumSpecialType, List<RecipeSpecialCapsuleRepairer>> recipes = new HashMap<EnumSpecialType, List<RecipeSpecialCapsuleRepairer>>();
	public static void addRecipe(EnumSpecialType type, RecipeSpecialCapsuleRepairer recipe) {
		if (recipes.get(type) == null)
			recipes.put(type, Arrays.asList(recipe));
		else
			recipes.get(type).add(recipe);
	}
	@Nullable
	public List<RecipeSpecialCapsuleRepairer> getRecipeOfType(EnumSpecialType type){
		return recipes.get(type);
	}
	
	public BlockSpecialCapsuleRepairer() {
		super(null);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
				@Override
				public Container createMenu(int winID, PlayerInventory playerInventory, PlayerEntity player) {
					return new ContainerSpecialCapsuleRepairer(winID, playerInventory, pos);
				}
				@Override
				public ITextComponent getDisplayName() {
					return new TranslationTextComponent(BlockSpecialCapsuleRepairer.this.getTranslationKey());
				}
			}, pos);
		}
		return true;
	}

}
