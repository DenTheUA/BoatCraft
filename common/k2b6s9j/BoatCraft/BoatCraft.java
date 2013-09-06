package k2b6s9j.BoatCraft;

import java.io.IOException;
import java.util.logging.Level;

import k2b6s9j.BoatCraft.entity.item.EntityBirchWoodBoat;
import k2b6s9j.BoatCraft.entity.item.EntityBoatChest;
import k2b6s9j.BoatCraft.entity.item.EntityBoatFurnace;
import k2b6s9j.BoatCraft.entity.item.EntityBoatHopper;
import k2b6s9j.BoatCraft.entity.item.EntityBoatTNT;
import k2b6s9j.BoatCraft.entity.item.EntityJungleWoodBoat;
import k2b6s9j.BoatCraft.entity.item.EntityOakWoodBoat;
import k2b6s9j.BoatCraft.entity.item.EntitySpruceWoodBoat;
import k2b6s9j.BoatCraft.item.boat.wood.birch.BoatBirch;
import k2b6s9j.BoatCraft.item.boat.wood.jungle.BoatJungle;
import k2b6s9j.BoatCraft.item.boat.wood.oak.BoatOakChest;
import k2b6s9j.BoatCraft.item.boat.wood.oak.BoatOakFurnace;
import k2b6s9j.BoatCraft.item.boat.wood.oak.BoatOakHopper;
import k2b6s9j.BoatCraft.item.boat.wood.oak.BoatOak;
import k2b6s9j.BoatCraft.item.boat.wood.oak.BoatOakTNT;
import k2b6s9j.BoatCraft.item.boat.wood.spruce.BoatSpruce;
import k2b6s9j.BoatCraft.proxy.CommonProxy;
import k2b6s9j.BoatCraft.utilities.CraftingUtilities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.mcstats.MetricsLite;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "BoatCraft", name = "BoatCraft", version = "2.0", dependencies="after:Forestry;")
@NetworkMod(channels = {"BoatCraft"}, clientSideRequired = true, serverSideRequired = true)

public class BoatCraft {
	@Instance("BoatCraft")
    public static BoatCraft instance;
	
	@SidedProxy(clientSide="k2b6s9j.BoatCraft.proxy.ClientProxy", serverSide="k2b6s9j.BoatCraft.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	//Mod Info
	public final String modName = "BoatCraft";
	public final String modVersion = "2.0-ForestryWood";
	
	//Config File Strings
	public final String itemBoats = "Boats in Item Form";
	
	//Boat Items
	public BoatOak oakBoat;
	public BoatOakChest oakChestBoat;
	public BoatOakFurnace oakFurnaceBoat;
	public BoatOakHopper oakHopperBoat;
	public BoatOakTNT oakTntBoat;
	public BoatSpruce spruceBoat;
	public BoatBirch birchBoat;
	public BoatJungle jungleBoat;
	
	
	public boolean OreDictWoodBoat;

	@EventHandler
	public void PreInit (FMLPreInitializationEvent event)
	{
		FMLLog.log(Level.INFO, "BoatCraft");
		FMLLog.log(Level.INFO, "Copyright Kepler Sticka-Jones 2013");
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try
        {
        	//Boats
        	oakBoat.ID = cfg.getItem(itemBoats, "Oak Boat", 25500).getInt(25500);
        	oakChestBoat.ID = cfg.getItem(itemBoats, "Chest Boat", 25501).getInt(25501);
        	oakFurnaceBoat.ID = cfg.getItem(itemBoats, "Furnace Boat", 25502).getInt(25502);
        	oakHopperBoat.ID = cfg.getItem(itemBoats, "Hopper Boat", 25503).getInt(25503);
        	oakTntBoat.ID = cfg.getItem(itemBoats, "TNT Boat", 25504).getInt(25504);
        	spruceBoat.ID = cfg.getItem(itemBoats, "Spruce Boat", 25505).getInt(25505);
        	birchBoat.ID = cfg.getItem(itemBoats, "Birch Boat", 25506).getInt(25506);
        	jungleBoat.ID = cfg.getItem(itemBoats, "Jungle Boat", 25507).getInt(25507);
        	
        	//Modules
        	this.OreDictWoodBoat = cfg.get("Modules", "OreDictWoodBoats", false, "Use the OreDictionary to craft Wooden Boats").getBoolean(false);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "BoatCraft had a problem loading it's configuration");
        }
        finally
        {
            if (cfg.hasChanged())
                cfg.save();
        }
        InitItems();
        RegisterRecipes();
        EntityWork();
        try {
            MetricsLite metrics = new MetricsLite(this.modName, this.modVersion);
            metrics.start();
        } catch (IOException e) {
        	FMLLog.log(Level.SEVERE, e, "BoatCraft had a problem submitting data to MCStats");
        }
	}
	
