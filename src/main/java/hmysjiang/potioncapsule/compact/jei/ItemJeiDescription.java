package hmysjiang.potioncapsule.compact.jei;

import hmysjiang.potioncapsule.compact.curio.ICurioProxy;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.init.ModBlocks;
import hmysjiang.potioncapsule.init.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;

public class ItemJeiDescription {
	
	public static void registerInfo(IRecipeRegistration registration) {
		registration.addIngredientInfo(new ItemStack(ModItems.APPLE_JELLY), VanillaTypes.ITEM, "potioncapsule.jei_desc.jelly");
		registration.addIngredientInfo(new ItemStack(ModItems.CAPSULE), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule");
		registration.addIngredientInfo(new ItemStack(ModItems.CAPSULE_INSTANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule_instant");
		// registration.addIngredientInfo(new ItemStack(ModItems.CAPSULE_SPECIAL), VanillaTypes.ITEM, "potioncapsule.jei_desc.capsule_special");
		registration.addIngredientInfo(new ItemStack(ModItems.GELATIN_POWDER), VanillaTypes.ITEM, "potioncapsule.jei_desc.gelatin_powder");
		
		if (ICurioProxy.isCurioLoaded()) {
			registration.addIngredientInfo(new ItemStack(ModItems.PENDANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.pendant",
																							   "potioncapsule.jei_desc.pendant_curio");
		}
		else {
			registration.addIngredientInfo(new ItemStack(ModItems.PENDANT), VanillaTypes.ITEM, "potioncapsule.jei_desc.pendant");
		}
		
		if (CommonConfigs.recipe_enableWartDust.get()) {
			registration.addIngredientInfo(new ItemStack(ModItems.WART_DUST), VanillaTypes.ITEM, "potioncapsule.jei_desc.wart_dust",
																								 "potioncapsule.jei_desc.wart_dust_craft");
		}
		else {
			registration.addIngredientInfo(new ItemStack(ModItems.WART_DUST), VanillaTypes.ITEM, "potioncapsule.jei_desc.wart_dust");
		}
		registration.addIngredientInfo(new ItemStack(ModItems.CATALYST), VanillaTypes.ITEM, "potioncapsule.jei_desc.catalyst");
		registration.addIngredientInfo(new ItemStack(ModItems.CREATIVE_CATALYST), VanillaTypes.ITEM, "potioncapsule.jei_desc.c_catalyst");
		
		
		registration.addIngredientInfo(new ItemStack(ModBlocks.GELATIN_EXTRACTOR), VanillaTypes.ITEM, "potioncapsule.jei_desc.gel_extractor");
		registration.addIngredientInfo(new ItemStack(ModBlocks.GELATIN_FORMER), VanillaTypes.ITEM, "potioncapsule.jei_desc.gel_former");
	}
	
}
