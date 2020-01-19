package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.configs.ClientConfigs;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class EffectNightVisionNF extends Effect {
	
	public static final EffectNightVisionNF INSTANCE = (EffectNightVisionNF) new EffectNightVisionNF().setRegistryName(Defaults.modPrefix.apply("effect_night_vision_nf"));

	protected EffectNightVisionNF() {
		super(EffectType.BENEFICIAL, Effects.NIGHT_VISION.getLiquidColor());
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn,
			AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		EffectInstance nv = entityLivingBaseIn.getActivePotionEffect(Effects.NIGHT_VISION);
		if (nv != null) {
			nv.duration = 1;
		}
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		if (entityLivingBaseIn != null) {
			EffectInstance effect = entityLivingBaseIn.getActivePotionEffect(this);
			if (effect != null) {
				entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 410, 0, false, true));
			}
		}
	}
	
	@Override
	public boolean shouldRenderHUD(EffectInstance effect) {
		return ClientConfigs.misc_renderNVNFHUD.get();
	}
	
}
