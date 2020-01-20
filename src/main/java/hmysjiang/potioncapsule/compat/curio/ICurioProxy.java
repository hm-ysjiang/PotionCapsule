package hmysjiang.potioncapsule.compat.curio;

import hmysjiang.potioncapsule.PotionCapsule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public interface ICurioProxy {
	
	static boolean isCurioLoaded() {
		return PotionCapsule.curioOpt.isPresent();
	}
	
	default void attachCaps(AttachCapabilitiesEvent<ItemStack> event) {
		
	}
	
	default void enqueueIMC() {
		
	}
	
	default ItemStack findCurio(ItemStack sample, PlayerEntity player) {
		return ItemStack.EMPTY;
	}
	
}
