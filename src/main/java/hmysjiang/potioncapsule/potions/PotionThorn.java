package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.potions.effects.EffectThorn;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionThorn extends Potion {
	
	private static final Potion THORN = new PotionThorn("potioncapsule_thorn", new EffectInstance(EffectThorn.INSTANCE, 180 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_thorn"));	
	private static final Potion THORN_STRONG = new PotionThorn("potioncapsule_thorn", new EffectInstance(EffectThorn.INSTANCE, 80 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_thorn_strong"));	
	private static final Potion THORN_VERY_STRONG = new PotionThorn("potioncapsule_thorn", new EffectInstance(EffectThorn.INSTANCE, 40 * 20, 2)).setRegistryName(Defaults.modPrefix.apply("potion_thorn_very_strong"));
	private static final Potion THORN_LONG = new PotionThorn("potioncapsule_thorn", new EffectInstance(EffectThorn.INSTANCE, 300 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_thorn_long"));	
	private static final Potion THORN_STRONG_LONG = new PotionThorn("potioncapsule_thorn", new EffectInstance(EffectThorn.INSTANCE, 140 * 20, 1)).setRegistryName(Defaults.modPrefix.apply("potion_thorn_strong_long"));	
	
	protected PotionThorn(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(THORN,
						THORN_STRONG,
						THORN_VERY_STRONG,
						THORN_LONG,
						THORN_STRONG_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register5StageRecipe(ModItems.CACTUS_FRAG, THORN, THORN_STRONG, THORN_VERY_STRONG, THORN_LONG, THORN_STRONG_LONG);
	}
	
}
