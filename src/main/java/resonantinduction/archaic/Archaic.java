package resonantinduction.archaic;

import net.minecraft.block.Block;
import resonantinduction.archaic.crate.BlockCrate;
import resonantinduction.archaic.crate.TileCrate;
import resonantinduction.archaic.engineering.BlockEngineeringTable;
import resonantinduction.archaic.engineering.TileEngineeringTable;
import resonantinduction.core.Reference;
import resonantinduction.core.ResonantInduction;
import resonantinduction.core.Settings;
import calclavia.lib.content.ContentRegistry;
import calclavia.lib.network.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * Resonant Induction Archaic Module
 * 
 * @author DarkCow, Calclavia
 * 
 */
@Mod(modid = Archaic.ID, name = Archaic.NAME, version = Reference.VERSION, dependencies = "required-after:" + ResonantInduction.ID)
@NetworkMod(channels = Reference.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Archaic
{
	/** Mod Information */
	public static final String ID = "ResonantInduction|Archaic";
	public static final String NAME = Reference.NAME + " Archaic";

	@Instance(ID)
	public static Archaic INSTANCE;

	@SidedProxy(clientSide = "resonantinduction.archaic.ClientProxy", serverSide = "resonantinduction.archaic.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Metadata(ID)
	public static ModMetadata metadata;

	public static final ContentRegistry contentRegistry = new ContentRegistry(Settings.CONFIGURATION, ID);

	public static Block blockEngineeringTable;
	public static Block blockCrate;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		blockEngineeringTable = contentRegistry.createTile(BlockEngineeringTable.class, TileEngineeringTable.class);
		blockCrate = contentRegistry.createTile(BlockCrate.class, TileCrate.class);
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		Settings.setModMetadata(metadata, ID, NAME);
		proxy.init();
	}
}
