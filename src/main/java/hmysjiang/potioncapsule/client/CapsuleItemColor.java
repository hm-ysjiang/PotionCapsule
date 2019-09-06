package hmysjiang.potioncapsule.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

public class CapsuleItemColor implements IItemColor {
	
	public static final CapsuleItemColor INSTANCE = new CapsuleItemColor();

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (PotionUtils.getEffectsFromStack(stack).isEmpty() || tintIndex != 1)
			return 0xFFFFFFFF;
		return PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(stack));
	}

}
