package hmysjiang.potioncapsule.compat.jei;

import java.util.Arrays;
import java.util.stream.Collectors;

import hmysjiang.potioncapsule.client.gui.ScreenGelatinExtractor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinFormer;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.recipe.RecipeGelatinExtractor;
import hmysjiang.potioncapsule.recipe.RecipeGelatinFormer;
import hmysjiang.potioncapsule.recipe.RecipeSpecialRepair;
import hmysjiang.potioncapsule.utils.Defaults;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class PCJeiPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return Defaults.modPrefix.apply("jeiplugin");
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new GelExtractorJei.Category(helper),
										 new GelFormerJei.Category(helper),
										 new WartDropJei.Category(helper),
										 new SpecialRepairJei.Category(helper));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.GELATIN_EXTRACTOR), GelExtractorJei.UID);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.GELATIN_FORMER), GelFormerJei.UID);
		registration.addRecipeCatalyst(new ItemStack(Items.NETHER_WART), WartDropJei.UID);
		for (Block block: BlockTags.ANVIL.getAllElements())
			registration.addRecipeCatalyst(new ItemStack(block), SpecialRepairJei.UID);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ItemJeiDescription.registerInfo(registration);
		
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(
			recipe -> { return recipe != null && recipe instanceof RecipeGelatinExtractor; }
		).collect(Collectors.toList()), GelExtractorJei.UID);
		
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(
			recipe -> { return recipe != null && recipe instanceof RecipeGelatinFormer; }
		).collect(Collectors.toList()), GelFormerJei.UID);
		
		Minecraft.getInstance().world.getRecipeManager().getRecipe(Defaults.modPrefix.apply("wart_dust")).ifPresent(r -> {
			registration.addRecipes(Arrays.asList(r), WartDropJei.UID);
		});
		
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(
			recipe -> { return recipe != null && recipe instanceof RecipeSpecialRepair; }
		).collect(Collectors.toList()), SpecialRepairJei.UID);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ScreenGelatinExtractor.class, 76, 35, 24, 16, GelExtractorJei.UID);
		registration.addRecipeClickArea(ScreenGelatinFormer.class, 76, 35, 24, 16, GelFormerJei.UID);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ContainerGelatinExtractor.class, GelExtractorJei.UID, 0, 2, 2, 36);
	}
	
}
