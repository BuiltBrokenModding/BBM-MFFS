package mffs.field.module;

import com.builtbroken.mc.lib.transform.vector.Pos;
import mffs.api.fortron.FrequencyGrid;
import mffs.api.machine.IProjector;
import mffs.base.ItemModule;
import mffs.field.TileElectromagneticProjector;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.Iterator;
import java.util.List;

/** Removes overlapping pieces of a field */
public class ItemModuleFusion extends ItemModule
{
    public ItemModuleFusion()
    {
        setMaxStackSize(1);
        setCost(1f);
    }

    @Override
    public boolean onProject(ItemStack stack, IProjector projector, List<Pos> field)
    {
        TileEntity tile = (TileEntity) projector;
        List<TileElectromagneticProjector> projectors = FrequencyGrid.instance().getNodes(TileElectromagneticProjector.class, projector.getFrequency());

        //TODO: Check threading efficiency
        for (TileElectromagneticProjector proj : projectors)
        {
            if (proj.getWorldObj() == tile.getWorldObj() && proj.isActive() && proj.getMode() != null)
            {
                Iterator<Pos> it = field.iterator();
                while (it.hasNext())
                {
                    Pos pos = it.next();
                    if (proj.getInteriorPoints().contains(pos) || proj.getMode().isInField(stack, proj, pos))
                    {
                        it.remove();
                    }
                }
            }
        }
        return false;
    }
}