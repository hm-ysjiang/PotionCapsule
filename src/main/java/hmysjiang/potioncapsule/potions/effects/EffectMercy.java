package hmysjiang.potioncapsule.potions.effects;

import java.util.Random;

import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.SPacketPlayerParticle;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectMercy extends Effect {
	
	private static Random rand = new Random();
	
	public static final EffectMercy INSTANCE = (EffectMercy) new EffectMercy().setRegistryName(Defaults.modPrefix.apply("effect_mercy"));

	protected EffectMercy() {
		super(EffectType.NEUTRAL, 0x7281ba);
	}
	
	@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		if (target.getHealth() <= 1F)
			return;
		if (event.getSource() instanceof EntityDamageSource) {
			EntityDamageSource src = (EntityDamageSource) event.getSource();
			Entity sourceentity = src.getTrueSource();
			if (sourceentity != null && sourceentity instanceof LivingEntity) {
				LivingEntity source = (LivingEntity) sourceentity;
				if (source.getActivePotionEffect(INSTANCE) != null) {
					if (target.getHealth() - event.getAmount() < 1F) {
						event.setAmount(target.getHealth() - 1F);
						if (source instanceof ServerPlayerEntity) {
							ServerPlayerEntity player = (ServerPlayerEntity) source;
							target.world.playSound(player, target.posX, target.posY, target.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
							for (int i = 0; i < 10; ++i) {
								double d0 = rand.nextGaussian() * 0.02D;
								double d1 = rand.nextGaussian() * 0.02D;
								double d2 = rand.nextGaussian() * 0.02D;
								PacketHandler.toPlayer(new SPacketPlayerParticle(ParticleTypes.HAPPY_VILLAGER,
										target.posX + (double) (rand.nextFloat() * target.getWidth() * 2.0F)
												- (double) target.getWidth(),
										target.posY + 1.0D + (double) (rand.nextFloat() * target.getHeight()),
										target.posZ + (double) (rand.nextFloat() * target.getWidth() * 2.0F)
												- (double) target.getWidth(),
										d0, d1, d2), player);
							}
						}
					}
				}
			}
		}
	}

}
