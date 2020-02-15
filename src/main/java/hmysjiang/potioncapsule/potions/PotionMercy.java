package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.potions.effects.EffectMercy;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionMercy extends Potion {
	
	private static final Potion MERCY = new PotionMercy("potioncapsule_mercy", new EffectInstance(EffectMercy.INSTANCE, 30 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_mercy"));	
	private static final Potion MERCY_LONG = new PotionMercy("potioncapsule_mercy", new EffectInstance(EffectMercy.INSTANCE, 80 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_mercy_long"));	
	
	protected PotionMercy(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(MERCY,
						MERCY_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register2StageRecipe(PotionWithering.WITHER, Items.FERMENTED_SPIDER_EYE, MERCY, MERCY_LONG);
		PotionHelper.registerRecipe(PotionWithering.WITHER_LONG, Items.FERMENTED_SPIDER_EYE, MERCY_LONG);
	}
	
}
