package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.potions.effects.EffectThorn;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionSpikyObi extends Potion {
	
	private static final Potion OBI = new PotionSpikyObi("potioncapsule_spikyobi", new EffectInstance(EffectThorn.INSTANCE, 90 * 20), new EffectInstance(Effects.RESISTANCE, 90 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_spikyobi"));	
	private static final Potion OBI_STRONG = new PotionSpikyObi("potioncapsule_spikyobi", new EffectInstance(EffectThorn.INSTANCE, 60 * 20, 1), new EffectInstance(Effects.RESISTANCE, 60 * 20, 2)).setRegistryName(Defaults.modPrefix.apply("potion_spikyobi_strong"));	
	private static final Potion OBI_VERY_STRONG = new PotionSpikyObi("potioncapsule_spikyobi", new EffectInstance(EffectThorn.INSTANCE, 30 * 20, 2), new EffectInstance(Effects.RESISTANCE, 30 * 20, 3)).setRegistryName(Defaults.modPrefix.apply("potion_spikyobi_very_strong"));	
	private static final Potion OBI_LONG = new PotionSpikyObi("potioncapsule_spikyobi", new EffectInstance(EffectThorn.INSTANCE, 160 * 20), new EffectInstance(Effects.RESISTANCE, 160 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_spikyobi_long"));	
	private static final Potion OBI_STRONG_LONG = new PotionSpikyObi("potioncapsule_spikyobi", new EffectInstance(EffectThorn.INSTANCE, 100 * 20, 1), new EffectInstance(Effects.RESISTANCE, 100 * 20, 2)).setRegistryName(Defaults.modPrefix.apply("potion_spikyobi_strong_long"));	
	
	protected PotionSpikyObi(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(OBI,
						OBI_STRONG,
						OBI_VERY_STRONG,
						OBI_LONG,
						OBI_STRONG_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register5StageRecipe(ModItems.BLOCK_SPIKY_OBI, OBI, OBI_STRONG, OBI_VERY_STRONG, OBI_LONG, OBI_STRONG_LONG);
	}
	
}
