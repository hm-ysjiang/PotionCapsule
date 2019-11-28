package hmysjiang.potioncapsule.utils.handler;

import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import hmysjiang.potioncapsule.utils.helper.InventoryHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.CapabilityItemHandler;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.FORGE)
public class EventHandler {
	private static DamageSource dmg = new DamageSource("pc_void_lost");
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack pendant = InventoryHelper.findStackFromPlayerInventory(player.inventory, new ItemStack(ModItems.PENDANT));
			if (!(event.getSource().equals(DamageSource.OUT_OF_WORLD) && player.posZ < -60))
				return;
			if (player.openContainer != null && player.openContainer instanceof ContainerPendant && ((ContainerPendant) player.openContainer).getStack() == pendant)
				return;
			if (!pendant.isEmpty()) {
				pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					for (int i = 8 ; i<11 ; i++) {
						ItemStack stack = handler.getStackInSlot(i);
						if (stack.getItem() == ItemSpecialCapsule.getCapsuleInstance(EnumSpecialType.LOST_CHRISTMAS)) {
							player.getCombatTracker().trackDamage(dmg, player.getHealth(), 0.0F);
							return;
						}
					}
				});
			}
		}
	}
	
}
