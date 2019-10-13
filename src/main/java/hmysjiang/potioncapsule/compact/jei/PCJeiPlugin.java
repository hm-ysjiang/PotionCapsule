package hmysjiang.potioncapsule.compact.jei;

import hmysjiang.potioncapsule.utils.Defaults;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;

//@JeiPlugin
public class PCJeiPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return Defaults.modPrefix.apply("jeiplugin");
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		// TODO Auto-generated method stub
		IModPlugin.super.registerCategories(registration);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		// TODO Auto-generated method stub
		IModPlugin.super.registerRecipeCatalysts(registration);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		// TODO Auto-generated method stub
		IModPlugin.super.registerGuiHandlers(registration);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ItemJeiDescription.registerInfo(registration);
		
		
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		// TODO Auto-generated method stub
		IModPlugin.super.registerRecipeTransferHandlers(registration);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		// TODO Auto-generated method stub
		IModPlugin.super.registerItemSubtypes(registration);
	}
	
}
