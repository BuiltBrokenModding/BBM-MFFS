package resonantinduction.mechanical.process.purifier;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import resonantinduction.api.mechanical.IMechanical;
import resonantinduction.api.recipe.MachineRecipes;
import resonantinduction.api.recipe.MachineRecipes.RecipeType;
import resonantinduction.archaic.filter.Timer;
import resonantinduction.core.Reference;
import resonantinduction.core.resource.ResourceGenerator;
import resonantinduction.core.resource.fluid.BlockFluidMixture;
import resonantinduction.mechanical.energy.network.TileMechanical;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.utility.inventory.InventoryUtility;

/**
 * @author Calclavia
 * 
 */
public class TileMixer extends TileMechanical implements IInventory
{
	public static final long POWER = 500000;
	public static final int PROCESS_TIME = 12 * 20;
	public static final Timer<EntityItem> timer = new Timer<EntityItem>();

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (canWork())
		{
			doWork();
		}
	}

	/**
	 * Can this machine work this tick?
	 * 
	 * @return
	 */
	public boolean canWork()
	{
		return angularVelocity != 0;
	}

	public void doWork()
	{
		boolean didWork = false;

		// Search for an item to "process"
		AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 1, this.zCoord + 2);
		List<Entity> entities = this.worldObj.getEntitiesWithinAABB(Entity.class, aabb);
		Set<EntityItem> processItems = new LinkedHashSet<EntityItem>();

		for (Entity entity : entities)
		{
			/**
			 * Rotate entities around the mixer
			 */
			Vector3 originalPosition = new Vector3(entity);
			Vector3 relativePosition = originalPosition.clone().subtract(new Vector3(this).add(0.5));
			relativePosition.rotate(-angularVelocity, 0, 0);
			Vector3 newPosition = new Vector3(this).add(0.5).add(relativePosition);
			Vector3 difference = newPosition.difference(originalPosition).scale(0.5);

			entity.addVelocity(difference.x, difference.y, difference.z);
			entity.onGround = false;

			if (entity instanceof EntityItem)
			{
				if (MachineRecipes.INSTANCE.getOutput(RecipeType.MIXER, ((EntityItem) entity).getEntityItem()).length > 0)
				{
					processItems.add((EntityItem) entity);
				}
			}
		}

		for (EntityItem processingItem : processItems)
		{
			if (!timer.containsKey(processingItem))
			{
				timer.put(processingItem, PROCESS_TIME);
			}

			if (!processingItem.isDead && new Vector3(this).add(0.5).distance(processingItem) < 2)
			{
				int timeLeft = timer.decrease(processingItem);

				if (timeLeft <= 0)
				{
					if (this.doneWork(processingItem))
					{
						if (--processingItem.getEntityItem().stackSize <= 0)
						{
							processingItem.setDead();
							timer.remove(processingItem);
							processingItem = null;
						}
						else
						{
							processingItem.setEntityItemStack(processingItem.getEntityItem());
							// Reset timer
							timer.put(processingItem, PROCESS_TIME);
						}
					}
				}
				else
				{
					processingItem.delayBeforeCanPickup = 20;
					this.worldObj.spawnParticle("bubble", processingItem.posX, processingItem.posY, processingItem.posZ, (Math.random() - 0.5f) * 3, (Math.random() - 0.5f) * 3, (Math.random() - 0.5f) * 3);
				}

				didWork = true;
			}
			else
			{
				timer.remove(processingItem);
				processingItem = null;
			}
		}

		if (didWork)
		{
			if (this.ticks % 20 == 0)
			{
				this.worldObj.playSoundEffect(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, Reference.PREFIX + "mixer", 0.5f, 1);
			}
		}
	}

	private boolean doneWork(EntityItem entity)
	{
		Vector3 mixPosition = new Vector3(entity.posX, yCoord, entity.posZ);
		Block block = Block.blocksList[mixPosition.getBlockID(worldObj)];

		if (block instanceof BlockFluidMixture)
		{
			ItemStack itemStack = entity.getEntityItem().copy();

			if (((BlockFluidMixture) block).mix(worldObj, mixPosition.intX(), mixPosition.intY(), mixPosition.intZ(), itemStack))
			{
				worldObj.notifyBlocksOfNeighborChange(mixPosition.intX(), mixPosition.intY(), mixPosition.intZ(), mixPosition.getBlockID(worldObj));
				return true;
			}
		}
		else if (block != null && (block.blockID == Block.waterStill.blockID || block.blockID == Block.waterMoving.blockID))
		{
			mixPosition.setBlock(worldObj, ResourceGenerator.getMixture(ResourceGenerator.getName(entity.getEntityItem())).blockID);
		}

		return false;
	}

	@Override
	public boolean canConnect(ForgeDirection from, Object source)
	{
		return from == ForgeDirection.UP || from == ForgeDirection.DOWN;
	}

	@Override
	public boolean inverseRotation(ForgeDirection dir, IMechanical with)
	{
		return dir == ForgeDirection.DOWN;
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if (!worldObj.isRemote)
		{
			Vector3 spawnVector = new Vector3(this).translate(ForgeDirection.getOrientation(worldObj.rand.nextInt(4) + 2)).translate(0.5);
			InventoryUtility.dropItemStack(worldObj, spawnVector, itemstack, 20, false);
		}
	}

	@Override
	public String getInvName()
	{
		return null;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return MachineRecipes.INSTANCE.getOutput(RecipeType.MIXER, itemstack).length > 0;
	}
}