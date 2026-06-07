package content.achievement

import content.entity.combat.killer
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.entity.obj.ObjectShape
import world.gregs.voidps.engine.inv.*
import world.gregs.voidps.type.Tile

class LumbridgeEasyTasks : Script {

    init {
        // Iron On - Mine iron ore from the Al Kharid mining spot.
        itemAdded("iron_ore", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["al_kharid_mine"]) {
                set("iron_on_task", true)
            }
        }

        // And It Was THIS Big! - Catch a pike east of Lumbridge Castle.
        itemAdded("raw_pike", inventory = "inventory") {
            if (softTimers.contains("fishing") && tile in Areas["lumbridge_river_fishing_area"]) {
                set("and_it_was_this_big_task", true)
            }
        }

        // Belter of a Smelter - Smelt a steel bar in the Lumbridge furnace.
        itemAdded("steel_bar", inventory = "inventory") {
            if (softTimers.contains("smelting") && tile in Areas["lumbridge_furnace"]) {
                set("belter_of_a_smelter_task", true)
            }
        }

        // Slippery When Wet - Craft a water rune at the Water Altar.
        itemAdded("water_rune", inventory = "inventory") {
            if (softTimers.contains("runecrafting")) {
                set("slippery_when_wet_task", true)
            }
        }

        // Ratatouille - Cook rat meat on a campfire in Lumbridge Swamp.
        itemRemoved("raw_rat_meat", inventory = "inventory") {
            if (inventory[it.index].id == "rat_meat" && softTimers.contains("cooking") && tile in Areas["lumbridge_swamp"]) {
                set("ratatouille_task", true)
            }
        }

        // Camping Trip - Light a campfire from normal logs in Lumbridge Swamp.
        itemRemoved("logs", inventory = "inventory") {
            if (!get("camping_trip_task", false)) {
                set("camping_burnt_log", true)
                set("camping_fire_tile", tile)
            }
        }

        timerStop("firemaking") {
            val burnt: Boolean = remove("camping_burnt_log") ?: return@timerStop
            val fireTile: Tile = remove("camping_fire_tile") ?: return@timerStop
            if (burnt && fireTile in Areas["lumbridge_swamp"]) {
                val fire = GameObjects.getShape(fireTile, ObjectShape.CENTRE_PIECE_STRAIGHT)
                if (fire != null && fire.id.startsWith("fire_")) {
                    set("camping_trip_task", true)
                }
            }
        }

        // You Doity Rat - Kill a giant rat in Lumbridge Swamp.
        npcDeath("giant_rat*") {
            val killer = killer
            if (killer is Player && tile in Areas["lumbridge_swamp"]) {
                killer["you_doity_rat_task"] = true
            }
        }

        // Money Down the Drayn - Access the bank in Draynor Village.
        interfaceOpened("bank") {
            if (tile in Areas["draynor"]) {
                set("money_down_the_drayn_task", true)
            }
        }
    }
}
