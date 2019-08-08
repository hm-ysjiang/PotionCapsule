package hmysjiang.potioncapsule.utils;

import java.util.function.Function;
import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;

public class Defaults {
	
	public static Supplier<Properties> itemProp = () -> { return new Properties().group(PotionCapsule.GROUP); };
	public static Function<String, ResourceLocation> modPrefix = path -> { return new ResourceLocation(Reference.MOD_ID, path); };
	
}
