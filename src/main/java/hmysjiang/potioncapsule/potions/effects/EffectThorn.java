package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectThorn extends Effect {
	
	public static final EffectThorn INSTANCE = (EffectThorn) new EffectThorn().setRegistryName(Defaults.modPrefix.apply("effect_thorn"));

	protected EffectThorn() {
		super(EffectType.BENEFICIAL, 0xf2ee72);
	}
	
	@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		LivingEntity living = event.getEntityLiving();
		EffectInstance ins = living.getActivePotionEffect(INSTANCE);
		if (ins != null) {
			DamageSource ds = event.getSource();
			if (ds instanceof EntityDamageSource && !(ds instanceof IndirectEntityDamageSource) && !((EntityDamageSource) ds).getIsThornsDamage()) {
				EntityDamageSource src = (EntityDamageSource) ds;
				int amp = ins.getAmplifier();
				float toReflect = event.getAmount() * (0.2F + (0.2F * amp)) + 1F;
				src.getTrueSource().attackEntityFrom(new EntityDamageSource("potioncapsule_thorn", living).setIsThornsDamage().setMagicDamage(), toReflect);
			}
		}
	}
	
}
