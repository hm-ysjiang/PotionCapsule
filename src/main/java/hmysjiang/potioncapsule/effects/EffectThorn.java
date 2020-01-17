package hmysjiang.potioncapsule.effects;

import hmysjiang.potioncapsule.PotionCapsule;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectThorn extends Effect {

	protected EffectThorn() {
		super(EffectType.NEUTRAL, 0xd9a11e);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	//@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		LivingEntity living = event.getEntityLiving();
		DamageSource ds = event.getSource();
		if (ds instanceof EntityDamageSource && !(ds instanceof IndirectEntityDamageSource) && !((EntityDamageSource) ds).getIsThornsDamage()) {
			EntityDamageSource src = (EntityDamageSource) ds;
			PotionCapsule.Logger.info(living);
			PotionCapsule.Logger.info(src);
		}
	}
	
}
