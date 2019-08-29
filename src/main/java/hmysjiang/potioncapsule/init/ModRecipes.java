package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.recipe.RecipeCapsuleAttachment;
import hmysjiang.potioncapsule.recipe.RecipeCapsuleCombinationOrClear;
import hmysjiang.potioncapsule.recipe.RecipeGelatinExtractor;
import hmysjiang.potioncapsule.recipe.RecipeGelatinFormer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModRecipes {
	
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		PotionCapsule.Logger.info("Recipes Registering");
		event.getRegistry().registerAll(RecipeCapsuleAttachment.SERIALIZER,
										RecipeCapsuleCombinationOrClear.SERIALIZER
										,
										RecipeGelatinExtractor.SERIALIZER,
										RecipeGelatinFormer.SERIALIZER);
	}
	
}
