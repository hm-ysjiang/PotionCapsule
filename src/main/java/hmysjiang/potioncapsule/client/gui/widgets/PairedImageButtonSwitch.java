package hmysjiang.potioncapsule.client.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PairedImageButtonSwitch extends ImageButton {
	
	private final int imgPosX_F, imgPosY_F, imgPosX_T, imgPosY_T, yDiff;
	private final ResourceLocation TEXTURE;
	private final IPressable action_F, action_T;
	private boolean flag;
	private ITextComponent hover_F, hover_T;
	
	public PairedImageButtonSwitch(int xIn, int yIn, int widthIn, int heightIn, int imgPosX_F, int imgPosY_F, int imgPosX_T, int imgPosY_T, int yDiff, ResourceLocation texture, IPressable action_F, IPressable action_T) {
		this(xIn, yIn, widthIn, heightIn, imgPosX_F, imgPosY_F, imgPosX_T, imgPosY_T, yDiff, texture, action_F, action_T, false);
	}
	
	public PairedImageButtonSwitch(int xIn, int yIn, int widthIn, int heightIn, int imgPosX_F, int imgPosY_F, int imgPosX_T, int imgPosY_T, int yDiff, ResourceLocation texture, IPressable action_F, IPressable action_T, int flagmask) {
		this(xIn, yIn, widthIn, heightIn, imgPosX_F, imgPosY_F, imgPosX_T, imgPosY_T, yDiff, texture, action_F, action_T, flagmask > 0);
	}
	
	public PairedImageButtonSwitch(int xIn, int yIn, int widthIn, int heightIn, int imgPosX_F, int imgPosY_F, int imgPosX_T, int imgPosY_T, int yDiff, ResourceLocation texture, IPressable action_F, IPressable action_T, int flagmask, ITextComponent hoverText_F, ITextComponent hoverText_T) {
		this(xIn, yIn, widthIn, heightIn, imgPosX_F, imgPosY_F, imgPosX_T, imgPosY_T, yDiff, texture, action_F, action_T, flagmask > 0, hoverText_F, hoverText_T);
	}
	
	public PairedImageButtonSwitch(int xIn, int yIn, int widthIn, int heightIn, int imgPosX_F, int imgPosY_F, int imgPosX_T, int imgPosY_T, int yDiff, ResourceLocation texture, IPressable action_F, IPressable action_T, boolean flag) {
		this(xIn, yIn, widthIn, heightIn, imgPosX_F, imgPosY_F, imgPosX_T, imgPosY_T, yDiff, texture, action_F, action_T, flag, null, null);
	}
	
	public PairedImageButtonSwitch(int xIn, int yIn, int widthIn, int heightIn, int imgPosX_F, int imgPosY_F, int imgPosX_T, int imgPosY_T, int yDiff, ResourceLocation texture, IPressable action_F, IPressable action_T, boolean flag, ITextComponent hoverText_F, ITextComponent hoverText_T) {
		super(xIn, yIn, widthIn, heightIn, imgPosX_F, imgPosY_F, yDiff, texture, action_F);
		this.imgPosX_F = imgPosX_F;
		this.imgPosX_T = imgPosX_T;
		this.imgPosY_F = imgPosY_F;
		this.imgPosY_T = imgPosY_T;
		this.yDiff = yDiff;
		this.TEXTURE = texture;
		this.action_F = action_F;
		this.action_T = action_T;
		this.flag = flag;
		hover_F = hoverText_F;
		hover_T = hoverText_T;
	}
	
	@Override
	public void onPress() {
		if (flag)
			this.action_T.onPress(this);
		else
			this.action_F.onPress(this);
		
		flag = !flag;
	}
	
	@Override
	public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(this.TEXTURE);
		GlStateManager.disableDepthTest();
		int i = this.flag ? this.imgPosY_T : imgPosY_F;
		if (this.isHovered()) {
			i += this.yDiff;
		}

		blit(this.x, this.y, (float) (this.flag ? this.imgPosX_T : this.imgPosX_F), (float) i, this.width, this.height, 256, 256);
		GlStateManager.enableDepthTest();
	}
	
	public ITextComponent getTooltip() {
		return flag ? hover_T : hover_F;
	}
	
}
