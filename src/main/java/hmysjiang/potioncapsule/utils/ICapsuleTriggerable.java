package hmysjiang.potioncapsule.utils;

import javax.annotation.Nonnull;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ICapsuleTriggerable {
	
	boolean canBeTriggered(@Nonnull ItemStack capsuleStack);
	
	ItemStack onTrigger(ItemStack stack, World world, LivingEntity entityLiving, boolean renderStatus);
	
}
