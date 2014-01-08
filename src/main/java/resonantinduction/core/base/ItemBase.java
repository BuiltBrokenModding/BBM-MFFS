package resonantinduction.core.base;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import resonantinduction.core.ResonantInduction;
import resonantinduction.core.ResonantInductionTabs;

/**
 * 
 * @author AidanBrady
 * 
 */
public class ItemBase extends Item
{
	public ItemBase(String name, int id)
	{
		super(ResonantInduction.CONFIGURATION.get(Configuration.CATEGORY_ITEM, name, id).getInt(id));
		this.setCreativeTab(ResonantInductionTabs.CORE);
		this.setUnlocalizedName(ResonantInduction.PREFIX + name);
		this.setTextureName(ResonantInduction.PREFIX + name);
	}
}