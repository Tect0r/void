package content.achievement

import content.entity.combat.killer
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.character.player.Player

class VarrockEliteTasks : Script {
    init {
        // Stick a Bork In Him, He's Done — Defeat Bork.
        npcDeath("bork,bork_surok") {
            val killer = killer as? Player ?: return@npcDeath
            killer.set("stick_a_bork_in_him_hes_done_task", true)
        }
    }
}
