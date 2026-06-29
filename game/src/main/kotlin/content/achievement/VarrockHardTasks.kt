package content.achievement

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.character.player.skill.Skill

class VarrockHardTasks : Script {

    init {
        // Battle of the Elements - Craft an air battlestaff.
        crafted(Skill.Crafting) { recipe ->
            if (recipe.add.any { it.id == "air_battlestaff" }) {
                set("battle_of_the_elements_task", true)
            }
        }

        // Waka-Waka-Waka - Make a waka canoe near Edgeville.
        variableSet("canoe_state_edgeville") { _, _, to ->
            if (to == "waka" && !get("waka_waka_waka_task", false)) {
                set("waka_waka_waka_task", true)
            }
        }
    }
}
