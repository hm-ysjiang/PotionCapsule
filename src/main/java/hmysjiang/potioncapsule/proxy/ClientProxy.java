package hmysjiang.potioncapsule.proxy;

import java.util.Arrays;

import hmysjiang.potioncapsule.client.CapsuleItemColor;
import hmysjiang.potioncapsule.client.KeyBindHandler;
import hmysjiang.potioncapsule.client.SpecialCapsuleItemColor;
import hmysjiang.potioncapsule.client.gui.ScreenAutoBrewer;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinExtractor;
import hmysjiang.potioncapsule.client.gui.ScreenGelatinFormer;
import hmysjiang.potioncapsule.client.gui.ScreenPendant;
import hmysjiang.potioncapsule.client.gui.ScreenSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import hmysjiang.potioncapsule.container.ContainerGelatinExtractor;
import hmysjiang.potioncapsule.container.ContainerGelatinFormer;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.container.ContainerSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule.EnumSpecialType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ClientProxy implements ISidedProxy {
	
	@Override
	public void init() {
		ISidedProxy.super.init();
		KeyBindHandler.init();

		Minecraft.getInstance().getItemColors().register(CapsuleItemColor.INSTANCE, ModItems.CAPSULE);
		Minecraft.getInstance().getItemColors().register(CapsuleItemColor.INSTANCE, ModItems.CAPSULE_INSTANT);

		Arrays.asList(EnumSpecialType.values()).stream().map(ItemSpecialCapsule::getCapsuleInstance).forEach(ClientProxy::registerSpecialColor);
		
		ScreenManager.registerFactory(ContainerPendant.TYPE, ScreenPendant::new);
		ScreenManager.registerFactory(ContainerGelatinExtractor.TYPE, ScreenGelatinExtractor::new);
		ScreenManager.registerFactory(ContainerGelatinFormer.TYPE, ScreenGelatinFormer::new);
		ScreenManager.registerFactory(ContainerSpecialCapsuleRepairer.TYPE, ScreenSpecialCapsuleRepairer::new);
		ScreenManager.registerFactory(ContainerAutoBrewer.TYPE, ScreenAutoBrewer::new);
	}
	
	@Override
	public PlayerEntity getPlayer() {
		return Minecraft.getInstance().player;
	}
	
	@Override
	public World getWorld() {
		return Minecraft.getInstance().world;
	}
	
	public static void registerSpecialColor(Item item) {
		Minecraft.getInstance().getItemColors().register(SpecialCapsuleItemColor.INSTANCE, item);
	}
	
}
