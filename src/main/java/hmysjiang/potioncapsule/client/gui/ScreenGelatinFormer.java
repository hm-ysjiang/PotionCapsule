package hmysjiang.potioncapsule.client.gui;

import java.util.Arrays;

import com.mojang.blaze3d.platform.GlStateManager;

import hmysjiang.potioncapsule.container.ContainerGelatinFormer;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenGelatinFormer extends ContainerScreen<ContainerGelatinFormer> {
	public static final ResourceLocation TEXTURE = Defaults.modPrefix
			.apply("textures/gui/container/gelatin_former.png");

	public ScreenGelatinFormer(ContainerGelatinFormer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		ySize = 166;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if (mouseX >= this.guiLeft + 6 && mouseX < this.guiLeft + 18 && mouseY >= this.guiTop + 37
				&& mouseY < this.guiTop + 55) {
			int per = (int) (container.getTile().getWorkedPercnetage() * 100);
			if (!container.getTile().isWorking()) {
				renderTooltip(
						Arrays.asList(
								new TranslationTextComponent("potioncapsule.gui.gelatin_form.progress_percentage",
										String.valueOf(per), "%").getFormattedText(),
								new TranslationTextComponent("potioncapsule.gui.gelatin_form.zero_descr").getFormattedText()), mouseX, mouseY);
			}
			else {
				renderTooltip(new TranslationTextComponent("potioncapsule.gui.gelatin_form.progress_percentage",
						String.valueOf(per), "%").getFormattedText(), mouseX, mouseY);
			}
		}
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
		if (container.getTile().isWorking()) {
			this.blit(guiLeft + 76, guiTop + 35, 176, 17, container.getTile().getWorkedPercentagePixel(), 17);
		} else {
			this.blit(guiLeft + 76, guiTop + 35, 176, 0, 24, 17);
		}
	}

}
