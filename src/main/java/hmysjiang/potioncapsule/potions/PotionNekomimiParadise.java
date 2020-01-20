package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.potions.effects.EffectNekomimiParadise;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionNekomimiParadise extends Potion {
	
	private static final Potion PARA = new PotionNekomimiParadise("potioncapsule_nekomimi", new EffectInstance(EffectNekomimiParadise.INSTANCE, 180 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_nekomimi"));	
	private static final Potion PARA_LONG = new PotionNekomimiParadise("potioncapsule_nekomimi", new EffectInstance(EffectNekomimiParadise.INSTANCE, 480 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_nekomimi_long"));	
	
	protected PotionNekomimiParadise(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(PARA,
						PARA_LONG);
	}
	
	public static void registerRecipes() {
		// PotionHelper.register2StageRecipe(ModItems.BLOCK_FIERY_LILY, PARA, PARA_LONG);
	}
	
}
