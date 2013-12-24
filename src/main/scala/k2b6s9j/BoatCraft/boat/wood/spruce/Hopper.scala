package k2b6s9j.BoatCraft.boat.wood.spruce

import k2b6s9j.BoatCraft.boat.Boat.{EntityBoatContainer, ItemCustomBoat, RenderBoat, EntityCustomBoat}
import k2b6s9j.BoatCraft.boat.Materials
import k2b6s9j.BoatCraft.boat.Modifiers
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.block.Block
import k2b6s9j.BoatCraft.registry.RecipeRegistration

object Hopper {

  class Entity(world: World) extends EntityBoatContainer(world) with Materials.Entity.Wood.Spruce with Modifiers.Entity.Hopper {

    override def useItemID(): Boolean = {
      true
    }

    override def customBoatItemID(): Int = {
      Item.item.itemID
    }

  }

  object Item {

    var ID: Int = _
    var item: Item = _
    RecipeRegistration.AddShapelessRecipe(new ItemStack(item), new ItemStack(Block.hopperBlock), "boatSpruce")

  }

  class Item(id: Int) extends ItemCustomBoat(id) {

    setUnlocalizedName("boat.wood.spruce.hopper")
    GameRegistry.registerItem(this, "Hopper Spruce Wood Boat")
    OreDictionary.registerOre("boatSpruceWoodHopper", new ItemStack(this))

    override def getEntity(world: World, x: Int, y: Int, z: Int): EntityCustomBoat = {
      new Entity(world)
    }

  }

  class Render extends RenderBoat with Materials.Render.Wood.Spruce with Modifiers.Render.Hopper {

    override def getEntity(): EntityCustomBoat = {
      val entity: Entity =  null
      return entity
    }

  }

}