package hmysjiang.potioncapsule.proxy;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.loot.RandomCapsuleEffect;
import hmysjiang.potioncapsule.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.loading.FMLEnvironment;

public interface ISidedProxy {
	
	default void init() {
		PacketHandler.register();
		LootFunctionManager.registerFunction(RandomCapsuleEffect.SERIALIZER);
	}
	
	@Nullable
	default <T> T doSidedWork(Callable<T> serverWork, Callable<T> clientWork) {
		try {
			switch (FMLEnvironment.dist) {
			case CLIENT:
				return clientWork.call();
			case DEDICATED_SERVER:
				return serverWork.call();
			default:
				PotionCapsule.Logger.error("Unsided env?");
				return null;
			}
		} catch (Exception exp) {
			PotionCapsule.Logger.error("Exception occured while executing sided work\n" + exp.getLocalizedMessage());
		}
		return null;
	}
	
	default PlayerEntity getPlayer() {
		return null;
	}
	
	default World getWorld() {
		return null;
	}
	
}
