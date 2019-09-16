package hmysjiang.potioncapsule.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.configs.ClientConfigs;
import hmysjiang.potioncapsule.configs.ServerConfigs;
import hmysjiang.potioncapsule.effects.EffectNightVisionNF;
import hmysjiang.potioncapsule.init.ModItems;
import hmysjiang.potioncapsule.utils.Defaults;
import hmysjiang.potioncapsule.utils.text.CapsuleUsedTextComponent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemCapsule extends Item {
	public static boolean isItemCapsule(ItemStack stack) {
		return getCapsuleType(stack.getItem()) != null;
	}
	public static EnumCapsuleType getCapsuleType(Item item) {
		if (item != ModItems.CAPSULE && item != ModItems.CAPSULE_INSTANT && item != ModItems.CAPSULE_SPECIAL)
			return null;
		return ((ItemCapsule) item).TYPE;
	}
	public static boolean canApplyEffectOnCapsule(ItemStack capsule, Effect effect) {
		return canApplyEffectOnType(getCapsuleType(capsule.getItem()), effect);
	}
	public static boolean canApplyEffectOnType(EnumCapsuleType type, Effect effect) {
		if (effect == null)
			return false;
		if (type == null || type == EnumCapsuleType.SPECIAL)
			return false;
		return type == EnumCapsuleType.NORMAL ^ effect.isInstant();
	}
	public static ItemStack getDefaultInstance(EnumCapsuleType type) {
		switch (type) {
		case INSTANT:
			return ModItems.CAPSULE_INSTANT.getDefaultInstance();
		case NORMAL:
			return ModItems.CAPSULE.getDefaultInstance();
		case SPECIAL:
			return ModItems.CAPSULE_SPECIAL.getDefaultInstance();
		default:
			return ModItems.CAPSULE.getDefaultInstance();
		}
	}

	private final EnumCapsuleType TYPE;
	private static Set<EffectInstance> effects;

	public ItemCapsule(EnumCapsuleType type) {
		super(Defaults.itemProp.get().maxStackSize(ServerConfigs.capsule_stackSize.get()));
		this.addPropertyOverride(new ResourceLocation("potion"), (stack, world, entity) -> {
			return PotionUtils.getEffectsFromStack(stack).isEmpty() ? 0.0F : 1.0F;
		});
		TYPE = type;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
		if (player == null || !player.abilities.isCreativeMode) {
			stack.shrink(1);
		}

		if (player instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
		}

		if (!worldIn.isRemote) {
			for (EffectInstance effect: PotionUtils.getEffectsFromStack(stack)) {
				entityLiving.addPotionEffect(new EffectInstance(effect));
			}
		}

		if (player != null) {
			player.addStat(Stats.ITEM_USED.get(this));
		}
		return stack;
	}
	
	public ItemStack onItemUseFinishRegardsActiveEffects(ItemStack stack, World world, LivingEntity entityLiving) {
		if (stack.isEmpty())
			return stack;
		
		EnumCapsuleType type = getCapsuleType(stack.getItem());
		boolean shouldApply = (type == EnumCapsuleType.INSTANT || type == EnumCapsuleType.SPECIAL);
		for (EffectInstance effect: PotionUtils.getEffectsFromStack(stack)) {
			if (shouldApply)
				break;
			if (entityLiving.getActivePotionEffect(effect.getPotion()) == null) {
				shouldApply = true;
			}
		}
		
		if (shouldApply) {
			PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
			if (player == null || !player.abilities.isCreativeMode) {
				stack.shrink(1);
				player.sendStatusMessage(new CapsuleUsedTextComponent("potioncapsule.tooltip.capsule.used", stack.getDisplayName()), true);
			}

			if (player instanceof ServerPlayerEntity) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
			}

			if (!world.isRemote) {
				for (EffectInstance effect: PotionUtils.getEffectsFromStack(stack)) {
					entityLiving.addPotionEffect(new EffectInstance(effect));
				}
			}

			if (player != null) {
				player.addStat(Stats.ITEM_USED.get(this));
			}
			return stack;
		}
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 8;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return ClientConfigs.capsule_glowWithEffect.get() || !PotionUtils.getEffectsFromStack(stack).isEmpty();
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		if (ClientConfigs.capsule_effectRenderCount.get() == 0)
			return super.getDisplayName(stack);
		if (!PotionUtils.getEffectsFromStack(stack).isEmpty()) {
			return new TranslationTextComponent(getTranslationKey() + ".with", getDescriptionString(stack));
		}
		return super.getDisplayName(stack);
	}
	
	public String getDescriptionString(ItemStack stack) {
		if (ClientConfigs.capsule_effectRenderCount.get() == 0)
			return "";
		if (stack.isEmpty())
			return new TranslationTextComponent("potioncapsule.tooltip.capsule.empty").getFormattedText();
		List<ITextComponent> components = addPotionTooltipWithoutDuration(stack, new ArrayList<>());
		if (components.size() < 1)
			return new TranslationTextComponent("potioncapsule.tooltip.capsule.empty").getFormattedText();
		
		String jointComponents = components.get(0).getFormattedText();
		for (int i = 1; i < components.size(); i++) {
			if (i == ClientConfigs.capsule_effectRenderCount.get()) {
				jointComponents += ", ...";
				break;
			}
			jointComponents += ", " + components.get(i).getFormattedText();
		}
		return jointComponents;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!PotionUtils.getEffectsFromStack(stack).isEmpty()) {
			if (Screen.hasShiftDown()) {
				PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
			}
			else {
				addPotionTooltipWithLimitedEffectsRendered(stack, tooltip, 1.0F);
			}
		}
		else
			tooltip.add((new TranslationTextComponent("potioncapsule.tooltip.capsule.empty")).applyTextStyle(TextFormatting.GRAY));
	}

	/***
	 * This is a copy from {@link PotionUtils.addPotionTooltip}, with a config condition to hide effect names to prevent the exploding tooltips
	 * 
	 * @param stack
	 * @param txtComponents
	 * @param durationFactor
	 */
	@OnlyIn(Dist.CLIENT)
	private List<ITextComponent> addPotionTooltipWithLimitedEffectsRendered(ItemStack stack, List<ITextComponent> txtComponents, float durationFactor) {
		boolean limitReached = false;
		List<EffectInstance> list = PotionUtils.getEffectsFromStack(stack);
		List<Tuple<String, AttributeModifier>> list1 = new ArrayList<>();
		if (list.isEmpty()) {
			txtComponents.add((new TranslationTextComponent("effect.none")).applyTextStyle(TextFormatting.GRAY));
		} 
		else {
			for (int i = 0 ; i<list.size() ; i++) {
				ITextComponent itextcomponent = new TranslationTextComponent(list.get(i).getEffectName());
				Effect effect = list.get(i).getPotion();
				Map<IAttribute, AttributeModifier> map = effect.getAttributeModifierMap();
				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(list.get(i).getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Tuple<>(entry.getKey().getName(), attributemodifier1));
					}
				}

				if (list.get(i).getAmplifier() > 0) {
					itextcomponent.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + list.get(i).getAmplifier()));
				}

				if (list.get(i).getDuration() > 20) {
					itextcomponent.appendText(" (").appendText(EffectUtils.getPotionDurationString(list.get(i), durationFactor)).appendText(")");
				}
				
				if (i < ClientConfigs.capsule_effectRenderCount.get()) {
					txtComponents.add(itextcomponent.applyTextStyle(effect.getEffectType().getColor()));
				}
				else {
					limitReached = true;
				}
			}
		}
		
		if (limitReached)
			txtComponents.add(new TranslationTextComponent("potioncapsule.tooltip.capsule.shiftdown").applyTextStyle(TextFormatting.GRAY).applyTextStyle(TextFormatting.ITALIC));

		if (!list1.isEmpty()) {
			txtComponents.add(new StringTextComponent(""));
			txtComponents.add((new TranslationTextComponent("potion.whenDrank")).applyTextStyle(TextFormatting.DARK_PURPLE));

			for (Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getB();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} 
				else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					txtComponents.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String) tuple.getA()))).applyTextStyle(TextFormatting.BLUE));
				} 
				else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					txtComponents.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String) tuple.getA()))).applyTextStyle(TextFormatting.RED));
				}
			}
		}
		return txtComponents;
	}

	/***
	 * This is a copy from {@link PotionUtils.addPotionTooltip}, with the duration and the "when used" part removed
	 * 
	 * @param stack
	 * @param txtComponents
	 */
	@OnlyIn(Dist.CLIENT)
	private List<ITextComponent> addPotionTooltipWithoutDuration(ItemStack stack, List<ITextComponent> txtComponents) {
		List<EffectInstance> list = PotionUtils.getEffectsFromStack(stack);
		if (list.isEmpty()) {
			txtComponents.add((new TranslationTextComponent("effect.none")).applyTextStyle(TextFormatting.GRAY));
		} 
		else {
			for (EffectInstance effectinstance : list) {
				ITextComponent itextcomponent = new TranslationTextComponent(effectinstance.getEffectName());
				Effect effect = effectinstance.getPotion();

				if (effectinstance.getAmplifier() > 0) {
					itextcomponent.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				txtComponents.add(itextcomponent.applyTextStyle(effect.getEffectType().getColor()));
			}
		}
		return txtComponents;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			items.add(getDefaultInstance());
			
			if (effects == null) {
				PotionCapsule.Logger.info("Start querying effect instances from existing potion");
				effects = new TreeSet<>(new Comparator<EffectInstance>() {
					@Override
					public int compare(EffectInstance o1, EffectInstance o2) {
						String name1 = o1.getEffectName(), name2 = o2.getEffectName();
						if (name1.equals(name2))
							return o1.getAmplifier() < o2.getAmplifier() ? -1 : (o1.getAmplifier() > o2.getAmplifier() ? 1 : 0);
						return name1.compareTo(name2);
					}
				});
				for (Potion potion: Registry.POTION) {
					for (EffectInstance effect: potion.getEffects()) {
						EffectInstance toadd = new EffectInstance(effect);
						if (!toadd.getPotion().isInstant())
							toadd.duration = ServerConfigs.capsule_capacity.get();
						if (toadd.getPotion() == Effects.NIGHT_VISION && ServerConfigs.misc_replaceNvWithNvnf.get())
							toadd = new EffectInstance(EffectNightVisionNF.INSTANCE, toadd.getDuration(), toadd.getAmplifier(), toadd.isAmbient(), toadd.doesShowParticles(), toadd.isShowIcon());
						if (effects.add(toadd)) {
							PotionCapsule.Logger.info(toadd.getAmplifier() > 0 ? new TranslationTextComponent(toadd.getEffectName()).getFormattedText() + " x " + (toadd.getAmplifier() + 1) : new TranslationTextComponent(toadd.getEffectName()).getFormattedText());
						}
					}
				}
				PotionCapsule.Logger.info("Querying complete");
			}
			
			for (EffectInstance effect: effects) {
				if (canApplyEffectOnType(TYPE, effect.getPotion()))
					items.add(PotionUtils.appendEffects(getDefaultInstance(), Arrays.asList(effect)));
			}
		}
	}
	
	public static enum EnumCapsuleType {
		NORMAL("normal"),
		INSTANT("instant"),
		SPECIAL("special");
		
		public String name;
		EnumCapsuleType(String name) {
			this.name = name;
		}
	}

}
