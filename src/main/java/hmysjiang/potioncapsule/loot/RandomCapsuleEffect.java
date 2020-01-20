package hmysjiang.potioncapsule.loot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class RandomCapsuleEffect extends LootFunction {
	
	public static final Serializer SERIALIZER = new Serializer(Defaults.modPrefix.apply("random_capsule_effect"), RandomCapsuleEffect.class);
	
	private final List<Float> dropChances;
	private static Random rand = new Random();
	protected RandomCapsuleEffect(ILootCondition[] conditionsIn, List<Float> dropChance) {
		super(conditionsIn);
		dropChances = dropChance;
	}

	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context) {
		if (ItemCapsule.isItemCapsule(stack)) {
			int amount = 0;
			float f = rand.nextFloat();
			for (int i = 0 ; i<dropChances.size() ; i++, amount++) {
				if (dropChances.get(i) < f)
					break;
			}
			ItemCapsule.applyRandomEffectFromPool(stack, amount);
		}
		return stack;
	}
	
	static class Serializer extends LootFunction.Serializer<RandomCapsuleEffect> {

		public Serializer(ResourceLocation p_i50228_1_, Class<RandomCapsuleEffect> p_i50228_2_) {
			super(p_i50228_1_, p_i50228_2_);
		}
		
		@Override
		public void serialize(JsonObject object, RandomCapsuleEffect functionClazz, JsonSerializationContext serializationContext) {
			super.serialize(object, functionClazz, serializationContext);
			JsonArray arr = new JsonArray();
			for (float f: functionClazz.dropChances) {
				arr.add(f);
			}
			object.add("drops", arr);
		}

		@Override
		public RandomCapsuleEffect deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
			if (object.has("drops")) {
				List<Float> chances = new ArrayList<Float>();
				for (JsonElement json: JSONUtils.getJsonArray(object, "drops")) {
					chances.add(json.getAsFloat());
				}
				return new RandomCapsuleEffect(conditionsIn, chances);
			}
			return new RandomCapsuleEffect(conditionsIn, Arrays.asList(1F));
		}
		
	}

}
