package hmysjiang.potioncapsule.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import hmysjiang.potioncapsule.client.KeyBindHandler;
import hmysjiang.potioncapsule.client.gui.widgets.PairedImageButtonSwitch;
import hmysjiang.potioncapsule.container.ContainerPendant;
import hmysjiang.potioncapsule.items.ItemCapsulePendant;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.CPacketUpdatePendantStatus;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenPendant extends ContainerScreen<ContainerPendant> {
	private static final ResourceLocation TEXTURE = Defaults.modPrefix.apply("textures/gui/container/pendant.png");
	
	private int mask; 
	
	public ScreenPendant(ContainerPendant screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		ySize = 246;
		mask = screenContainer.getStack().getOrCreateTag().getInt("StatusMask");
	}
	
	@Override
	protected void init() {
		super.init();
		addButton(new PairedImageButtonSwitch(guiLeft + 75, guiTop + 17, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 0));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 0));
				}, mask & (1 << 0), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 35, guiTop + 33, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 1));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 1));
				}, mask & (1 << 1), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 115, guiTop + 33, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 2));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 2));
				}, mask & (1 << 2), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 15, guiTop + 68, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 3));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 3));
				}, mask & (1 << 3), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 135, guiTop + 68, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 4));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 4));
				}, mask & (1 << 4), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 35, guiTop + 103, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 5));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 5));
				}, mask & (1 << 5), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 115, guiTop + 103, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 6));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 6));
				}, mask & (1 << 6), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 75, guiTop + 119, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 7));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 7));
				}, mask & (1 << 7), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));

		addButton(new PairedImageButtonSwitch(guiLeft + 115, guiTop + 138, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 8));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 8));
				}, mask & (1 << 8), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 133, guiTop + 138, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 9));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 9));
				}, mask & (1 << 9), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
		addButton(new PairedImageButtonSwitch(guiLeft + 151, guiTop + 138, 4, 4, 180, 0, 176, 0, 5, TEXTURE, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 10));
				}, 
				btn -> {
					PacketHandler.getInstacne().sendToServer(new CPacketUpdatePendantStatus(1 << 10));
				}, mask & (1 << 10), 
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_off").applyTextStyle(TextFormatting.RED)),
				new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status").appendSibling(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.status_on").applyTextStyle(TextFormatting.GREEN))));
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
		this.font.drawString(new TranslationTextComponent("potioncapsule.gui.pendant.tooltip.specialslot").getFormattedText(), 106.0F, (float)(this.ySize - 120 + 2), 4210752);
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
		for (Widget wg: buttons)
			if (wg.isHovered()){
				ITextComponent text = ((PairedImageButtonSwitch) wg).getTooltip();
				if (text != null) {
					this.renderTooltip(text.getFormattedText(), mouseX, mouseY);
				}
			}
	}
	
}
