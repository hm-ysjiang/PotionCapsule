package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionWithering extends Potion {
	
	public static final Potion WITHER = new PotionThorn("potioncapsule_withering", new EffectInstance(Effects.WITHER, 20 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_withering"));	
	private static final Potion WITHER_STRONG = new PotionThorn("potioncapsule_withering", new EffectInstance(Effects.WITHER, 15 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_withering_strong"));	
	private static final Potion WITHER_VERY_STRONG = new PotionThorn("potioncapsule_withering", new EffectInstance(Effects.WITHER, 10 * 20, 2)).setRegistryName(Defaults.modPrefix.apply("potion_withering_very_strong"));
	public static final Potion WITHER_LONG = new PotionThorn("potioncapsule_withering", new EffectInstance(Effects.WITHER, 30 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_withering_long"));	
	private static final Potion WITHER_STRONG_LONG = new PotionThorn("potioncapsule_withering", new EffectInstance(Effects.WITHER, 23 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_withering_strong_long"));	
	
	protected PotionWithering(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(WITHER,
						WITHER_STRONG,
						WITHER_VERY_STRONG,
						WITHER_LONG,
						WITHER_STRONG_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register5StageRecipe(ModItems.WITHER_FRAG, WITHER, WITHER_STRONG, WITHER_VERY_STRONG, WITHER_LONG, WITHER_STRONG_LONG);
	}

}
