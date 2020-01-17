package hmysjiang.potioncapsule.compact.jei;

import java.util.Arrays;

import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.recipe.RecipeGelatinFormer;
import hmysjiang.potioncapsule.utils.Defaults;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GelFormerJei {
	
	public static final ResourceLocation UID = Defaults.modPrefix.apply("jei_former");
	
	public static class Category implements IRecipeCategory<RecipeGelatinFormer> {
		protected final IDrawableAnimated arrow;
		protected final IDrawable background;
		protected final IDrawable icon;
		
		public Category(IGuiHelper helper) {
			arrow = helper.drawableBuilder(Defaults.modPrefix.apply("textures/gui/container/gelatin_extractor.png"), 176, 17, 24, 17)
					.buildAnimated(100, StartDirection.LEFT, false);
			background = helper.createDrawable(Defaults.modPrefix.apply("textures/gui/jei/jei_background.png"), 0, 51, 100, 50);
			icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.GELATIN_FORMER));
		}

		@Override
		public ResourceLocation getUid() {
			return UID;
		}

		@Override
		public Class<? extends RecipeGelatinFormer> getRecipeClass() {
			return RecipeGelatinFormer.class;
		}

		@Override
		public String getTitle() {
			return new TranslationTextComponent("potioncapsule.jei_category.gel_former").getFormattedText();
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
		public void draw(RecipeGelatinFormer recipe, double mouseX, double mouseY) {
			arrow.draw(49, 17);
		}

		@Override
		public void setIngredients(RecipeGelatinFormer recipe, IIngredients ingredients) {
			ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(new ItemStack(ModItems.GELATIN_POWDER, recipe.getGelatinCount()),
																   recipe.getCatalyst()));
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, RecipeGelatinFormer recipe, IIngredients ingredients) {
			IGuiItemStackGroup group = recipeLayout.getItemStacks();
			group.init(0, true, 7, 16);
			group.init(1, true, 29, 16);
			group.init(2, false, 75, 16);
			group.set(ingredients);
		}
		
	}
}
