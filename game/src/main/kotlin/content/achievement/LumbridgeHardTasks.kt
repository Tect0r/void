package content.achievement

import content.skill.prayer.praying
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.entity.obj.ObjectShape
import world.gregs.voidps.engine.inv.*
import world.gregs.voidps.type.Tile

/**
 * Triggers for the Lumbridge/Draynor **Hard** achievement task set.
 */
class LumbridgeHardTasks : Script {

    init {
        // A Body in the Sewers: smith a mithril platebody on the anvil beneath the Draynor jail.
        itemAdded("mithril_platebody", inventory = "inventory") {
            if (softTimers.contains("smithing") && tile in Areas["draynor_sewer_anvil"]) {
                set("a_body_in_the_sewers_task", true)
            }
        }

        // Are You As Fired Up As I Am?: burn yew logs atop the Lumbridge Castle gatehouse roof.
        // Mirrors the `log_a_rhythm` firemaking pattern: stash the fire tile when the logs leave
        // the inventory, then confirm a fire object stands on that tile when the timer stops.
        itemRemoved("yew_logs", inventory = "inventory") {
            if (!get("are_yew_as_fired_up_as_i_am_task", false)) {
                set("burnt_yew_log", true)
                set("yew_fire_tile", tile)
            }
        }

        timerStop("firemaking") {
            val burnt: Boolean = remove("burnt_yew_log") ?: return@timerStop
            val fireTile: Tile = remove("yew_fire_tile") ?: return@timerStop
            if (burnt && fireTile in Areas["lumbridge_castle_gatehouse_roof"]) {
                val fire = GameObjects.getShape(fireTile, ObjectShape.CENTRE_PIECE_STRAIGHT)
                if (fire != null && fire.id.startsWith("fire_")) {
                    set("are_yew_as_fired_up_as_i_am_task", true)
                }
            }
        }

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
