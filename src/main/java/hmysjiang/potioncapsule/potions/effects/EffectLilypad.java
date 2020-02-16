package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class EffectLilypad extends Effect {

	public static final EffectLilypad INSTANCE = (EffectLilypad) new EffectLilypad().setRegistryName(Defaults.modPrefix.apply("effect_lilypad"));

	protected EffectLilypad() {
		super(EffectType.BENEFICIAL, 0x17b745);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(LivingEntity living, int amplifier) {
		if (living != null && !living.isSneaking()) {
			if (living.getActivePotionEffect(this) != null) {
				BlockState under = living.world.getBlockState(living.getPosition().down());
				BlockState feet = living.world.getBlockState(living.getPosition());
				BlockState head = living.world.getBlockState(living.getPosition().up());
				if (under.getMaterial().isLiquid() && !feet.getMaterial().isLiquid()) {
					if (under.getMaterial() == Material.WATER)
						living.extinguish();
					if (under.getBlock() instanceof FlowingFluidBlock && ((FlowingFluidBlock) under.getBlock()).getFluid().isIn(FluidTags.LAVA)) {
						if (!living.isImmuneToFire()) {
							living.setFire(15);
							living.attackEntityFrom(DamageSource.LAVA, 4.0F);
						}
					}
					if (living.posY % 1 < 0.1) {
						Vec3d motion = living.getMotion();
						living.setMotion(motion.x, 0, motion.z);
						living.onGround = true;
						living.fallDistance = 0;
					}
				}
				else if (feet.getMaterial().isLiquid() && !head.getMaterial().isLiquid()) {
					if (living.posY % 1 > 0.4) {
						Vec3d motion = living.getMotion();
						living.setMotion(motion.x, 0.05, motion.z);
					}
				}
			}
		}
	}
	
}
