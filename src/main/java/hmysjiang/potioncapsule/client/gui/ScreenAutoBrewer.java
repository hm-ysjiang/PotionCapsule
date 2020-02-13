package hmysjiang.potioncapsule.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import hmysjiang.potioncapsule.PotionCapsule;
import hmysjiang.potioncapsule.blocks.auto_brewer.TileEntityAutoBrewer;
import hmysjiang.potioncapsule.configs.CommonConfigs;
import hmysjiang.potioncapsule.container.ContainerAutoBrewer;
import hmysjiang.potioncapsule.network.PacketHandler;
import hmysjiang.potioncapsule.network.packets.CPacketClearBrewerOutput;
import hmysjiang.potioncapsule.network.packets.CPacketUpdateBrewerMemory;
import hmysjiang.potioncapsule.network.packets.CPacketUpdateBrewerPartition;
import hmysjiang.potioncapsule.utils.Defaults;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class ScreenAutoBrewer extends ContainerScreen<ContainerAutoBrewer> {
	public static final ResourceLocation TEXTURE = Defaults.modPrefix
			.apply("textures/gui/container/auto_brewer.png");

	private final Rectangle2d HOVER_FUEL;
	private final Rectangle2d HOVER_GUNPOWDER;
	private final Rectangle2d HOVER_BREATH;
	private final Rectangle2d HOVER_CATALYST;
	private final Rectangle2d HOVER_WATER;
	private final Rectangle2d HOVER_POTION;
	private final Rectangle2d HOVER_STATUS;

	public ScreenAutoBrewer(ContainerAutoBrewer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		// BrewingStandScreen
		ySize = 224;
		
		// Minus the width by 1 to match the <= right bound
		HOVER_FUEL = new Rectangle2d(50, 121, 19 - 1, 6);
		HOVER_GUNPOWDER = new Rectangle2d(122, 91, 3 - 1, 18);
		HOVER_BREATH = new Rectangle2d(122, 109, 3 - 1, 18);
		HOVER_CATALYST = new Rectangle2d(148, 91, 3 - 1, 18);
		HOVER_WATER = new Rectangle2d(50, 19, 18 - 1, 72);
		HOVER_POTION = new Rectangle2d(105, 46, 16 - 1, 16);
		HOVER_STATUS = new Rectangle2d(9, 113, 16 - 1, 16);
	}
	
	@Override
	protected void init() {
		super.init();
		addButton(new Button(this.guiLeft + 8, this.guiTop + 50, 37, 20, new TranslationTextComponent("potioncapsule.gui.auto_brewer.button.set").getFormattedText(), btn -> {
			PacketHandler.toServer(new CPacketUpdateBrewerMemory(true));
		}));
		addButton(new Button(this.guiLeft + 8, this.guiTop + 71, 37, 20, new TranslationTextComponent("potioncapsule.gui.auto_brewer.button.reset").getFormattedText(), btn -> {
			PacketHandler.toServer(new CPacketUpdateBrewerMemory(false));
		}));
		addButton(new ImageButton(this.guiLeft + 8, this.guiTop + 19, 37, 10, 209, 32, 10, TEXTURE, 256, 256, btn -> {
			container.brewer.updatePartition(ContainerScreen.hasShiftDown() ? 30 : 5);
			PacketHandler.toServer(new CPacketUpdateBrewerPartition(ContainerScreen.hasShiftDown() ? 30 : 5));
		}) {
			@Override
			public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
				super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
				drawCenteredString(minecraft.fontRenderer, "+", this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
			}
		});
		addButton(new ImageButton(this.guiLeft + 8, this.guiTop + 39, 37, 10, 209, 32, 10, TEXTURE, 256, 256, btn -> {
			container.brewer.updatePartition(ContainerScreen.hasShiftDown() ? -30 : -5);
			PacketHandler.toServer(new CPacketUpdateBrewerPartition(ContainerScreen.hasShiftDown() ? -30 : -5));
		}) {
			@Override
			public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
				super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
				drawCenteredString(minecraft.fontRenderer, "-", this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
			}
		});
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (ContainerScreen.hasShiftDown()) {
			int mouseX = (int) p_mouseClicked_1_, mouseY = (int) p_mouseClicked_3_;
			if (HOVER_POTION.contains(mouseX - guiLeft, mouseY - guiTop)) {
				container.brewer.clearPotion();
				PacketHandler.toServer(new CPacketClearBrewerOutput());
			}
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if (HOVER_FUEL.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.fuel", container.brewer.getFuel()).getFormattedText(), mouseX, mouseY);
		if (HOVER_GUNPOWDER.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.gunpowder", container.brewer.getGunpowder()).getFormattedText(), mouseX, mouseY);
		if (HOVER_BREATH.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.breath", container.brewer.getBreath()).getFormattedText(), mouseX, mouseY);
		if (HOVER_CATALYST.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.catalyst", container.brewer.getCatalyst()).getFormattedText(), mouseX, mouseY);
		if (HOVER_WATER.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.water", container.brewer.getWater().getFluidAmount()).getFormattedText(), mouseX, mouseY);
		if (HOVER_POTION.contains(mouseX - guiLeft, mouseY - guiTop))
			renderPotionTooltip(mouseX, mouseY);
		if (HOVER_STATUS.contains(mouseX - guiLeft, mouseY - guiTop))
			renderTooltip(container.brewer.getStatus().getB().getFormattedText(), mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String formattedTitle = title.getFormattedText();
		this.font.drawString(formattedTitle, (float) (this.xSize / 2 - this.font.getStringWidth(formattedTitle) / 2),
				6.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F,
				(float) (this.ySize - 96 + 2), 4210752);
		this.font.drawString(container.brewer.getPartition() + " s", 12F, 30.0F, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		// BG
		this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);
		// Fuel Bar
		float f1 = MathHelper.clamp(((float) container.brewer.getFuel()) / ((float) TileEntityAutoBrewer.FUEL_MAX), 0F, 1F);
		this.blit(guiLeft + 50, guiTop + 122, 186, 29, (int) (18F * f1), 4);
		// Catalyst
		float f2 = MathHelper.clamp(((float) container.brewer.getGunpowder()) / 3F, 0F, 1F);
		int i2 = (int) (f2 * 14F);
		this.blit(guiLeft + 123, guiTop + 93 + (14 - i2), 186, 33 + (14 - i2), 1, i2);
		
		float f3 = MathHelper.clamp(((float) container.brewer.getBreath()) / 3F, 0F, 1F);
		int i3 = (int) (f3 * 14F);
		this.blit(guiLeft + 123, guiTop + 111 + (14 - i3), 203, 33 + (14 - i3), 1, i3);
		
		float f4 = MathHelper.clamp(((float) container.brewer.getCatalyst()) / (float) CommonConfigs.recipe_instantCatalystAllowed.get(), 0F, 1F);
		int i4 = (int) (f4 * 14F);
		this.blit(guiLeft + 149, guiTop + 93 + (14 - i4), 204, 33 + (14 - i4), 1, i4);
		// Brewtime
		float f6 = MathHelper.clamp(((float) (600 - container.brewer.getBrewtime())) / 600F, 0F, 1F);
		int i5 = (600 - container.brewer.getBrewtime()) % 30;
		int i6 = container.brewer.isBrewing() ? (int) (f6 * 85F) : 0;
		this.blit(guiLeft + 53, guiTop + 91 + (29 - i5), 186, 29 - i5, 11, i5);
		this.blit(guiLeft + 70, guiTop + 31, 177, 1, 7, i6);
		// Status
		Tuple<Integer, TranslationTextComponent> status = container.brewer.getStatus();
		switch (status.getA()) {
		case 0:
			this.blit(guiLeft + 9, guiTop + 113, 225, 0, 14, 14);
			break;
		case 1:
			this.blit(guiLeft + 9, guiTop + 113, 239, 0, 14, 14);
			break;
		case 2:
			this.blit(guiLeft + 9, guiTop + 113, 225, 14, 14, 14);
			break;
		}
		// Water tank
		renderFluid();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(TEXTURE);
		this.blit(guiLeft + 51, guiTop + 20, 187, 33, 16, 70);
		// Potion Output
		renderPotion();
		
		// Memory
		if (container.brewer.isMemMode())
			renderMemory();
	}
	
	protected void renderFluid() {
		FluidTank water = container.brewer.getWater();
		float f = MathHelper.clamp(((float) water.getFluidAmount()) / ((float) 4000), 0F, 1F);
		int pixels = (int) (70F * f);
		drawWater(this.guiLeft + 51, this.guiTop + 20 + (70 - pixels), 16, pixels, Fluids.WATER.getAttributes().getColor());
	}
	
	protected void renderPotion() {
		List<EffectInstance> potion = container.brewer.getPotion();
		if (potion != null && !potion.isEmpty())
			drawWater(this.guiLeft + 105, this.guiTop + 46, 16, 16, PotionUtils.getPotionColorFromEffectList(potion));
	}
	
	private void drawWater(int x, int y, int width, int height, int color) {
		GlStateManager.color4f((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = ForgeHooksClient.getFluidSprites(PotionCapsule.proxy.getWorld(), new BlockPos(0, 0, 0), Fluids.WATER.getDefaultState())[0];
		for (int i = 0 ; i<width ; i += 16) {
			for (int j = 0 ; j<height ; j += 16) {
				blit(x + i, y + j, blitOffset, (i + 16 > width ? width - i : 16), (j + 16 > height ? height - j : 16), sprite);
			}
		}
	}
	
	protected void renderMemory() {
		ItemStackHandler memory = container.brewer.getMemory();
		ItemStackHandler inventory = container.inventory;
		
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) guiLeft, (float) guiTop, 0.0F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
        blitOffset = 100;
        itemRenderer.zLevel = 100.0F;
        GlStateManager.enableDepthTest();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        for (int i = 0 ; i<6 ; i++) {
        	ItemStack stack = memory.getStackInSlot(i);
        	if (!stack.isEmpty())
        		itemRenderer.renderItemAndEffectIntoGUI(stack, 80, 20 + 18 * i);
    		GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableDepthTest();
            minecraft.getTextureManager().bindTexture(TEXTURE);
            if (stack.isEmpty())
            	this.blit(80, 20 + 18 * i, 209, 0, 16, 16);
            else if (inventory.getStackInSlot(i + 1).isEmpty()) {
            	GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.65F);
            	this.blit(80, 20 + 18 * i, 209, 16, 16, 16);
            	GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            GlStateManager.enableDepthTest();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
        }
        blitOffset = 0;
        itemRenderer.zLevel = 0.0F;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
	}

	private void renderPotionTooltip(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<>();
		tooltip.add(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.output").getFormattedText());
		if (container.brewer.getPotion() == null || container.brewer.getPotion().isEmpty())
			tooltip.add(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.output.empty").applyTextStyles(TextFormatting.GRAY, TextFormatting.ITALIC).getFormattedText());
		else {
			tooltip.addAll(container.brewer.getPotion().stream().map(effect -> {
				ITextComponent text = new TranslationTextComponent(effect.getEffectName());
				if (effect.getAmplifier() > 0)
					text.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effect.getAmplifier()));
				if (!effect.getPotion().isInstant()) {
					float s = ((float) effect.getDuration()) / 20F;
					text.appendText(" (" + s + "s)");
				}
				text.applyTextStyle(effect.getPotion().getEffectType().getColor());
				return text.getFormattedText();
			}).collect(Collectors.toList()));
			tooltip.add(new TranslationTextComponent("potioncapsule.gui.auto_brewer.tooltip.output.clear").applyTextStyle(TextFormatting.ITALIC).getFormattedText());
		}
		renderTooltip(tooltip, mouseX, mouseY);
	}

}
