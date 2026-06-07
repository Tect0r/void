package content.achievement

import content.skill.prayer.praying
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.inv.*

/**
 * Triggers for the Lumbridge/Draynor **Hard** achievement task set.
 *
 * Two of the seven hard tasks are not wired up because the map objects/areas
 * they require do not exist (the same gap that defers their Medium siblings
 * `steel_justice` and `willow_the_wisp_of_smoke`):
 * - `a_body_in_the_sewers` needs the anvil in the sewers beneath Draynor.
 * - `are_yew_as_fired_up_as_i_am` needs the Lumbridge Castle gatehouse roof.
 */
class LumbridgeHardTasks : Script {

    init {
        // Building Up Strength: enchant a ruby amulet into an amulet of strength within Lumbridge.
        itemAdded("amulet_of_strength", inventory = "inventory") {
            if (tile in Areas["lumbridge"]) {
                set("building_up_strength_task", true)
            }
        }

        // Have Your Cake and Eat It: make a chocolate cake in the Lumbridge Castle kitchen.
        itemAdded("chocolate_cake", inventory = "inventory") {
            if (tile in Areas["lumbridge_kitchen"]) {
                set("have_your_cake_and_eat_it_task", true)
            }
        }

        // Not Waving But Drowning: craft 100 or more water runes in a single binding.
        slotChanged("inventory") { change ->
            if (change.item.id == "water_rune" && softTimers.contains("runecrafting")) {
                val before = if (change.fromItem.id == "water_rune") change.fromItem.amount else 0
                if (change.item.amount - before >= 100) {
                    set("not_waving_but_drowning_task", true)
                }
            }
        }

        // Blast and Hellfire: cast Fire Blast at a goblin or spider in the Lumbridge area.
        combatAttack("magic") {
            if (it.spell == "fire_blast") {
                val target = it.target
                if (target is NPC && (target.id.startsWith("goblin") || target.id.contains("spider")) &&
                    tile in Areas["lumbridge"]
                ) {
                    set("blast_and_hellfire_task", true)
                }
            }
        }

        // Gods, Give Me Strength: pray at the Lumbridge Church altar with Mystic Might active.
        objectOperate("Pray", "prayer_altar_lumbridge") {
            if (praying("mystic_might")) {
                set("gods_give_me_strength_task", true)
            }
        }
        objectOperate("Pray-at", "prayer_altar_lumbridge") {
            if (praying("mystic_might")) {
                set("gods_give_me_strength_task", true)
            }
        }
    }
}
