package hmysjiang.potioncapsule.proxy;

import hmysjiang.potioncapsule.client.CapsuleItemColor;
import hmysjiang.potioncapsule.client.KeyBindHandler;
import hmysjiang.potioncapsule.client.SpecialCapsuleItemColor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinExtractor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinFormer;
import hmysjiang.potioncapsule.client.gui.ScreenPendant;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.container.ContainerGelatinFormer;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;

public class ClientProxy implements ISidedProxy {
	
	@Override
	public void init() {
		ISidedProxy.super.init();
		KeyBindHandler.init();

		Minecraft.getInstance().getItemColors().register(CapsuleItemColor.INSTANCE, ModItems.CAPSULE);
		Minecraft.getInstance().getItemColors().register(CapsuleItemColor.INSTANCE, ModItems.CAPSULE_INSTANT);

		Minecraft.getInstance().getItemColors().register(SpecialCapsuleItemColor.INSTANCE, ItemSpecialCapsule.getCapsuleInstance(EnumSpecialType.BITEZDUST));
		Minecraft.getInstance().getItemColors().register(SpecialCapsuleItemColor.INSTANCE, ItemSpecialCapsule.getCapsuleInstance(EnumSpecialType.LOST_CHRISTMAS));
		
		ScreenManager.registerFactory(ContainerPendant.TYPE, ScreenPendant::new);
		ScreenManager.registerFactory(ContainerGelatinExtractor.TYPE, ScreenGelatinExtractor::new);
		ScreenManager.registerFactory(ContainerGelatinFormer.TYPE, ScreenGelatinFormer::new);
	}
	
	@Override
	public PlayerEntity getPlayer() {
		return Minecraft.getInstance().player;
	}
	
}
