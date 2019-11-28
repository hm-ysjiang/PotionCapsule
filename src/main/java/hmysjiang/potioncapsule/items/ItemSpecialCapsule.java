package hmysjiang.potioncapsule.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.SPacketVisualExplosion;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.InventoryHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.FORGE)
public class ItemSpecialCapsule extends Item {

	private static Map<EnumSpecialType, Item> capsules = new HashMap<>();
	public static Item getCapsuleInstance(EnumSpecialType type) { return capsules.get(type); }
	private static Map<EnumSpecialType, Boolean> enable = new HashMap<>();
	private static Map<EnumSpecialType, Integer> durab = new HashMap<>();
	
	public final EnumSpecialType type;
	
	public ItemSpecialCapsule(EnumSpecialType type) {
		super(Defaults.itemProp.get().maxDamage(durab.get(type)));
		this.type = type;
		setRegistryName(Defaults.modPrefix.apply(ItemRegs.SPECIAL_CAPSULE + type.getPost()));
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack pendant = InventoryHelper.findStackFromPlayerInventory(player.inventory, new ItemStack(ModItems.PENDANT));
			if (player.openContainer != null && player.openContainer instanceof ContainerPendant && ((ContainerPendant) player.openContainer).getStack() == pendant)
				return;
			if (!pendant.isEmpty()) {
				pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					int bzd = -1, xmas = -1;
					
					for (int i = 8 ; i<11 ; i++) {
						ItemStack stack = handler.getStackInSlot(i);
						if (stack.getItem() == capsules.get(EnumSpecialType.LOST_CHRISTMAS)) {
							xmas = i;
						}
						else if (stack.getItem() == capsules.get(EnumSpecialType.BITEZDUST)) {
							bzd = i;
						}
					}
					
					if (xmas > 0) {
						if (saluteTheBirthOfKing(handler.getStackInSlot(xmas), player, event))
							return;
					}
					if (bzd > 0) {
						biteZaDust(handler.getStackInSlot(bzd), player, event);
					}
				});
			}
		}
	}
	
	public static boolean biteZaDust(ItemStack stack, PlayerEntity player, LivingDeathEvent dusto) {
		if (stack.getDamage() == stack.getMaxDamage())
			return false;
		player.setHealth(player.getMaxHealth());
		player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 100, 2, false, false));
		player.sendStatusMessage(new TranslationTextComponent("potioncapsule.tooltip.capsule.used", stack.getDisplayName()), true);
		PacketHandler.toPlayer(new SPacketVisualExplosion(), (ServerPlayerEntity) player);
		
		if (!player.isCreative() && !stack.getOrCreateTag().getBoolean("CapsuleCreative")) {
			stack.setDamage(stack.getDamage() + 1);
		}
		if (player instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
		}
		player.addStat(Stats.ITEM_USED.get(capsules.get(EnumSpecialType.BITEZDUST)));
		dusto.setCanceled(true);
		return true;
	}
	
	public static boolean saluteTheBirthOfKing(ItemStack stack, PlayerEntity player, LivingDeathEvent vergissmeinnicht) {
		if (stack.getDamage() == stack.getMaxDamage())
			return false;
		if (!(vergissmeinnicht.getSource().equals(DamageSource.OUT_OF_WORLD) && player.posZ < -60))
			return false;
		
		player.setHealth(1);
		player.sendStatusMessage(new TranslationTextComponent("potioncapsule.tooltip.capsule.used", stack.getDisplayName()), true);
		BlockPos pos = player.getPosition();
		for(pos = new BlockPos(pos.getX(), 0, pos.getZ()); !player.world.isAirBlock(pos.up()); pos = pos.up());
		player.world.setBlockState(pos, Blocks.GLASS.getDefaultState());
		player.setMotion(player.getMotion().x, 0, player.getMotion().z);
		player.fallDistance = 0.0F;
		player.onGround = true;
		player.setPositionAndUpdate(player.posX, pos.getY() + 1.2, player.posZ);
		
		if (!player.isCreative() && !stack.getOrCreateTag().getBoolean("CapsuleCreative")) {
			stack.setDamage(stack.getDamage() + 1);
		}
		if (player instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
		}
		player.addStat(Stats.ITEM_USED.get(capsules.get(EnumSpecialType.LOST_CHRISTMAS)));
		vergissmeinnicht.setCanceled(true);
		return true;
	}
	
	public static enum EnumSpecialType implements IStringSerializable {
		BITEZDUST("bite_za_dust", 0xcf4afc, TextFormatting.LIGHT_PURPLE),
		LOST_CHRISTMAS("lost_christmas", 0x2a7bcc, TextFormatting.DARK_GRAY);
		
		public String name;
		public int capcolor;
		private TextFormatting[] format;
		
		private EnumSpecialType(String name, int color, TextFormatting... formats) {
			this.name = name;
			this.capcolor = color;
			this.format = formats;
		}
		
		public String getPost() {
			return "_" + this.name;
		}
		
		public int getCapColor() {
			return this.capcolor;
		}
		
		public String getDisplayKey() {
			return "item.potioncapsule.item_special_capsule." + this.name;
		}
		
		public String getDescriptionKey() {
			return "potioncapsule.tooltip.special_capsule." + this.name + ".desc";
		}
		
		public String getTriggerKey() {
			return "potioncapsule.tooltip.special_capsule." + this.name + ".trigger";
		}
		
		public String getEffectKey() {
			return "potioncapsule.tooltip.special_capsule." + this.name + ".effect";
		}
		
		public TextFormatting[] getFormat() {
			return format;
		}
		
		public ResourceLocation getRepairTag() {
			return Defaults.modPrefix.apply(name + "_repair");
		}
		
		@Override
		public String getName() {
			return this.name;
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if (toRepair.getItem() instanceof ItemSpecialCapsule) {
			if (toRepair.getOrCreateTag().getBoolean("CapsuleCreative"))
				return false;
			ItemSpecialCapsule item = (ItemSpecialCapsule) toRepair.getItem();
			return ItemTags.getCollection().get(item.type.getRepairTag()).contains(repair.getItem());
		}
		return super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("CapsuleCreative");
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(type.getDescriptionKey()));
		tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.special_capsule.pre.trigger").appendSibling(
				new TranslationTextComponent(type.getTriggerKey()).applyTextStyle(TextFormatting.GRAY)
				).applyTextStyle(TextFormatting.AQUA));
		tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.special_capsule.pre.effect").appendSibling(
				new TranslationTextComponent(type.getEffectKey()).applyTextStyle(TextFormatting.GRAY)
				).applyTextStyle(TextFormatting.GREEN));
	}
	
	@Override
	public String getTranslationKey() {
		return "item.potioncapsule.item_special_capsule";
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		if (stack.getOrCreateTag().getBoolean("CapsuleCreative"))
			return new TranslationTextComponent("item.potioncapsule.item_creative_capsule_prefix").appendSibling(
					new TranslationTextComponent(getTranslationKey(), new TranslationTextComponent(type.getDisplayKey()).applyTextStyles(type.getFormat()))).applyTextStyle(TextFormatting.GOLD);
		return new TranslationTextComponent(getTranslationKey(), new TranslationTextComponent(type.getDisplayKey()).applyTextStyles(type.getFormat())).applyTextStyle(TextFormatting.GOLD);
	}
	
	public static void init() {
		buildMaps();
		for (EnumSpecialType type: EnumSpecialType.values()) {
			capsules.put(type, new ItemSpecialCapsule(type));
		}
	}
	
	private static void buildMaps() {
		PotionCapsule.Logger.info("Building Information map for Special Capsules");
		for (EnumSpecialType type: EnumSpecialType.values()) {
			switch (type) {
			case BITEZDUST:
				enable.put(type, CommonConfigs.special_bzd_enable.get());
				durab.put(type, CommonConfigs.special_bzd_uses.get());
				break;
			case LOST_CHRISTMAS:
				enable.put(type, CommonConfigs.special_xmas_enable.get());
				durab.put(type, CommonConfigs.special_xmas_uses.get());
				break;
			}
		}
	}
	
	public static void register(IForgeRegistry<Item> reg) {
		PotionCapsule.Logger.info("Registering Special Capsules");
		for (EnumSpecialType type: EnumSpecialType.values()) {
			if (enable.get(type)) {
				reg.register(capsules.get(type));
				PotionCapsule.Logger.info("Type " + type.getName() + " is enabled, registering...");
			}
		}
	}

}
