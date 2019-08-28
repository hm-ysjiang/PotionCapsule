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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		if (this.hoveredSlot != null) {
			if (this.hoveredSlot.slotNumber < 8) {
				net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(this.hoveredSlot.getStack());
				List<String> tooltips = this.hoveredSlot.getHasStack() ? this.getTooltipFromItem(this.hoveredSlot.getStack()) : new ArrayList<>();
				if (this.hoveredSlot.slotNumber < 8) {
					tooltips.add(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.slotdescr_"
							+ ItemCapsulePendant.CapsuleSlots.fromIndex(this.hoveredSlot.slotNumber))
									.applyTextStyle(TextFormatting.GOLD).getFormattedText());
					if (this.hoveredSlot.slotNumber == 7) {
						String key = KeyBindHandler.keybindings.get(0).getLocalizedName();
						key = key.substring(0, 1).toUpperCase() + key.substring(1);
						tooltips.add(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.keyhint", key).applyTextStyle(TextFormatting.AQUA).getFormattedText());
					}
				}
				this.renderTooltip(tooltips, mouseX, mouseY, font);
				net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
			}
			else
				super.renderHoveredToolTip(mouseX, mouseY);
		}
	}
	
}
