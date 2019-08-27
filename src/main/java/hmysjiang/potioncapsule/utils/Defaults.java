package hmysjiang.potioncapsule.utils;

import java.util.function.Function;
import java.util.function.Supplier;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class Defaults {
	
	public static Supplier<Item.Properties> itemProp = () -> new Item.Properties().group(PotionCapsule.GROUP);
	public static Function<String, ResourceLocation> modPrefix = path -> new ResourceLocation(Reference.MOD_ID, path);
	
}
