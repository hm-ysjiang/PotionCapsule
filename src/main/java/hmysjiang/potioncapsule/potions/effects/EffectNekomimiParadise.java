package hmysjiang.potioncapsule.potions.effects;

import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EffectNekomimiParadise extends Effect {
	
	public static final EffectNekomimiParadise INSTANCE = (EffectNekomimiParadise) new EffectNekomimiParadise().setRegistryName(Defaults.modPrefix.apply("effect_nekomimiparadise"));

	protected EffectNekomimiParadise() {
		super(EffectType.NEUTRAL, 0xffb3ea);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	public static class CatNekomimiGoal extends TemptGoal {
		private final CatEntity cat;
		private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(10.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
		
		private static final Ingredient BREED_ITEMS = Ingredient.fromItems(Items.COD, Items.SALMON);

		public CatNekomimiGoal(CatEntity catIn) {
			super(catIn, 0.6, false, BREED_ITEMS);
			cat = catIn;
		}
		
		public boolean shouldExecute() {
			super.shouldExecute();
			if (this.cat.isTamed())
				return false;
			closestPlayer = creature.world.getClosestPlayer(ENTITY_PREDICATE, creature);
			if (closestPlayer == null)
				return false;
			return closestPlayer.getActivePotionEffect(EffectNekomimiParadise.INSTANCE) != null;
		}
		
		@Override
		public void tick() {
			super.tick();
			cat.setSitting(creature.getDistanceSq(this.closestPlayer) < 6.25D);
		}
		
		@Override
		public void resetTask() {
			super.resetTask();
			cat.setSitting(false);
		}
	}
	
	public static class OcelotNekomimiGoal extends TemptGoal {
		private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(10.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
		
		private static final Ingredient BREED_ITEMS = Ingredient.fromItems(Items.COD, Items.SALMON);

		public OcelotNekomimiGoal(OcelotEntity catIn) {
			super(catIn, 0.6, false, BREED_ITEMS);
		}
		
		public boolean shouldExecute() {
			super.shouldExecute();
			closestPlayer = creature.world.getClosestPlayer(ENTITY_PREDICATE, creature);
			if (closestPlayer == null)
				return false;
			return closestPlayer.getActivePotionEffect(EffectNekomimiParadise.INSTANCE) != null;
		}
	}
	
}
