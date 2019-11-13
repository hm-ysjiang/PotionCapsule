package hmysjiang.potioncapsule.client;

import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class SpecialCapsuleItemColor implements IItemColor {
	
	public static final SpecialCapsuleItemColor INSTANCE = new SpecialCapsuleItemColor();

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (tintIndex == 1)
			return 0xffff70;
		if (tintIndex == 2)
			return ((ItemSpecialCapsule) stack.getItem()).type.color;
		return -1;
	}

}
