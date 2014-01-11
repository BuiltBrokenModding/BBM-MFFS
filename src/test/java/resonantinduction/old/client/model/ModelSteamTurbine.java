// Date: 11/26/2013 1:36:31 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package resonantinduction.old.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelSteamTurbine extends ModelMachine
{
	// fields
	ModelRenderer base;
	ModelRenderer base2;
	ModelRenderer centerBeam;
	ModelRenderer fan1;
	ModelRenderer fan2;
	ModelRenderer fan3;
	ModelRenderer fan4;
	ModelRenderer fan5;
	ModelRenderer fan6;
	ModelRenderer fan7;
	ModelRenderer fan8;
	ModelRenderer rightSide;
	ModelRenderer leftSide;
	ModelRenderer backSide;
	ModelRenderer motor;
	ModelRenderer motorNeck;
	ModelRenderer brace;
	ModelRenderer gauge;
	ModelRenderer gaugeFrame;
	ModelRenderer gaugeFrame2;
	ModelRenderer gaugeFrame3;
	ModelRenderer gaugeDieal;

	public ModelSteamTurbine()
	{
		textureWidth = 256;
		textureHeight = 256;

		base = new ModelRenderer(this, 120, 40);
		base.addBox(-8F, 0F, -8F, 16, 1, 16);
		base.setRotationPoint(0F, 23F, 0F);
		base.setTextureSize(256, 256);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		base2 = new ModelRenderer(this, 120, 22);
		base2.addBox(-7F, -1F, -7F, 14, 1, 14);
		base2.setRotationPoint(0F, 23F, 0F);
		base2.setTextureSize(256, 256);
		base2.mirror = true;
		setRotation(base2, 0F, 0F, 0F);
		centerBeam = new ModelRenderer(this, 0, 80);
		centerBeam.addBox(-2F, -1F, -2F, 4, 7, 4);
		centerBeam.setRotationPoint(0F, 16F, 0F);
		centerBeam.setTextureSize(256, 256);
		centerBeam.mirror = true;
		setRotation(centerBeam, 0F, 0F, 0F);
		fan1 = new ModelRenderer(this, 0, 100);
		fan1.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan1.setRotationPoint(0F, 16F, 0F);
		fan1.setTextureSize(256, 256);
		fan1.mirror = true;
		setRotation(fan1, 0F, 0F, 0F);
		fan2 = new ModelRenderer(this, 0, 100);
		fan2.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan2.setRotationPoint(0F, 16F, 0F);
		fan2.setTextureSize(256, 256);
		fan2.mirror = true;
		setRotation(fan2, 0F, 0.7853982F, 0F);
		fan3 = new ModelRenderer(this, 0, 100);
		fan3.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan3.setRotationPoint(0F, 16F, 0F);
		fan3.setTextureSize(256, 256);
		fan3.mirror = true;
		setRotation(fan3, 0F, 1.570796F, 0F);
		fan4 = new ModelRenderer(this, 0, 100);
		fan4.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan4.setRotationPoint(0F, 16F, 0F);
		fan4.setTextureSize(256, 256);
		fan4.mirror = true;
		setRotation(fan4, 0F, 2.356194F, 0F);
		fan5 = new ModelRenderer(this, 0, 100);
		fan5.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan5.setRotationPoint(0F, 16F, 0F);
		fan5.setTextureSize(256, 256);
		fan5.mirror = true;
		setRotation(fan5, 0F, 3.141593F, 0F);
		fan6 = new ModelRenderer(this, 0, 100);
		fan6.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan6.setRotationPoint(0F, 16F, 0F);
		fan6.setTextureSize(256, 256);
		fan6.mirror = true;
		setRotation(fan6, 0F, -2.356194F, 0F);
		fan7 = new ModelRenderer(this, 0, 100);
		fan7.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan7.setRotationPoint(0F, 16F, 0F);
		fan7.setTextureSize(256, 256);
		fan7.mirror = true;
		setRotation(fan7, 0F, -1.570796F, 0F);
		fan8 = new ModelRenderer(this, 0, 100);
		fan8.addBox(-0.5F, -1F, -6F, 1, 7, 4);
		fan8.setRotationPoint(0F, 16F, 0F);
		fan8.setTextureSize(256, 256);
		fan8.mirror = true;
		setRotation(fan8, 0F, -0.7853982F, 0F);
		rightSide = new ModelRenderer(this, 124, 0);
		rightSide.addBox(-8F, 0F, -3F, 2, 10, 9);
		rightSide.setRotationPoint(0F, 13F, 0F);
		rightSide.setTextureSize(256, 256);
		rightSide.mirror = true;
		setRotation(rightSide, 0F, 0F, 0F);
		leftSide = new ModelRenderer(this, 150, 0);
		leftSide.addBox(6F, 0F, -3F, 2, 10, 9);
		leftSide.setRotationPoint(0F, 13F, 0F);
		leftSide.setTextureSize(256, 256);
		leftSide.mirror = true;
		setRotation(leftSide, 0F, 0F, 0F);
		backSide = new ModelRenderer(this, 120, 111);
		backSide.addBox(-8F, 0F, 6F, 16, 15, 2);
		backSide.setRotationPoint(0F, 8F, 0F);
		backSide.setTextureSize(256, 256);
		backSide.mirror = true;
		setRotation(backSide, 0F, 0F, 0F);
		motor = new ModelRenderer(this, 0, 53);
		motor.addBox(-3F, -1F, -3F, 6, 7, 7);
		motor.setRotationPoint(0F, 8F, 0F);
		motor.setTextureSize(256, 256);
		motor.mirror = true;
		setRotation(motor, 0F, 0F, 0F);
		motorNeck = new ModelRenderer(this, 0, 71);
		motorNeck.addBox(-2F, -1F, -2F, 4, 1, 4);
		motorNeck.setRotationPoint(0F, 15F, 0F);
		motorNeck.setTextureSize(256, 256);
		motorNeck.mirror = true;
		setRotation(motorNeck, 0F, 0F, 0F);
		brace = new ModelRenderer(this, 120, 96);
		brace.addBox(0F, 0F, 0F, 14, 5, 5);
		brace.setRotationPoint(-7F, 9F, 1F);
		brace.setTextureSize(256, 256);
		brace.mirror = true;
		setRotation(brace, 0F, 0F, 0F);
		gauge = new ModelRenderer(this, 0, 17);
		gauge.addBox(0F, 0F, 0F, 5, 5, 2);
		gauge.setRotationPoint(-8F, 8F, 0F);
		gauge.setTextureSize(256, 256);
		gauge.mirror = true;
		setRotation(gauge, 0F, 0F, 0F);
		gaugeFrame = new ModelRenderer(this, 0, 0);
		gaugeFrame.addBox(0F, 0F, 0F, 5, 1, 1);
		gaugeFrame.setRotationPoint(-8F, 8F, -0.2F);
		gaugeFrame.setTextureSize(256, 256);
		gaugeFrame.mirror = true;
		setRotation(gaugeFrame, 0F, 0F, 0F);
		gaugeFrame2 = new ModelRenderer(this, 0, 0);
		gaugeFrame2.addBox(0F, 0F, 0F, 4, 1, 1);
		gaugeFrame2.setRotationPoint(-7F, 12F, -0.2F);
		gaugeFrame2.setTextureSize(256, 256);
		gaugeFrame2.mirror = true;
		setRotation(gaugeFrame2, 0F, 0F, 0F);
		gaugeFrame3 = new ModelRenderer(this, 0, 0);
		gaugeFrame3.addBox(0F, 0F, 0F, 1, 4, 1);
		gaugeFrame3.setRotationPoint(-8F, 9F, -0.2F);
		gaugeFrame3.setTextureSize(256, 256);
		gaugeFrame3.mirror = true;
		setRotation(gaugeFrame3, 0F, 0F, 0F);
		gaugeDieal = new ModelRenderer(this, 0, 28);
		gaugeDieal.addBox(-1F, -0.5F, 0F, 3, 1, 0);
		gaugeDieal.setRotationPoint(-5F, 12F, -0.2F);
		gaugeDieal.setTextureSize(256, 256);
		gaugeDieal.mirror = true;
		setRotation(gaugeDieal, 0F, 0F, -2.80998F);
	}

	@Override
	public void render(float f5)
	{
		base.render(f5);
		base2.render(f5);
		centerBeam.render(f5);
		rightSide.render(f5);
		leftSide.render(f5);
		backSide.render(f5);
		motor.render(f5);
		motorNeck.render(f5);
		brace.render(f5);
		gauge.render(f5);
		gaugeFrame.render(f5);
		gaugeFrame2.render(f5);
		gaugeFrame3.render(f5);
		gaugeDieal.render(f5);
	}

	public void renderFan(float f5)
	{
		fan1.render(f5);
		fan2.render(f5);
		fan3.render(f5);
		fan4.render(f5);
		fan5.render(f5);
		fan6.render(f5);
		fan7.render(f5);
		fan8.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
