package com.builtbroken.mffs.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot
{
    protected IInventory tileEntity;

    public SlotBase(IInventory tileEntity, int id, int par4, int par5)
    {
        super(tileEntity, id, par4, par5);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return this.tileEntity.isItemValidForSlot(this.getSlotIndex(), itemStack);
    }

    @Override
    public int getSlotStackLimit()
    {
        ItemStack itemStack = this.tileEntity.getStackInSlot(this.slotNumber);

        if (itemStack != null)
        {
            return itemStack.getMaxStackSize();
        }

        return this.tileEntity.getInventoryStackLimit();
    }
}