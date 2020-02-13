package hmysjiang.potioncapsule.compat.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import hmysjiang.potioncapsule.client.gui.ScreenGelatinExtractor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinFormer;
import hmysjiang.potioncapsule.client.gui.ScreenSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.compat.jei.recipe.RecipeSpecialRepairJei;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.recipe.RecipeFoodRestoration;
import hmysjiang.potioncapsule.recipe.RecipeGelatinExtractor;
import hmysjiang.potioncapsule.recipe.RecipeGelatinFormer;
import hmysjiang.potioncapsule.recipe.RecipeSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.utils.Defaults;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
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
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.CAPSULE_REPAIR), SpecialRepairJei.UID);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.AUTO_BREWER), VanillaRecipeCategoryUid.BREWING);
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
		
		List<RecipeSpecialRepairJei> splitRecipes = new ArrayList<>();
		for (IRecipe<?> r: Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(
				recipe -> { return recipe != null && recipe instanceof RecipeSpecialCapsuleRepairer; }
					).collect(Collectors.toList())) {
			RecipeSpecialCapsuleRepairer recipe = (RecipeSpecialCapsuleRepairer) r;
			for (Entry<Ingredient, Integer> entry: recipe.getMaterials().entrySet()) {
				splitRecipes.add(new RecipeSpecialRepairJei(ItemSpecialCapsule.getCapsuleInstance(recipe.getCapsuleType()), entry.getKey(), entry.getValue()));
			}
		}
		for (IRecipe<?> r: Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter(
				recipe -> { return recipe != null && recipe instanceof RecipeFoodRestoration; }
					).collect(Collectors.toList())) {
			RecipeFoodRestoration recipe = (RecipeFoodRestoration) r;
			for (Entry<Integer, List<Item>> entry: recipe.getVal2Items().entrySet()) {
				splitRecipes.add(new RecipeSpecialRepairJei(ItemSpecialCapsule.getCapsuleInstance(EnumSpecialType.LIE_OF_CAKE), Ingredient.fromItems(entry.getValue().toArray(new Item[0])), entry.getKey()));
			}
		}
		registration.addRecipes(splitRecipes, SpecialRepairJei.UID);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(ScreenGelatinExtractor.class, 76, 35, 24, 16, GelExtractorJei.UID);
		registration.addRecipeClickArea(ScreenGelatinFormer.class, 76, 35, 24, 16, GelFormerJei.UID);
		registration.addRecipeClickArea(ScreenSpecialCapsuleRepairer.class, 107, 37, 24, 19, SpecialRepairJei.UID);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(ContainerGelatinExtractor.class, GelExtractorJei.UID, 0, 2, 2, 36);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		IModPlugin.super.registerItemSubtypes(registration);
		registration.registerSubtypeInterpreter(ModItems.CAPSULE, stack -> {
			List<String> str = PotionUtils.getEffectsFromStack(stack).stream().map(effect -> effect.getEffectName() + " " + effect.getAmplifier()).collect(Collectors.toList());
			str.sort(null);
			return str.toString();
		});
		registration.registerSubtypeInterpreter(ModItems.CAPSULE_INSTANT, stack -> {
			List<String> str = PotionUtils.getEffectsFromStack(stack).stream().map(effect -> effect.getEffectName() + " " + effect.getAmplifier()).collect(Collectors.toList());
			str.sort(null);
			return str.toString();
		});
	}
	
}
