package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectBloodlust extends Effect {
	
	public static final EffectBloodlust INSTANCE = (EffectBloodlust) new EffectBloodlust().setRegistryName(Defaults.modPrefix.apply("effect_bloodlust"));

	protected EffectBloodlust() {
		super(EffectType.BENEFICIAL, 0xff0000);
	}
	
	@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		if (event.getSource() instanceof EntityDamageSource && !(event.getSource() instanceof IndirectEntityDamageSource)) {
			EntityDamageSource src = (EntityDamageSource) event.getSource();
			if (src.getIsThornsDamage())
				return;
			Entity sourceentity = src.getTrueSource();
			if (sourceentity != null && sourceentity instanceof LivingEntity) {
				LivingEntity source = (LivingEntity) sourceentity;
				if (source.getActivePotionEffect(INSTANCE) != null) {
					int amp = source.getActivePotionEffect(INSTANCE).getAmplifier();
					source.heal(event.getAmount() * (0.1F + amp * 0.1F));
				}
			}
		}
	}

}
