package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.potions.effects.EffectBloodlust;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionBloodlust extends Potion {
	
	private static final Potion LUST = new PotionBloodlust("potioncapsule_bloodlust", new EffectInstance(EffectBloodlust.INSTANCE, 180 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_bloodlust"));	
	private static final Potion LUST_STRONG = new PotionBloodlust("potioncapsule_bloodlust", new EffectInstance(EffectBloodlust.INSTANCE, 80 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_bloodlust_strong"));	
	private static final Potion LUST_VERY_STRONG = new PotionBloodlust("potioncapsule_bloodlust", new EffectInstance(EffectBloodlust.INSTANCE, 40 * 20, 2)).setRegistryName(Defaults.modPrefix.apply("potion_bloodlust_very_strong"));
	private static final Potion LUST_LONG = new PotionBloodlust("potioncapsule_bloodlust", new EffectInstance(EffectBloodlust.INSTANCE, 300 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_bloodlust_long"));	
	private static final Potion LUST_STRONG_LONG = new PotionBloodlust("potioncapsule_bloodlust", new EffectInstance(EffectBloodlust.INSTANCE, 140 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_bloodlust_strong_long"));	
	
	protected PotionBloodlust(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(LUST,
						LUST_STRONG,
						LUST_VERY_STRONG,
						LUST_LONG,
						LUST_STRONG_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register5StageRecipe(ModItems.FANG, LUST, LUST_STRONG, LUST_VERY_STRONG, LUST_LONG, LUST_STRONG_LONG);
	}
	
}
