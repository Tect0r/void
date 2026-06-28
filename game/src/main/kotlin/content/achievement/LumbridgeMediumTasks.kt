package content.achievement

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.*
import world.gregs.voidps.type.Tile

/**
 * Triggers for the Lumbridge/Draynor **Medium** achievement task set.
 *
 * Dialogue/teleport-based mediums live in their own domain scripts:
 * - `wheres_the_beef` -> [content.area.misthalin.lumbridge.BeefyBill]
 * - `always_be_prepared` -> [content.area.misthalin.lumbridge.castle.DukeHoracio]
 * - `ease_of_access` -> [content.skill.magic.spell.Teleports]
 */
class LumbridgeMediumTasks : Script {

    init {
        itemAdded("coal", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["lumbridge_swamp_west_coal_mine"]) {
                set("everybody_loves_coal_task", true)
            }
        }

        itemAdded("silver_ore", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["al_kharid_mine"]) {
                set("hi_ho_silver_task", true)
            }
        }

        itemAdded("silver_bar", inventory = "inventory") {
            if (softTimers.contains("smelting") && tile in Areas["lumbridge_furnace"]) {
                set("one_day_you_shall_be_a_fork_task", true)
            }
        }

        itemAdded("unstrung_symbol", inventory = "inventory") {
            if (tile in Areas["lumbridge_furnace"]) {
                set("made_to_order_task", true)
            }
        }

        itemRemoved("raw_lobster", inventory = "inventory") {
            if (inventory[it.index].id == "lobster" && softTimers.contains("cooking") && tile in Areas["lumbridge_kitchen"]) {
                set("a_meal_fit_for_a_duke_task", true)
            }
        }

        itemAdded("raw_salmon", inventory = "inventory") {
            if (softTimers.contains("fishing") && tile in Areas["lumbridge_river_fishing_area"]) {
                set("lovely_with_a_squeeze_of_lemon_task", true)
            }
        }

        // Cut down the willow (willow_2, id 5552) east of Lumbridge Castle, across the river.
        itemAdded("willow_logs", inventory = "inventory") {
            if (softTimers.contains("woodcutting") && tile in Areas["lumbridge_castle_willow"]) {
                set("weeping_willow_task", true)
            }
        }

        // Smith a steel longsword on the anvil in the Draynor jail sewers.
        itemAdded("steel_longsword", inventory = "inventory") {
            if (softTimers.contains("smithing") && tile in Areas["draynor_sewer_anvil"]) {
                set("steel_justice_task", true)
            }
        }

        // Light a willow log fire on top of the Lumbridge Castle gatehouse (level 2).
        // The fire colour (orange) is shared between log types, so the willow log is
        // identified by listening to its inventory removal: the log is dropped to the
        // floor at the player's tile right before lighting. That tile is the eventual
        // fire tile; on firemaking completion we confirm a fire spawned there.
        itemRemoved("willow_logs", inventory = "inventory") {
            if (tile in Areas["lumbridge_castle_gatehouse_roof"]) {
                set("wisp_of_smoke_fire_tile", tile)
            }
        }

        timerStop("firemaking") {
            val fireTile: Tile? = this["wisp_of_smoke_fire_tile"]
            if (fireTile != null) {
                if (GameObjects.findOrNull(fireTile) { obj -> obj.id.startsWith("fire") } != null) {
                    set("willow_the_wisp_of_smoke_task", true)
                }
                clear("wisp_of_smoke_fire_tile")
            }
        }
    }
}
