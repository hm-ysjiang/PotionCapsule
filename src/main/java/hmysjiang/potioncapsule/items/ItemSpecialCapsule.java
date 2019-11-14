package hmysjiang.potioncapsule.items;

import java.util.List;

import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.SPacketVisualExplosion;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.helper.InventoryHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.IStringSerializable;
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

@EventBusSubscriber(bus=EventBusSubscriber.Bus.FORGE)
public class ItemSpecialCapsule extends Item {
	
	public final EnumSpecialType type;
	
	public ItemSpecialCapsule(EnumSpecialType type) {
		super(Defaults.itemProp.get());
		this.type = type;
		setRegistryName(Defaults.modPrefix.apply(ItemRegs.SPECIAL_CAPSULE + type.getPost()));
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void biteZaDust(LivingDeathEvent dusto) {
		if (dusto.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) dusto.getEntityLiving();
			ItemStack pendant = InventoryHelper.findStackFromPlayerInventory(player.inventory, new ItemStack(ModItems.PENDANT));
			if (!pendant.isEmpty()) {
				pendant.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					for (int i = 8 ; i<11 ; i++) {
						if (handler.getStackInSlot(i).getItem() == ModItems.S_CAPSULE_BITEZDUST) {
							ItemStack stack = handler.getStackInSlot(i);
							if (!player.isCreative() && !stack.getOrCreateTag().getBoolean("CapsuleCreative")) {
								stack.shrink(1);
							}
							if (player instanceof ServerPlayerEntity) {
								CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
							}
							player.addStat(Stats.ITEM_USED.get(ModItems.S_CAPSULE_BITEZDUST));
							dusto.setCanceled(true);
							player.setHealth(player.getMaxHealth());
							player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 100, 2, false, false));
							player.sendStatusMessage(new TranslationTextComponent("potioncapsule.tooltip.capsule.used", stack.getDisplayName()), true);
							PacketHandler.toPlayer(new SPacketVisualExplosion(), (ServerPlayerEntity) player);
							return;
						}
					}
				});
			}
		}
	}
	
	public static enum EnumSpecialType implements IStringSerializable {
		BITEZDUST("bite_za_dust", 0xcf4afc, TextFormatting.LIGHT_PURPLE);
		
		public String name;
		public int color;
		private TextFormatting[] format;
		
		private EnumSpecialType(String name, int color, TextFormatting... formats) {
			this.name = name;
			this.color = color;
			this.format = formats;
		}
		
		public String getPost() {
			return "_" + this.name;
		}
		
		public int getColor() {
			return this.color;
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
		
		@Override
		public String getName() {
			return this.name;
		}
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

}
