package hmysjiang.potioncapsule.compact.jei;

import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.recipe.RecipeGelatinExtractor;
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

public class GelExtractorJei {
	
	public static final ResourceLocation UID = Defaults.modPrefix.apply("jei_extractor");
	
	public static class Category implements IRecipeCategory<RecipeGelatinExtractor> {
		protected final IDrawableAnimated arrow;
		protected final IDrawable background;
		protected final IDrawable icon;
		
		public Category(IGuiHelper helper) {
			arrow = helper.drawableBuilder(Defaults.modPrefix.apply("textures/gui/container/gelatin_extractor.png"), 176, 17, 24, 17)
					.buildAnimated(100, StartDirection.LEFT, false);
			background = helper.createDrawable(Defaults.modPrefix.apply("textures/gui/jei/jei_background.png"), 0, 0, 100, 50);
			icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.GELATIN_EXTRACTOR));
		}

		@Override
		public ResourceLocation getUid() {
			return UID;
		}

		@Override
		public Class<? extends RecipeGelatinExtractor> getRecipeClass() {
			return RecipeGelatinExtractor.class;
		}

		@Override
		public String getTitle() {
			return new TranslationTextComponent("potioncapsule.jei_category.gel_extractor").getFormattedText();
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
		public void draw(RecipeGelatinExtractor recipe, double mouseX, double mouseY) {
			arrow.draw(38, 17);
		}

		@Override
		public void setIngredients(RecipeGelatinExtractor recipe, IIngredients ingredients) {
			ingredients.setInput(VanillaTypes.ITEM, new ItemStack(recipe.getInput()));
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, RecipeGelatinExtractor recipe, IIngredients ingredients) {
			IGuiItemStackGroup group = recipeLayout.getItemStacks();
			group.init(0, true, 19, 17);
			group.init(1, false, 65, 17);
			group.set(ingredients);
		}
		
	}
	
}
