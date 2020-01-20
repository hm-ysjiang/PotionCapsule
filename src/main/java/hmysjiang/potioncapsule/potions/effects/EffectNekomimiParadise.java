package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectNekomimiParadise extends Effect {
	
	public static final EffectNekomimiParadise INSTANCE = (EffectNekomimiParadise) new EffectNekomimiParadise().setRegistryName(Defaults.modPrefix.apply("effect_nekomimiparadise"));

	protected EffectNekomimiParadise() {
		super(EffectType.NEUTRAL, 0xd692c3);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
}
