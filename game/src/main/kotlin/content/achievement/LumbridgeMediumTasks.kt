package content.achievement

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.inv.*

/**
 * Triggers for the Lumbridge/Draynor **Medium** achievement task set.
 *
 * Dialogue/teleport-based mediums live in their own domain scripts:
 * - `wheres_the_beef` -> [content.area.misthalin.lumbridge.BeefyBill]
 * - `always_be_prepared` -> [content.area.misthalin.lumbridge.castle.DukeHoracio]
 * - `ease_of_access` -> [content.skill.magic.spell.Teleports]
 *
 * The three remaining mediums (`steel_justice`, `weeping_willow`,
 * `willow_the_wisp_of_smoke`) are not yet wired up: they depend on map objects
 * (the Draynor sewer anvil, the willows east of the castle, and the gatehouse
 * roof) for which no area/object is currently defined.
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
    }
}
