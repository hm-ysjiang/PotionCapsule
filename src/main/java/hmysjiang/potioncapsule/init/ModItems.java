package hmysjiang.potioncapsule.init;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference;
import hmysjiang.potioncapsule.Reference.BlockRegs;
import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.items.ItemCapsule;
import hmysjiang.potioncapsule.items.ItemCapsule.EnumCapsuleType;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import hmysjiang.potioncapsule.items.ItemFieryLilypad;
import hmysjiang.potioncapsule.items.ItemSpecialCapsule;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static final Item BLOCK_GELATIN_EXTRACTOR = new BlockItem(ModBlocks.GELATIN_EXTRACTOR, Defaults.itemProp.get()).setRegistryName(BlockRegs.GELATIN_EXTRACTOR);
	public static final Item BLOCK_GELATIN_FORMER = new BlockItem(ModBlocks.GELATIN_FORMER, Defaults.itemProp.get()).setRegistryName(BlockRegs.GELATIN_FORMER);
	public static final Item BLOCK_TINY_CACTUS = new BlockItem(ModBlocks.TINY_CACTI, Defaults.itemProp.get()).setRegistryName(BlockRegs.TINY_CACTI);
	public static final Item BLOCK_FIERY_LILY = new ItemFieryLilypad().setRegistryName(BlockRegs.FIERY_LILY);
	public static final Item BLOCK_SPIKY_OBI = new BlockItem(ModBlocks.SPIKY_OBI, Defaults.itemProp.get()).setRegistryName(BlockRegs.SPIKY_OBI);
	public static final Item BLOCK_CAPSULE_REPAIR = new BlockItem(ModBlocks.CAPSULE_REPAIR, Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(BlockRegs.SPECIAL_CAPSULE_REPAIRER));
	public static final Item BLOCK_AUTO_BREWER = new BlockItem(ModBlocks.AUTO_BREWER, Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(BlockRegs.AUTO_BREWER));;
	
	public static final Item CAPSULE = new ItemCapsule(EnumCapsuleType.NORMAL).setRegistryName(Defaults.modPrefix.apply(ItemRegs.CAPSULE));
	public static final Item CAPSULE_INSTANT = new ItemCapsule(EnumCapsuleType.INSTANT).setRegistryName(Defaults.modPrefix.apply(ItemRegs.CAPSULE + "_instant"));

	public static final Item PENDANT = new ItemCapsulePendant().setRegistryName(Defaults.modPrefix.apply(ItemRegs.PENDANT));

	public static final Item GELATIN_POWDER = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.GELATIN_POWDER));
	public static final Item WART_DUST = new Item(Defaults.itemProp.get()) {
		@OnlyIn(Dist.CLIENT)
		@Override
		public void addInformation(net.minecraft.item.ItemStack stack, net.minecraft.world.World worldIn, java.util.List<net.minecraft.util.text.ITextComponent> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.wart_dust.obtain").applyTextStyle(TextFormatting.GRAY));
		};
	}.setRegistryName(Defaults.modPrefix.apply(ItemRegs.WART_DUST));
	public static final Item APPLE_JELLY = new Item(Defaults.itemProp.get().food((new Food.Builder()).setAlwaysEdible().fastToEat().hunger(2).saturation(0.3F).build())).setRegistryName(Defaults.modPrefix.apply(ItemRegs.APPLE_JELLY));
	public static final Item CACTUS_JELLY = new Item(Defaults.itemProp.get().food((new Food.Builder()).setAlwaysEdible().fastToEat().hunger(2).saturation(0.3F).build())).setRegistryName(Defaults.modPrefix.apply(ItemRegs.CACTUS_JELLY));
	public static final Item CATALYST = new Item(Defaults.itemProp.get()) {
		@OnlyIn(Dist.CLIENT)
		@Override
		public void addInformation(net.minecraft.item.ItemStack stack, net.minecraft.world.World worldIn, java.util.List<net.minecraft.util.text.ITextComponent> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.catalyst_1").applyTextStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.catalyst_2", String.valueOf(CommonConfigs.recipe_instantCatalystAllowed.get())));
		};
	}.setRegistryName(Defaults.modPrefix.apply(ItemRegs.CATALYST));
	public static final Item CREATIVE_CATALYST = new Item(Defaults.itemProp.get().maxStackSize(1)) {
		@OnlyIn(Dist.CLIENT)
		@Override
		public void addInformation(net.minecraft.item.ItemStack stack, net.minecraft.world.World worldIn, java.util.List<net.minecraft.util.text.ITextComponent> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.c_catalyst_1").applyTextStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.c_catalyst_2", String.valueOf(CommonConfigs.recipe_instantCatalystAllowed.get())).applyTextStyle(TextFormatting.GRAY));
		};
	}.setRegistryName(Defaults.modPrefix.apply(ItemRegs.CREATIVE_CATALYST));
	public static final Item WITHER_FRAG = new Item(Defaults.itemProp.get()) {
		@OnlyIn(Dist.CLIENT)
		@Override
		public void addInformation(net.minecraft.item.ItemStack stack, net.minecraft.world.World worldIn, java.util.List<net.minecraft.util.text.ITextComponent> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.wither_dust.obtain").applyTextStyle(TextFormatting.GRAY));
		};
	}.setRegistryName(Defaults.modPrefix.apply(ItemRegs.WITHER_FRAG));
	public static final Item CACTUS_FRAG = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.CATCUS_FRAG));
	public static final Item CAT_FUR = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.CAT_FUR));
	public static final Item FANG = new Item(Defaults.itemProp.get()).setRegistryName(Defaults.modPrefix.apply(ItemRegs.FANG));
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		PotionCapsule.Logger.info("Items Registering");
		IForgeRegistry<Item> reg = event.getRegistry();
		reg.registerAll(BLOCK_GELATIN_EXTRACTOR,
						BLOCK_GELATIN_FORMER,
						BLOCK_CAPSULE_REPAIR,
						BLOCK_TINY_CACTUS,
						BLOCK_FIERY_LILY,
						BLOCK_SPIKY_OBI,
						BLOCK_AUTO_BREWER
						,
						CAPSULE,
						CAPSULE_INSTANT
						,
						PENDANT
						,
						GELATIN_POWDER,
						WART_DUST,
						APPLE_JELLY,
						CACTUS_JELLY,
						CATALYST,
						CREATIVE_CATALYST,
						WITHER_FRAG,
						CACTUS_FRAG,
						CAT_FUR,
						FANG);
		ItemSpecialCapsule.register(reg);
	}
	
}
