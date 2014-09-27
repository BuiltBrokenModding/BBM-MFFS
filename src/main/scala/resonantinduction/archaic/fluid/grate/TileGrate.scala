package resonantinduction.archaic.fluid.grate

import java.util.{Collections, Comparator, HashMap, PriorityQueue}

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{ChatComponentText, IIcon}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidContainerRegistry, FluidRegistry, FluidStack}
import resonant.api.IRotatable
import resonant.lib.config.Config
import resonant.lib.utility.FluidUtility
import resonantinduction.archaic.fluid.grate.TileGrate._
import resonantinduction.core.Reference
import resonantinduction.core.prefab.node.TilePressureNode
import universalelectricity.core.transform.vector.Vector3

object TileGrate
{

  @Config(comment = "The multiplier for the influence of the grate. Dependent on pressure.")
  private var grateEffectMultiplier: Double = 5

  @Config(comment = "The speed in which the grate drains blocks. Dependent on grate block influence.")
  private var grateDrainSpeedMultiplier: Double = 0.01

  @SideOnly(Side.CLIENT)
  private var iconFront: IIcon = _

  @SideOnly(Side.CLIENT)
  private var iconSide: IIcon = _

  class ComparableVector(var position: Vector3, var iterations: Int) extends Comparable[Vector3]
  {

    override def compareTo(obj: Vector3): Int =
    {
      val wr = obj.asInstanceOf[ComparableVector]
      if (this.position.y == wr.position.y)
      {
        return this.iterations - wr.iterations
      }
      this.position.yi - wr.position.yi
    }
  }

}

class TileGrate extends TilePressureNode(Material.rock) with IRotatable
{

  private var gratePath: GratePathfinder = _
  private var fillOver: Boolean = true
  isOpaqueCube = false
  normalRender = true

  override def getIcon(world: IBlockAccess, side: Int): IIcon =
  {
    if (side == getDirection.ordinal()) iconFront else iconSide
  }

