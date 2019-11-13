package hmysjiang.potioncapsule.items;

import java.util.List;

import hmysjiang.potioncapsule.Reference.ItemRegs;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSpecialCapsule extends Item {
	
	public final EnumSpecialType type;
	
	public ItemSpecialCapsule(EnumSpecialType type) {
		super(Defaults.itemProp.get());
		this.type = type;
		setRegistryName(Defaults.modPrefix.apply(ItemRegs.SPECIAL_CAPSULE + type.getPost()));
	}
	
	public static enum EnumSpecialType implements IStringSerializable {
		BITEZDUST("bite_za_dust", 0xcf4afc, "Bite The Dust", TextFormatting.LIGHT_PURPLE);
		
		public String name;
		public int color;
		private String desc;
		private TextFormatting[] format;
		
		private EnumSpecialType(String name, int color, String desc, TextFormatting... formats) {
			this.name = name;
			this.color = color;
			this.desc = desc;
			this.format = formats;
		}
		
		public String getPost() {
			return "_" + this.name;
		}
		
		public int getColor() {
			return this.color;
		}
		
		public String getDescription() {
			return this.desc;
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
		switch (((ItemSpecialCapsule) stack.getItem()).type) {
		case BITEZDUST:
			tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.special_capsule_bite_za_dust").applyTextStyle(TextFormatting.GRAY));
			break;
		default:
			break;
		}
	}
	
	@Override
	public String getTranslationKey() {
		return "item.potioncapsule.item_special_capsule";
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return new TranslationTextComponent(getTranslationKey(), new StringTextComponent(type.getDescription()).applyTextStyles(type.getFormat())).applyTextStyle(TextFormatting.GOLD);
	}

}
