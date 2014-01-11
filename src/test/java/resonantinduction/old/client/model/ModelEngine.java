// Date: 8/24/2012 1:44:37 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package resonantinduction.old.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEngine extends ModelBase
{
	// fields
	ModelRenderer Base;
	ModelRenderer top;
	ModelRenderer TopPiston;
	ModelRenderer BottomPiston;
	ModelRenderer center;
	ModelRenderer C1;
	ModelRenderer C2;

	public ModelEngine()
	{
		textureWidth = 64;
		textureHeight = 64;

		Base = new ModelRenderer(this, 0, 20);
		Base.addBox(-6F, 0F, -6F, 12, 8, 12);
		Base.setRotationPoint(0F, 16F, 0F);
		Base.setTextureSize(64, 64);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		top = new ModelRenderer(this, 0, 0);
		top.addBox(-6F, 0F, -6F, 12, 8, 12);
		top.setRotationPoint(0F, -8F, 0F);
		top.setTextureSize(64, 64);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		TopPiston = new ModelRenderer(this, 0, 52);
		TopPiston.addBox(-2F, 0F, -2F, 4, 8, 4);
		TopPiston.setRotationPoint(0F, 0F, 0F);
		TopPiston.setTextureSize(64, 64);
		TopPiston.mirror = true;
		setRotation(TopPiston, 0F, 0F, 0F);
		BottomPiston = new ModelRenderer(this, 16, 52);
		BottomPiston.addBox(-2F, 0F, -2F, 4, 8, 4);
		BottomPiston.setRotationPoint(0F, 8F, 0F);
		BottomPiston.setTextureSize(64, 64);
		BottomPiston.mirror = true;
		setRotation(BottomPiston, 0F, 0F, 0F);
		center = new ModelRenderer(this, 32, 52);
		center.addBox(-3F, 0F, -3F, 6, 6, 6);
		// center.setRotationPoint(0F, 5F, 0F);
		center.setTextureSize(64, 64);
		center.mirror = true;
		setRotation(center, 0F, 0F, 0F);
		C1 = new ModelRenderer(this, 0, 41);
		C1.addBox(-2F, -3F, 0F, 4, 6, 3);
		C1.setRotationPoint(0F, 8F, 3F);
		C1.setTextureSize(64, 64);
		C1.mirror = true;
		setRotation(C1, 0F, 0F, 0F);
		C2 = new ModelRenderer(this, 15, 41);
		C2.addBox(-2F, -3F, -3F, 4, 6, 3);
		C2.setRotationPoint(0F, 8F, -3F);
		C2.setTextureSize(64, 64);
		C2.mirror = true;
		setRotation(C2, 0F, 0F, 0F);
	}

	public void renderBot(float f5)
	{
		Base.render(f5);
		BottomPiston.render(f5);
	}

	public void renderTop(float f5)
	{
		top.render(f5);
		TopPiston.render(f5);
		C1.render(f5);
		C2.render(f5);
	}

	public void renderMid(float f5, float p)
	{

		center.setRotationPoint(0F, p, 0F);
		center.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
