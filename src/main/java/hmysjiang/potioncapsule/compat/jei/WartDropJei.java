package hmysjiang.potioncapsule.compat.jei;

import java.util.Arrays;
import java.util.List;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.recipe.RecipeWartDust;
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
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class WartDropJei {

	public static final ResourceLocation UID = Defaults.modPrefix.apply("jei_wartdrop");
	
	public static class Category implements IRecipeCategory<RecipeWartDust> {
		protected final IDrawable background;
		protected final IDrawable icon;
		protected final IDrawable pick;
		
		public Category(IGuiHelper helper) {
			background = helper.createDrawable(Defaults.modPrefix.apply("textures/gui/jei/jei_background.png"), 0, 102, 100, 70);
			icon = helper.createDrawableIngredient(new ItemStack(ModItems.WART_DUST));
			pick = helper.createDrawableIngredient(new ItemStack(Items.WOODEN_PICKAXE));
		}

		@Override
		public ResourceLocation getUid() {
			return UID;
		}

		@Override
		public Class<? extends RecipeWartDust> getRecipeClass() {
			return RecipeWartDust.class;
		}

		@Override
		public String getTitle() {
			return new TranslationTextComponent("potioncapsule.jei_category.wart_drop").getFormattedText();
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
		public void draw(RecipeWartDust recipe, double mouseX, double mouseY) {
			pick.draw(28, 27);
		}

		@Override
		public void setIngredients(RecipeWartDust recipe, IIngredients ingredients) {
			ingredients.setInput(VanillaTypes.ITEM, new ItemStack(Blocks.NETHER_WART));
			ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ModItems.WART_DUST));
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, RecipeWartDust recipe, IIngredients ingredients) {
			IGuiItemStackGroup group = recipeLayout.getItemStacks();
			group.init(0, true, 28, 5);
			group.init(1, false, 54, 27);
			group.set(ingredients);
		}
		
		@Override
		public List<String> getTooltipStrings(RecipeWartDust recipe, double mouseX, double mouseY) {
			if (mouseX >= 28 && mouseX < 28 + 16 && 
					mouseY >= 27 && mouseY < 27 + 16)
				return Arrays.asList(new TranslationTextComponent("potioncapsule.jei_addi.wart_drop", "%").getFormattedText());
			return IRecipeCategory.super.getTooltipStrings(recipe, mouseX, mouseY);
		}
		
	}
	
}
