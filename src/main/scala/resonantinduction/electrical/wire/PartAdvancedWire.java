package resonantinduction.electrical.wire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import resonantinduction.core.prefab.part.MultipartUtility;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;
import universalelectricity.compatibility.Compatibility;

/** @author Calclavia */
@Deprecated
public abstract class PartAdvancedWire extends PartConductor
{
    public static final int DEFAULT_COLOR = 15;
    public int color = DEFAULT_COLOR;

    public WireMaterial material = WireMaterial.COPPER;
    public boolean isInsulated = false;
    protected ItemStack insulationType = new ItemStack(Blocks.wool);

    /** INTERNAL USE. Can this conductor connect with an external object? */
    @Override
    public boolean canConnectTo(Object obj)
    {
        if (obj != null && (obj.getClass().isAssignableFrom(this.getClass()) || this.getClass().isAssignableFrom(obj.getClass())))
        {
            PartAdvancedWire wire = (PartAdvancedWire) obj;

            if (this.getMaterial() == wire.getMaterial())
            {
                if (this.isInsulated() && wire.isInsulated())
                {
                    return this.getColor() == wire.getColor() || (this.getColor() == DEFAULT_COLOR || wire.getColor() == DEFAULT_COLOR);
                }

                return true;
            }
        }

        return Compatibility.isHandler(obj);
    }

    protected boolean canConnectTo(Object obj, ForgeDirection dir)
    {
        if (obj != null && (obj.getClass().isAssignableFrom(this.getClass()) || this.getClass().isAssignableFrom(obj.getClass())))
        {
            PartAdvancedWire wire = (PartAdvancedWire) obj;

            if (this.getMaterial() == wire.getMaterial())
            {
                if (this.isInsulated() && wire.isInsulated())
                {
                    return this.getColor() == wire.getColor() || (this.getColor() == DEFAULT_COLOR || wire.getColor() == DEFAULT_COLOR);
                }

                return true;
            }
        }

        return Compatibility.isHandler(obj);
    }

    //@Override
    public float getResistance()
    {
        return this.getMaterial().resistance;
    }

    //@Override
    public long getCurrentCapacity()
    {
        return this.getMaterial().maxAmps;
    }

    /** Material Methods */
    public WireMaterial getMaterial()
    {
        return this.material;
    }

    public void setMaterial(WireMaterial material)
    {
        this.material = material;
    }

    public void setMaterial(int id)
    {
        this.setMaterial(WireMaterial.values()[id]);
    }

    public int getMaterialID()
    {
        return this.material.ordinal();
    }

    /** Insulation Methods */
    public void setInsulated(boolean insulated)
    {
        this.isInsulated = insulated;
        this.color = DEFAULT_COLOR;

        if (!this.world().isRemote)
        {
            tile().notifyPartChange(this);
            this.sendInsulationUpdate();
        }
    }

    public void setInsulated(int dyeColour)
    {
        this.isInsulated = true;
        this.color = dyeColour;

        if (!this.world().isRemote)
        {
            tile().notifyPartChange(this);
            this.sendInsulationUpdate();
            this.sendColorUpdate();
        }
    }

    public boolean isInsulated()
    {
        return this.isInsulated;
    }

    public void sendInsulationUpdate()
    {
        tile().getWriteStream(this).writeByte(1).writeBoolean(this.isInsulated);
    }

    /** Wire Coloring Methods */
    public int getColor()
    {
        return this.isInsulated ? this.color : -1;
    }

    public void setColor(int dye)
    {
        if (this.isInsulated)
        {
            this.color = dye;

            if (!this.world().isRemote)
            {
                tile().notifyPartChange(this);
                this.sendColorUpdate();
            }
        }
    }

    public void sendColorUpdate()
    {
        tile().getWriteStream(this).writeByte(2).writeInt(this.color);
    }

    /** Changes the wire's color. */
    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition part, ItemStack itemStack)
    {
        if (itemStack != null)
        {
            int dyeColor = MultipartUtility.isDye(itemStack);

            if (dyeColor != -1 && this.isInsulated())
            {
                if (!player.capabilities.isCreativeMode)
                {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                }

                this.setColor(dyeColor);
                return true;
            }
            else if (itemStack.getItem() == insulationType.getItem())
            {
                if (this.isInsulated())
                {
                    if (!world().isRemote && player.capabilities.isCreativeMode)
                    {
                        tile().dropItems(Collections.singletonList(insulationType));
                    }

                    this.setInsulated(false);
                    return true;
                }
                else
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    this.setInsulated(itemStack.getItemDamage());
                    return true;
                }
            }
            else if (itemStack.getItem() instanceof ItemShears && isInsulated())
            {
                if (!world().isRemote && !player.capabilities.isCreativeMode)
                {
                    tile().dropItems(Collections.singletonList(insulationType));
                }

                this.setInsulated(false);
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack getItem()
    {
        return WireMaterial.values()[getMaterialID()].getWire();
    }

    @Override
    public Iterable<ItemStack> getDrops()
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(getItem());

        if (this.isInsulated)
        {
            drops.add(insulationType.copy());
        }

        return drops;
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit)
    {
        return getItem();
    }

    @Override
    public void readDesc(MCDataInput packet)
    {
        this.setMaterial(packet.readByte());
        this.color = packet.readByte();
        this.isInsulated = packet.readBoolean();
    }

    @Override
    public void writeDesc(MCDataOutput packet)
    {
        packet.writeByte((byte) this.getMaterialID());
        packet.writeByte((byte) this.color);
        packet.writeBoolean(this.isInsulated);
    }

    public void read(MCDataInput packet, int packetID)
    {
        switch (packetID)
        {
            case 1:
                this.isInsulated = packet.readBoolean();
                this.tile().markRender();
                break;
            case 2:
                this.color = packet.readInt();
                this.tile().markRender();
                break;
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        super.save(nbt);
        nbt.setInteger("typeID", getMaterialID());
        nbt.setBoolean("isInsulated", isInsulated);
        nbt.setInteger("dyeID", color);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        super.load(nbt);
        setMaterial(nbt.getInteger("typeID"));
        this.isInsulated = nbt.getBoolean("isInsulated");
        this.color = nbt.getInteger("dyeID");
    }

    @Override
    public boolean checkRedstone(int side)
    {
        if (this.world().isBlockIndirectlyGettingPowered(x(), y(), z()))
        {
            return true;
        }
        else
        {
            for (TMultiPart tp : tile().jPartList())
            {
                if (tp instanceof IRedstonePart)
                {
                    IRedstonePart rp = (IRedstonePart) tp;
                    if ((Math.max(rp.strongPowerLevel(side), rp.weakPowerLevel(side)) << 4) > 0)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "[PartAdvancedWire]" + x() + "x " + y() + "y " + z() + "z ";
    }
}
