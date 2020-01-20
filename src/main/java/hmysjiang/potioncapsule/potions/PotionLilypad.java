package hmysjiang.potioncapsule.potions;

import hmysjiang.potioncapsule.potions.effects.EffectLilypad;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.PotionHelper;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionLilypad extends Potion {
	
	public static final Potion LILY = new PotionLilypad("potioncapsule_lilypad", new EffectInstance(EffectLilypad.INSTANCE, 180 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_lilypad"));	
	public static final Potion LILY_LONG = new PotionLilypad("potioncapsule_lilypad", new EffectInstance(EffectLilypad.INSTANCE, 300 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_lilypad_long"));	
	public static final Potion LILY_VERY_LONG = new PotionLilypad("potioncapsule_lilypad", new EffectInstance(EffectLilypad.INSTANCE, 480 * 20)).setRegistryName(Defaults.modPrefix.apply("potion_lilypad_very_long"));	
	
	protected PotionLilypad(String basename, EffectInstance... effects) {
		super(basename, effects);
	}
	
	public static void register(IForgeRegistry<Potion> reg) {
		reg.registerAll(LILY,
						LILY_LONG,
						LILY_VERY_LONG);
	}
	
	public static void registerRecipes() {
		PotionHelper.register3StageRecipe(Items.LILY_PAD, LILY, LILY_LONG, LILY_VERY_LONG);
	}
	
}
