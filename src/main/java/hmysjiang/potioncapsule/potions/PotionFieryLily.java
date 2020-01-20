package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.potions.effects.EffectLilypad;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionFieryLily extends Potion {
	
	private static final Potion FLILY = new PotionFieryLily("potioncapsule_fierylily", new EffectInstance(EffectLilypad.INSTANCE, 180 * 20), new EffectInstance(Effects.FIRE_RESISTANCE, 180 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_fierylily"));	
	private static final Potion FLILY_LONG = new PotionFieryLily("potioncapsule_fierylily", new EffectInstance(EffectLilypad.INSTANCE, 400 * 20), new EffectInstance(Effects.FIRE_RESISTANCE, 400 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_fierylily_long"));	
	
	protected PotionFieryLily(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(FLILY,
						FLILY_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register2StageRecipe(ModItems.BLOCK_FIERY_LILY, FLILY, FLILY_LONG);
	}
	
}
