package hmysjiang.potioncapsule.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import hmysjiang.potioncapsule.client.KeyBindHandler;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenPendant extends ContainerScreen<ContainerPendant> {
	private static final ResourceLocation TEXTURE = Defaults.modPrefix.apply("textures/gui/container/pendant.png");
	
	public ScreenPendant(ContainerPendant screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		ySize = 228;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);		
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String formattedTitle = title.getFormattedText();
		this.font.drawString(formattedTitle, (float) (this.xSize / 2 - this.font.getStringWidth(formattedTitle) / 2), 6.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null
				&& (this.hoveredSlot.getHasStack() || this.hoveredSlot.slotNumber < 8)) {
			this.renderTooltip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
		}
	}
	
	@Override
	public List<String> getTooltipFromItem(ItemStack stack) {
		List<String> tooltip = new ArrayList<>();
		if (container.getHandler() != null) {
			for (ItemCapsulePendant.CapsuleSlots slotType: ItemCapsulePendant.CapsuleSlots.values()) {
				if (container.getHandler().getStackInSlot(slotType.getIndex()) == stack) {
					if (!stack.isEmpty()) {
						for (String str: super.getTooltipFromItem(stack))
							tooltip.add(str);
					}
					tooltip.add(new TranslationTextComponent("potioncapsule.tooltip.pendant.gui.slotdescr_" + slotType.name())
							.applyTextStyle(TextFormatting.GOLD).getFormattedText()); 
					if (stack == container.getHandler().getStackInSlot(7)) {
						String key = KeyBindHandler.keybindings.get(0).getLocalizedName();
						key = key.substring(0, 1).toUpperCase() + key.substring(1);
						tooltip.add((new TranslationTextComponent("potioncapsule.tooltip.pendant.gui.keyhint")
								.appendSibling(new StringTextComponent(key).applyTextStyle(TextFormatting.AQUA))).getFormattedText());
					}
					break;
				}
			}
		}
		return tooltip;
	}
	
}
