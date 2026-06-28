package content.achievement

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.inv.*

class VarrockEasyTasks : Script {

    init {
        // Doing the Ironing - Mine iron ore south-east of Varrock.
        itemAdded("iron_ore", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["varrock_south_east_mine"] && !get("doing_the_ironing_task", false)) {
                set("doing_the_ironing_task", true)
            }
        }

        // Sherpa's Delight - Catch a trout in the river east of Barbarian Village.
        itemAdded("raw_trout", inventory = "inventory") {
            if (softTimers.contains("fishing") && tile in Areas["barbarian_village_fishing_spot"] && !get("sherpas_delight_task", false)) {
                set("sherpas_delight_task", true)
            }
        }

        // Journey to the Centre of the Earth Altar - Enter the Earth Altar with an earth tiara or talisman.
        entered("earth_altar") {
            if (inventory.contains("earth_talisman") || inventory.contains("earth_tiara") || equipment.contains("earth_tiara")) {
                set("journey_to_the_centre_of_the_earth_altar_task", true)
            }
        }

        // On the Ragged Edge - Enter Edgeville Dungeon using the entrance south of Edgeville.
        objTeleportLand("Climb-down", "trapdoor_80_opened") { _, _ ->
            set("on_the_ragged_edge_task", true)
        }
    }
}
