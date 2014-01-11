// Date: 11/13/2013 7:19:55 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package resonantinduction.old.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.ForgeDirection;

public class ModelAdvancedHopper extends ModelBase
{
	// fields
	ModelRenderer bottomConnection;
	ModelRenderer center;
	ModelRenderer top;
	ModelRenderer top2;
	ModelRenderer top3;
	ModelRenderer top4;
	ModelRenderer top5;
	ModelRenderer frontConnection;
	ModelRenderer backConnection;
	ModelRenderer leftConnection;
	ModelRenderer rightConnection;

	public ModelAdvancedHopper()
	{
		textureWidth = 128;
		textureHeight = 128;

		bottomConnection = new ModelRenderer(this, 18, 0);
		bottomConnection.addBox(-2F, 0F, -2F, 4, 4, 4);
		bottomConnection.setRotationPoint(0F, 20F, 0F);
		bottomConnection.setTextureSize(128, 128);
		bottomConnection.mirror = true;
		setRotation(bottomConnection, 0F, 0.7853982F, 0F);
		center = new ModelRenderer(this, 45, 0);
		center.addBox(-4F, 0F, -4F, 8, 6, 8);
		center.setRotationPoint(0F, 14F, 0F);
		center.setTextureSize(128, 128);
		center.mirror = true;
		setRotation(center, 0F, 0F, 0F);
		top = new ModelRenderer(this, 0, 33);
		top.addBox(-8F, -4F, -8F, 16, 6, 2);
		top.setRotationPoint(0F, 12F, 0F);
		top.setTextureSize(128, 128);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		top2 = new ModelRenderer(this, 0, 62);
		top2.addBox(-8F, -4F, 6F, 16, 6, 2);
		top2.setRotationPoint(0F, 12F, 0F);
		top2.setTextureSize(128, 128);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);
		top3 = new ModelRenderer(this, 0, 42);
		top3.addBox(-8F, -4F, -6F, 2, 6, 12);
		top3.setRotationPoint(0F, 12F, 0F);
		top3.setTextureSize(128, 128);
		top3.mirror = true;
		setRotation(top3, 0F, 0F, 0F);
		top4 = new ModelRenderer(this, 30, 42);
		top4.addBox(6F, -4F, -6F, 2, 6, 12);
		top4.setRotationPoint(0F, 12F, 0F);
		top4.setTextureSize(128, 128);
		top4.mirror = true;
		setRotation(top4, 0F, 0F, 0F);
		top5 = new ModelRenderer(this, 61, 44);
		top5.addBox(-6F, 0F, -6F, 12, 1, 12);
		top5.setRotationPoint(0F, 13F, 0F);
		top5.setTextureSize(128, 128);
		top5.mirror = true;
		setRotation(top5, 0F, 0F, 0F);
		frontConnection = new ModelRenderer(this, 0, 0);
		frontConnection.addBox(-2F, -2F, -4F, 4, 5, 4);
		frontConnection.setRotationPoint(0F, 16F, -4F);
		frontConnection.setTextureSize(128, 128);
		frontConnection.mirror = true;
		setRotation(frontConnection, 0F, 0F, 0F);
		backConnection = new ModelRenderer(this, 0, 0);
		backConnection.addBox(-2F, -2F, 0F, 4, 5, 4);
		backConnection.setRotationPoint(0F, 16F, 4F);
		backConnection.setTextureSize(128, 128);
		backConnection.mirror = true;
		setRotation(backConnection, 0F, 0F, 0F);
		leftConnection = new ModelRenderer(this, 0, 0);
		leftConnection.addBox(0F, -2F, -2F, 4, 5, 4);
		leftConnection.setRotationPoint(4F, 16F, 0F);
		leftConnection.setTextureSize(128, 128);
		leftConnection.mirror = true;
		setRotation(leftConnection, 0F, 0F, 0F);
		rightConnection = new ModelRenderer(this, 0, 0);
		rightConnection.addBox(-4F, -3F, -2F, 4, 5, 4);
		rightConnection.setRotationPoint(-4F, 17F, 0F);
		rightConnection.setTextureSize(128, 128);
		rightConnection.mirror = true;
		setRotation(rightConnection, 0F, 0F, 0F);
	}

	public void render(float f5)
	{
		center.render(f5);
		top.render(f5);
		top2.render(f5);
		top3.render(f5);
		top4.render(f5);
		top5.render(f5);
	}

	public void render(float f5, ForgeDirection side)
	{
		switch (side)
		{
			case NORTH:
				frontConnection.render(f5);
				break;
			case SOUTH:
				backConnection.render(f5);
				break;
			case EAST:
				leftConnection.render(f5);
				break;
			case WEST:
				rightConnection.render(f5);
				break;
			case DOWN:
				bottomConnection.render(f5);
				break;
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
