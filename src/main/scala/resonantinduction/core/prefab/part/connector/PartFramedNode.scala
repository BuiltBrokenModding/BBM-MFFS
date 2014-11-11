package resonantinduction.core.prefab.part.connector

import java.lang.{Iterable => JIterable}
import java.util.Set

import codechicken.lib.data.{MCDataInput, MCDataOutput}
import codechicken.lib.raytracer.IndexedCuboid6
import codechicken.lib.vec.Cuboid6
import codechicken.multipart._
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{IIcon, MovingObjectPosition}
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.grid.node.NodeConnector
import resonant.lib.wrapper.BitmaskWrapper._
import resonantinduction.core.prefab.part.CuboidShapes

import scala.collection.convert.wrapAll._
import scala.collection.mutable

abstract class PartFramedNode extends PartAbstract with TPartNodeProvider with TSlottedPart with TNormalOcclusion with TIconHitEffects
{
  /** Bitmask connections */
  var clientRenderMask = 0x00

  @SideOnly(Side.CLIENT)
  protected var breakIcon: IIcon = null

  /** Client Side */
  protected var testingSide: ForgeDirection = null

  protected val node: NodeConnector[_]

  //Check if lazy val will be null?
  nodes.add(node)

  override def getStrength(hit: MovingObjectPosition, player: EntityPlayer): Float = 10f

  override def getBounds: Cuboid6 = CuboidShapes.center

  override def getBrokenIcon(side: Int): IIcon = breakIcon

  def getOcclusionBoxes: Set[Cuboid6] = getCollisionBoxes

  /** Rendering and block bounds. */
  override def getCollisionBoxes: Set[Cuboid6] = mutable.Set.empty[Cuboid6] ++ getSubParts

  override def getSubParts: JIterable[IndexedCuboid6] =
  {
    val sideCuboids = if (this.isInstanceOf[TInsulatable] && this.asInstanceOf[TInsulatable].insulated) CuboidShapes.thickSegment else CuboidShapes.segment
    val list = mutable.Set.empty[IndexedCuboid6]
    list += CuboidShapes.center
    list ++= ForgeDirection.VALID_DIRECTIONS.filter(s => clientRenderMask.mask(s) || s == testingSide).map(s => sideCuboids(s.ordinal()))
    return list
  }

  override def getSlotMask = PartMap.CENTER.mask

  def isBlockedOnSide(side: ForgeDirection): Boolean =
  {
    val blocker: TMultiPart = tile.partMap(side.ordinal)
    testingSide = side
    val expandable = NormalOcclusionTest.apply(this, blocker)
    testingSide = null
    return !expandable
  }

  def isCurrentlyConnected(side: ForgeDirection): Boolean = clientRenderMask.mask(side)

  override def write(packet: MCDataOutput, id: Int)
  {
    super.write(packet, id)

    if (id == 0)
    {
      packet.writeByte(node.connectedMask.toByte)
    }
  }

  override def read(packet: MCDataInput, id: Int)
  {
    super.read(packet, id)

    if (id == 0)
    {
      clientRenderMask = packet.readByte()
    }
  }

  @deprecated
  def connectionMapContainsSide(connections: Int, side: ForgeDirection): Boolean =
  {
    val tester = 1 << side.ordinal
    return (connections & tester) > 0
  }
}