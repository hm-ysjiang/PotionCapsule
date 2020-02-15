package hmysjiang.potioncapsule.client;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value= {Dist.CLIENT})
public class ClientEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.getOrCreateTag().getBoolean("pc_CustomLingerPotion")) {
			event.getToolTip().clear();
			resetLingerPotionTooltip(stack, event.getEntityPlayer(), event.getToolTip(), event.getFlags());
		}
	}

	@SuppressWarnings("deprecation")
	private static void resetLingerPotionTooltip(ItemStack stack, PlayerEntity playerIn, List<ITextComponent> list,
			ITooltipFlag advanced) {
		ITextComponent itextcomponent = (new StringTextComponent("")).appendSibling(stack.getDisplayName()).applyTextStyle(stack.getRarity().color);
		if (stack.hasDisplayName()) {
			itextcomponent.applyTextStyle(TextFormatting.ITALIC);
		}

		list.add(itextcomponent);
		if (!advanced.isAdvanced() && !stack.hasDisplayName() && stack.getItem() == Items.FILLED_MAP) {
			list.add((new StringTextComponent("#" + FilledMapItem.getMapId(stack))).applyTextStyle(TextFormatting.GRAY));
		}

		int i = 0;
		if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
			i = stack.getTag().getInt("HideFlags");
		}

		if ((i & 32) == 0) {
			PotionUtils.addPotionTooltip(stack, list, 1.0F);
		}

		if (stack.hasTag()) {
			if ((i & 1) == 0) {
				ItemStack.addEnchantmentTooltips(list, stack.getEnchantmentTagList());
			}

			if (stack.getTag().contains("display", 10)) {
				CompoundNBT compoundnbt = stack.getTag().getCompound("display");
				if (compoundnbt.contains("color", 3)) {
					if (advanced.isAdvanced()) {
						list.add((new TranslationTextComponent("item.color", String.format("#%06X", compoundnbt.getInt("color")))).applyTextStyle(TextFormatting.GRAY));
					} else {
						list.add((new TranslationTextComponent("item.dyed")).applyTextStyles(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
					}
				}

				if (compoundnbt.getTagId("Lore") == 9) {
					ListNBT listnbt = compoundnbt.getList("Lore", 8);

					for (int j = 0; j < listnbt.size(); ++j) {
						String s = listnbt.getString(j);

						try {
							ITextComponent itextcomponent1 = ITextComponent.Serializer.fromJson(s);
							if (itextcomponent1 != null) {
								list.add(TextComponentUtils.mergeStyles(itextcomponent1, (new Style()).setColor(TextFormatting.DARK_PURPLE).setItalic(true)));
							}
						} catch (JsonParseException var19) {
							compoundnbt.remove("Lore");
						}
					}
				}
			}
		}

		for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
			Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(equipmentslottype);
			if (!multimap.isEmpty() && (i & 2) == 0) {
				list.add(new StringTextComponent(""));
				list.add((new TranslationTextComponent("item.modifiers." + equipmentslottype.getName())).applyTextStyle(TextFormatting.GRAY));

				for (Entry<String, AttributeModifier> entry : multimap.entries()) {
					AttributeModifier attributemodifier = entry.getValue();
					double d0 = attributemodifier.getAmount();
					boolean flag = false;
					// if (playerIn != null) {
					// if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER) {
					// d0 = d0 +
					// playerIn.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
					// d0 = d0 + (double) EnchantmentHelper.getModifierForCreature(stack,
					// CreatureAttribute.UNDEFINED);
					// flag = true;
					// } else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER) {
					// d0 +=
					// playerIn.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
					// flag = true;
					// }
					// }

					double d1;
					if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
						d1 = d0;
					} else {
						d1 = d0 * 100.0D;
					}

					if (flag) {
						list.add((new StringTextComponent(" ")).appendSibling(new TranslationTextComponent("attribute.modifier.equals." + attributemodifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String) entry.getKey()))).applyTextStyle(TextFormatting.DARK_GREEN));
					} else if (d0 > 0.0D) {
						list.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String) entry.getKey()))).applyTextStyle(TextFormatting.BLUE));
					} else if (d0 < 0.0D) {
						d1 = d1 * -1.0D;
						list.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String) entry.getKey()))).applyTextStyle(TextFormatting.RED));
					}
				}
			}
		}

		if (stack.hasTag() && stack.getTag().getBoolean("Unbreakable") && (i & 4) == 0) {
			list.add((new TranslationTextComponent("item.unbreakable")).applyTextStyle(TextFormatting.BLUE));
		}

		if (stack.hasTag() && stack.getTag().contains("CanDestroy", 9) && (i & 8) == 0) {
			ListNBT listnbt1 = stack.getTag().getList("CanDestroy", 8);
			if (!listnbt1.isEmpty()) {
				list.add(new StringTextComponent(""));
				list.add((new TranslationTextComponent("item.canBreak")).applyTextStyle(TextFormatting.GRAY));

				for (int k = 0; k < listnbt1.size(); ++k) {
					list.addAll(getPlacementTooltip(listnbt1.getString(k)));
				}
			}
		}

		if (stack.hasTag() && stack.getTag().contains("CanPlaceOn", 9) && (i & 16) == 0) {
			ListNBT listnbt2 = stack.getTag().getList("CanPlaceOn", 8);
			if (!listnbt2.isEmpty()) {
				list.add(new StringTextComponent(""));
				list.add((new TranslationTextComponent("item.canPlace")).applyTextStyle(TextFormatting.GRAY));

				for (int l = 0; l < listnbt2.size(); ++l) {
					list.addAll(getPlacementTooltip(listnbt2.getString(l)));
				}
			}
		}

		if (advanced.isAdvanced()) {
			if (stack.isDamaged()) {
				list.add(new TranslationTextComponent("item.durability", stack.getMaxDamage() - stack.getDamage(), stack.getMaxDamage()));
			}

			list.add((new StringTextComponent(Registry.ITEM.getKey(stack.getItem()).toString())).applyTextStyle(TextFormatting.DARK_GRAY));
			if (stack.hasTag()) {
				list.add((new TranslationTextComponent("item.nbt_tags", stack.getTag().keySet().size())).applyTextStyle(TextFormatting.DARK_GRAY));
			}
		}
	}

	private static Collection<ITextComponent> getPlacementTooltip(String stateString) {
		try {
			BlockStateParser blockstateparser = (new BlockStateParser(new StringReader(stateString), true)).parse(true);
			BlockState blockstate = blockstateparser.getState();
			ResourceLocation resourcelocation = blockstateparser.getTag();
			boolean flag = blockstate != null;
			boolean flag1 = resourcelocation != null;
			if (flag || flag1) {
				if (flag) {
					return Lists.newArrayList(blockstate.getBlock().getNameTextComponent().applyTextStyle(TextFormatting.DARK_GRAY));
				}

				Tag<Block> tag = BlockTags.getCollection().get(resourcelocation);
				if (tag != null) {
					Collection<Block> collection = tag.getAllElements();
					if (!collection.isEmpty()) {
						return collection.stream().map(Block::getNameTextComponent).map((p_222119_0_) -> {
							return p_222119_0_.applyTextStyle(TextFormatting.DARK_GRAY);
						}).collect(Collectors.toList());
					}
				}
			}
		} catch (CommandSyntaxException var8) {
			;
		}

		return Lists.newArrayList((new StringTextComponent("missingno")).applyTextStyle(TextFormatting.DARK_GRAY));
	}

}