  override def getIcon(side: Int, metadata: Int): IIcon = if (side == 1) iconFront else iconSide

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister)
  {
    iconFront = iconRegister.registerIcon(Reference.prefix + "grate_front")
    iconSide = iconRegister.registerIcon(Reference.prefix + "grate")
  }

  override def readFromNBT(nbt: NBTTagCompound)
  {
    super.readFromNBT(nbt)
    fillOver = nbt.getBoolean("fillOver")
  }

  override def writeToNBT(nbt: NBTTagCompound)
  {
    super.writeToNBT(nbt)
    nbt.setBoolean("fillOver", fillOver)
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = getDirection != from

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = getDirection != from

  override def update()
  {
    super.update()
    if (!world.isRemote)
    {
      if (ticks % 10 == 0)
      {
        val pressure = getPressure(getDirection)
        val blockEffect = Math.abs(pressure * grateEffectMultiplier).toInt
        setCapacity(Math.max(blockEffect * FluidContainerRegistry.BUCKET_VOLUME * grateDrainSpeedMultiplier,
                             FluidContainerRegistry.BUCKET_VOLUME).toInt)
        if (pressure > 0)
        {
          if (getFluidAmount >= FluidContainerRegistry.BUCKET_VOLUME)
          {
            if (gratePath == null)
            {
              gratePath = new GratePathfinder(true)
              gratePath.startFill(new Vector3(this), getTank().getFluid.getFluid.getID)
            }
            val filledInWorld = gratePath.tryFill(getFluidAmount, blockEffect)
            getTank().drain(filledInWorld, true)
          }
        }
        else if (pressure < 0)
        {
          val maxDrain = getTank().getCapacity - getFluidAmount
          if (maxDrain > 0)
          {
            if (gratePath == null)
            {
              gratePath = new GratePathfinder(false)
              if (!gratePath.startDrain(new Vector3(this)))
              {
                resetPath()
              }
            }
            if (gratePath != null && gratePath.tryPopulateDrainMap(blockEffect))
            {
              getTank().fill(gratePath.tryDrain(maxDrain, true), true)
            }
          }
        }
      }
    }
  }

  def resetPath()
  {
    this.gratePath = null
  }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = getTank().fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    if (resource != null)
    {
      return drain(from, resource.amount, doDrain)
    }
    null
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    getTank().drain(maxDrain, doDrain)
  }

  protected override def configure(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    if (!player.isSneaking)
    {
      if (!world.isRemote)
      {
        fillOver = !fillOver
        player.addChatMessage(new ChatComponentText("Grate now set to " + (if (fillOver) "" else "not ") +
                                                    "fill higher than itself."))
        gratePath = null
      }
      return true
    }
    super.configure(player, side, hit)
  }

  class GratePathfinder(checkVertical: Boolean)
  {

    var fluidType: Fluid = _

    var start: Vector3 = _

    var navigationMap: HashMap[Vector3, Vector3] = new HashMap[Vector3, Vector3]()

    var workingNodes: PriorityQueue[ComparableVector] = if (checkVertical) new PriorityQueue[ComparableVector]()
    else new PriorityQueue[ComparableVector](1024,
                                             new Comparator[ComparableVector]()
                                             {

                                               override def compare(a: ComparableVector, b: ComparableVector): Int =
                                               {
                                                 val wa = a.asInstanceOf[TileGrate.ComparableVector]
                                                 val wb = b.asInstanceOf[TileGrate.ComparableVector]
                                                 wa.iterations - wb.iterations
                                               }
                                             })

    var drainNodes: PriorityQueue[ComparableVector] = new PriorityQueue[ComparableVector](1024, Collections.reverseOrder())

    def startFill(start: Vector3, fluidID: Int)
    {
      this.fluidType = FluidRegistry.getFluid(fluidID)
      this.start = start
      for (i <- 0 until 6)
      {
        val dir = ForgeDirection.getOrientation(i)
        if (dir == TileGrate.this.getDirection)
        {
          val check = start.clone().add(dir)
          this.navigationMap.put(check, start)
          this.workingNodes.add(new TileGrate.ComparableVector(check, 0))
        }
      }
    }

    def tryFill(amount: Int, tries: Int): Int =
    {
      var filled = 0
      if (amount >= FluidContainerRegistry.BUCKET_VOLUME)
      {
        for (i <- 0 until tries)
        {
          val next = workingNodes.poll()
          if (next == null)
          {
            TileGrate.this.resetPath()
            return 0
          }
          if (!isConnected(next.position))
          {
            TileGrate.this.resetPath()
            return 0
          }
          val didFill = FluidUtility.fillBlock(TileGrate.this.worldObj, next.position, new FluidStack(fluidType,
                                                                                                      amount), true)
          filled += didFill
          if (FluidUtility.getFluidAmountFromBlock(TileGrate.this.worldObj, next.position) >
              0 ||
              didFill > 0)
          {
            addNextFill(next)
          }
          if (filled >= amount)
          {
            return filled
          }
        }
      }
      filled
    }

    def isConnected(check: Vector3): Boolean =
    {
      if (check == this.start)
      {
        return true
      }
      return false
    }

    def addNextFill(next: ComparableVector)
    {
      for (i <- 0 until 6)
      {
        val newPosition = next.position.clone().add(ForgeDirection.getOrientation(i))
        if (!this.navigationMap.containsKey(newPosition) && !(!fillOver && newPosition.yi > y))
        {
          this.navigationMap.put(newPosition, next.position)
          this.workingNodes.add(new ComparableVector(newPosition, next.iterations + 1))
        }
      }
    }

    def startDrain(start: Vector3): Boolean =
    {
      fluidType = null
      this.start = start
      for (i <- 0 until 6)
      {
        val dir = ForgeDirection.getOrientation(i)
        if (dir == TileGrate.this.getDirection)
        {
          val check = start.clone().add(dir)
          this.navigationMap.put(check, start)
          this.workingNodes.add(new ComparableVector(check, 0))
          fluidType = FluidUtility.getFluidFromBlock(TileGrate.this.worldObj, check)
        }
      }
      fluidType != null
    }

    def tryPopulateDrainMap(tries: Int): Boolean =
    {
      for (i <- 0 until tries)
      {
        val check = workingNodes.poll()
        if (check == null)
        {
          return true
        }
        val checkFluid = FluidUtility.getFluidFromBlock(TileGrate.this.worldObj, check.position)
        if (checkFluid != null && fluidType.getID == checkFluid.getID)
        {
          addNextDrain(check)
          val checkAmount = FluidUtility.getFluidAmountFromBlock(TileGrate.this.worldObj, check.position)
          if (checkAmount > 0)
          {
            drainNodes.add(check)
          }
        }
      }
      drainNodes.size > 0
    }

    def addNextDrain(next: ComparableVector)
    {
      for (i <- 0 until 6)
      {
        val check = next.position.clone().add(ForgeDirection.getOrientation(i))
        val checkFluid = FluidUtility.getFluidFromBlock(TileGrate.this.worldObj, check)
        if (checkFluid != null && fluidType.getID == checkFluid.getID)
        {
          if (!navigationMap.containsKey(check))
          {
            navigationMap.put(check, next.position)
            workingNodes.add(new TileGrate.ComparableVector(check, next.iterations + 1))
          }
        }
      }
    }

    def tryDrain(targetAmount: Int, doDrain: Boolean): FluidStack =
    {
      var drainedAmount = 0
      while (!drainNodes.isEmpty)
      {
        val fluidCoord = drainNodes.peek()
        if (!isConnected(fluidCoord.position))
        {
          TileGrate.this.resetPath()
          return new FluidStack(fluidType, drainedAmount)
        }
        if (FluidUtility.getFluidFromBlock(TileGrate.this.worldObj, fluidCoord.position) ==
            null ||
            this.fluidType.getID !=
            FluidUtility.getFluidFromBlock(TileGrate.this.worldObj, fluidCoord.position)
              .getID)
        {
          this.drainNodes.poll()
        }
        else
        {
          val checkAmount = FluidUtility.getFluidAmountFromBlock(TileGrate.this.worldObj, fluidCoord.position)
          if (drainedAmount + checkAmount > targetAmount)
          {
            //break
          }
          if (checkAmount == 0)
          {
            this.drainNodes.poll()
          }
          else
          {
            val fluidStack = FluidUtility.drainBlock(TileGrate.this.worldObj, fluidCoord.position, doDrain)
            this.drainNodes.poll()
            if (fluidStack != null)
            {
              drainedAmount += fluidStack.amount
              if (drainedAmount >= targetAmount)
              {
                //break
              }
            }
          }
        }
      }
      TileGrate.this.resetPath()
      if (drainedAmount > 0)
      {
        return new FluidStack(fluidType, drainedAmount)
      }
      null
    }
  }

}