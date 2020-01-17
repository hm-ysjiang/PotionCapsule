package hmysjiang.potioncapsule.utils.handler;

import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class LootTableHandler {
	
	@SubscribeEvent
	public static void onLootTableLoad(LootTableLoadEvent event) {
		String lootName = event.getName().toString();
		if (lootName.startsWith("minecraft:chests/")) {
			String path = lootName.substring(17);
			switch (path) {
			case "abandoned_mineshaft":
			case "buried_treasure":
			case "desert_pyramid":
			case "nether_bridge":
			case "simple_dungeon":
			case "spawn_bonus_chest":
				event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(Defaults.modPrefix.apply("inject/" + path)).weight(1))
														   .name(Defaults.modPrefix.apply("lootpool_chest").toString())
														   .build());
			default:
				break;
			}
		}
		else if (lootName.equals("minecraft:blocks/nether_wart")) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(Defaults.modPrefix.apply("inject/nether_wart")).weight(1))
													   .name(Defaults.modPrefix.apply("lootpool_wart").toString())
													   .build());
		}
		else if (lootName.equals("minecraft:entities/wither_skeleton")) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(Defaults.modPrefix.apply("inject/wither_skeleton")).weight(1))
													   .name(Defaults.modPrefix.apply("lootpool_wither").toString())
													   .build());
		}
	}
	
}