	public void InitItems() {
		OreDictionary.registerOre("itemBoat", Item.boat);
		
		//Boats
		oakBoat = new BoatOak(oakBoat.ID);
		oakChestBoat = new BoatOakChest(oakChestBoat.ID);
		oakFurnaceBoat = new BoatOakFurnace(oakFurnaceBoat.ID);
		oakHopperBoat = new BoatOakHopper(oakHopperBoat.ID);
		oakTntBoat = new BoatOakTNT(oakTntBoat.ID);
		spruceBoat = new BoatSpruce(spruceBoat.ID);
		birchBoat = new BoatBirch(birchBoat.ID);
		jungleBoat = new BoatJungle(jungleBoat.ID);
	}
	
	public void RegisterRecipes() {
		//Boat Recipes
		if (!OreDictWoodBoat) {
			CraftingUtilities.RemoveRecipe(new ItemStack(Item.boat));
	        GameRegistry.addRecipe(new ItemStack(oakBoat), "W W", "WWW", Character.valueOf('W'), new ItemStack(Block.planks, 1, 0));
	        GameRegistry.addRecipe(new ItemStack(spruceBoat), "W W", "WWW", Character.valueOf('W'), new ItemStack(Block.planks, 1, 1));
	        GameRegistry.addRecipe(new ItemStack(birchBoat), "W W", "WWW", Character.valueOf('W'), new ItemStack(Block.planks, 1, 2));
	        GameRegistry.addRecipe(new ItemStack(jungleBoat), "W W", "WWW", Character.valueOf('W'), new ItemStack(Block.planks, 1, 3));
		}
		if (OreDictWoodBoat) {
			CraftingUtilities.RemoveRecipe(new ItemStack(Item.boat));
			CraftingUtilities.AddRecipe(new ItemStack(oakBoat), "W W", "WWW", Character.valueOf('W'), "plankWood");
			CraftingUtilities.AddShapelessRecipe(new ItemStack(oakChestBoat), new ItemStack(Block.chest), "itemBoat");
	        CraftingUtilities.AddShapelessRecipe(new ItemStack(oakFurnaceBoat), new ItemStack(Block.furnaceIdle), "itemBoat");
	        CraftingUtilities.AddShapelessRecipe(new ItemStack(oakHopperBoat), new ItemStack(Block.tnt), "itemBoat");
	        CraftingUtilities.AddShapelessRecipe(new ItemStack(oakTntBoat), new ItemStack(Block.hopperBlock), "itemBoat");
		}
	}
	
	public void EntityWork() {
		proxy.registerRenderers();
		EntityRegistry.registerModEntity(EntityOakWoodBoat.class, "Oak Wood Boat", 1, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityBoatChest.class, "Oak Wood Chest Boat", 2, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityBoatFurnace.class, "Oak Wood Furnace Boat", 3, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityBoatHopper.class, "Oak Wood Hopper Boat", 4, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityBoatTNT.class, "Oak Wood TNT Boat", 5, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntitySpruceWoodBoat.class, "Spruce Wood Boat", 6, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityBirchWoodBoat.class, "Birch Wood Boat", 7, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityJungleWoodBoat.class, "Jungle Wood Boat", 8, this, 80, 3, true);
	}

	@EventHandler
	public void Init (FMLInitializationEvent event)
	{
		LanguageRegistry.addName(Item.boat, "Vanilla Boat");
		
		//Boats
		if (!OreDictWoodBoat) {
			LanguageRegistry.addName(oakBoat, "Oak Wood Boat");
			LanguageRegistry.addName(oakChestBoat, "Oak Wood Chest Boat");
			LanguageRegistry.addName(oakFurnaceBoat, "Oak Wood Furnace Boat");
			LanguageRegistry.addName(oakHopperBoat, "Oak Wood Hopper Boat");
			LanguageRegistry.addName(oakTntBoat, "Oak Wood TNT Boat");
			LanguageRegistry.addName(spruceBoat, "Spruce Wood Boat");
			LanguageRegistry.addName(birchBoat, "Birch Wood Boat");
			LanguageRegistry.addName(jungleBoat, "Jungle Wood Boat");
		}
		if (OreDictWoodBoat) {
			LanguageRegistry.addName(oakBoat, "Wooden Boat");
			LanguageRegistry.addName(oakChestBoat, "Chest Boat");
			LanguageRegistry.addName(oakFurnaceBoat, "Furnace Boat");
			LanguageRegistry.addName(oakHopperBoat, "Hopper Boat");
			LanguageRegistry.addName(oakTntBoat, "TNT Boat");
		}
		
	}

	@EventHandler
	public void PostInit (FMLPostInitializationEvent event)
	{

	}

}
