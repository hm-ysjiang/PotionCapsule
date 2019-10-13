package hmysjiang.potioncapsule.compact.curio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

public class CurioProxy implements ICurioProxy {
	
	@Override
	public void attachCaps(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() != ModItems.PENDANT)
			return;
		ICurio curio = new ICurio() {
			@Override
			public void onCurioTick(String identifier, int index, LivingEntity livingEntity) {
				if (livingEntity instanceof PlayerEntity)
					CuriosAPI.getCurioEquipped(ModItems.PENDANT, livingEntity).ifPresent(pendant -> {
						((ItemCapsulePendant) ModItems.PENDANT).onTick(pendant.getRight(), (PlayerEntity) livingEntity, livingEntity.world);
					});
			}
		};
		ICapabilityProvider provider = new ICapabilityProvider() {
			private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
			}
		};
		event.addCapability(CuriosCapability.ID_ITEM, provider);
	}
	
	@Override
	public void enqueueIMC() {
		PotionCapsule.Logger.info("Curios is loaded, registering Capsule Pendant as a necklace");
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
	}
	
	@Override
	public ItemStack findCurio(ItemStack sample, PlayerEntity player) {
		return CuriosAPI.getCurioEquipped(sample.getItem(), player).orElse(new ImmutableTriple<>("", 0, ItemStack.EMPTY)).getRight();
	}
	
}
