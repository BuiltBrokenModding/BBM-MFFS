package dark.assembly.common.armbot.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

import com.builtbroken.common.science.units.UnitHelper;

import dark.api.al.coding.IArmbot;
import dark.api.al.coding.IProgrammableMachine;
import dark.api.al.coding.args.ArgumentIntData;
import dark.assembly.common.armbot.TaskBaseArmbot;
import dark.assembly.common.armbot.TaskBaseProcess;
import dark.assembly.common.machine.InvInteractionHelper;
import dark.core.prefab.helpers.MathHelper;

public class CommandGive extends TaskBaseArmbot
{

    private ItemStack stack;
    private int ammount = -1;

    public CommandGive()
    {
        super("give");
        this.defautlArguments.add(new ArgumentIntData("blockID", -1, Block.blocksList.length - 1, -1));
        this.defautlArguments.add(new ArgumentIntData("blockMeta", -1, 15, -1));
        this.defautlArguments.add(new ArgumentIntData("stackSize", -1, 64, -1));
    }

    @Override
    public ProcessReturn onMethodCalled()
    {
        if (super.onMethodCalled() == ProcessReturn.CONTINUE)
        {

            ammount = UnitHelper.tryToParseInt(this.getArg("stackSize"), -1);
            int blockID = UnitHelper.tryToParseInt(this.getArg("blockID"), -1);
            int blockMeta = UnitHelper.tryToParseInt(this.getArg("blockMeta"), 32767);

            if (blockID > 0)
            {
                stack = new ItemStack(blockID, ammount <= 0 ? 1 : ammount, blockMeta == -1 ? 32767 : blockMeta);
            }

            return ProcessReturn.CONTINUE;
        }
        return ProcessReturn.GENERAL_ERROR;
    }

    @Override
    public ProcessReturn onUpdate()
    {
        if (super.onUpdate() == ProcessReturn.CONTINUE)
        {
            TileEntity targetTile = ((IArmbot) this.program.getMachine()).getHandPos().getTileEntity(this.program.getMachine().getLocation().left());

            if (targetTile != null && ((IArmbot) this.program.getMachine()).getGrabbedObject() instanceof ItemStack)
            {
                ForgeDirection direction = MathHelper.getFacingDirectionFromAngle((float) ((IArmbot) this.program.getMachine()).getRotation().x);
                ItemStack itemStack = (ItemStack) ((IArmbot) this.program.getMachine()).getGrabbedObject();
                List<ItemStack> stacks = new ArrayList<ItemStack>();
                if (this.stack != null)
                {
                    stacks.add(stack);
                }
                InvInteractionHelper invEx = new InvInteractionHelper(this.program.getMachine().getLocation().left(), this.program.getMachine().getLocation().right(), stacks, false);
                ItemStack insertStack = invEx.tryPlaceInPosition(itemStack, new Vector3(targetTile), direction.getOpposite());
                if (((IArmbot) this.program.getMachine()).clear(itemStack))
                {
                    ((IArmbot) this.program.getMachine()).grab(insertStack);
                }
            }
            return ProcessReturn.CONTINUE;
        }
        return ProcessReturn.GENERAL_ERROR;
    }

    @Override
    public String toString()
    {
        return super.toString() + " " + (stack != null ? stack.toString() : "1x???@???");
    }

    @Override
    public TaskBaseProcess loadProgress(NBTTagCompound taskCompound)
    {
        super.loadProgress(taskCompound);
        this.stack = ItemStack.loadItemStackFromNBT(taskCompound.getCompoundTag("item"));
        return this;
    }

    @Override
    public NBTTagCompound saveProgress(NBTTagCompound taskCompound)
    {
        super.saveProgress(taskCompound);
        if (stack != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            this.stack.writeToNBT(tag);
            taskCompound.setTag("item", tag);
        }
        return taskCompound;
    }

    @Override
    public TaskBaseProcess clone()
    {
        return new CommandGive();
    }

    @Override
    public boolean canUseTask(IProgrammableMachine device)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
