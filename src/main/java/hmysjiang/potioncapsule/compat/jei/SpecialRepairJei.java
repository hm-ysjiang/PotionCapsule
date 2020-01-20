package hmysjiang.potioncapsule.compat.jei;

import java.util.Arrays;
import java.util.stream.Collectors;

import hmysjiang.potioncapsule.recipe.RecipeSpecialRepair;
import hmysjiang.potioncapsule.utils.Defaults;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class SpecialRepairJei {

	public static final ResourceLocation UID = Defaults.modPrefix.apply("jei_specialrepair");
	
	public static class Category implements IRecipeCategory<RecipeSpecialRepair> {
		protected final IDrawable background;
		protected final IDrawable icon;
		
		public Category(IGuiHelper helper) {
			background = helper.createDrawable(Defaults.modPrefix.apply("textures/gui/jei/jei_background.png"), 0, 173, 112, 42);
			icon = helper.createDrawableIngredient(new ItemStack(Blocks.ANVIL));
		}

		@Override
		public ResourceLocation getUid() {
			return UID;
		}

		@Override
		public Class<? extends RecipeSpecialRepair> getRecipeClass() {
			return RecipeSpecialRepair.class;
		}

		@Override
		public String getTitle() {
			return new TranslationTextComponent("potioncapsule.jei_category.special_repair").getFormattedText();
		}

		@Override
		public IDrawable getBackground() {
			return background;
		}

		@Override
		public IDrawable getIcon() {
			return icon;
		}

		@Override
		public void setIngredients(RecipeSpecialRepair recipe, IIngredients ingredients) {
			ingredients.setInputLists(VanillaTypes.ITEM,
					Arrays.asList(Arrays.asList(recipe.getToRepair()), ItemTags.getCollection().get(recipe.getRepairs())
							.getAllElements().stream().map(item -> new ItemStack(item)).collect(Collectors.toList())));
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRepaired());
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, RecipeSpecialRepair recipe, IIngredients ingredients) {
			IGuiItemStackGroup group = recipeLayout.getItemStacks();
			group.init(0, true, 2, 21);
			group.init(1, true, 37, 21);
			group.init(2, false, 92, 21);
			group.set(ingredients);
		}
		
	}
	
}
