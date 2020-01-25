package hmysjiang.potioncapsule.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import hmysjiang.potioncapsule.container.ContainerSpecialCapsuleRepairer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenSpecialCapsuleRepairer extends ContainerScreen<ContainerSpecialCapsuleRepairer> {
	public static final ResourceLocation TEXTURE = Defaults.modPrefix
			.apply("textures/gui/container/special_repair.png");

	public ScreenSpecialCapsuleRepairer(ContainerSpecialCapsuleRepairer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		ySize = 158;
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
		this.font.drawString(formattedTitle, (float) (this.xSize / 2 - this.font.getStringWidth(formattedTitle) / 2),
				6.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F,
				(float) (this.ySize - 96 + 2), 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (!((ContainerSpecialCapsuleRepairer) container).match())
			this.blit(guiLeft + 107, guiTop + 37, 176, 0, 24, 19);
	}

}
