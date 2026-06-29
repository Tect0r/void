package content.achievement

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas

/**
 * Triggers for the hookable subset of the Varrock Medium Achievement Tasks.
 *
 * Each handler sets a `*_task` variable; the completion popup/reward is driven
 * centrally by [TaskSystem]'s `variableSet("*_task")` hook.
 *
 * Tasks living in their own content files instead of here:
 * - "Champion!" (`champion_task`) -> [content.area.misthalin.varrock.champions_guild.ChampionsGuild]
 * - "Unlocking Your Emotions" (`unlocking_your_emotions_task`) -> [content.entity.player.modal.tab.Emotes]
 */
class VarrockMediumTasks : Script {

    init {
        // The Body Shop - Browse Oziach's Armour Shop.
        shopOpen("oziachs_armour") {
            set("the_body_shop_task", true)
        }

        // Promised the Earth - Craft an earth tiara on the Earth Altar.
        itemAdded("earth_tiara", inventory = "inventory") {
            if (tile in Areas["earth_altar"]) {
                set("promised_the_earth_task", true)
            }
        }

        // Like a Varrocket - Use the Teleport to Varrock spell.
        teleportTakeOff("modern") { spell ->
            if (spell == "varrock_teleport") {
                set("like_a_varrocket_task", true)
            }
            true
        }

        // Challenge Vannaka - Get a Slayer task from Vannaka.
        variableSet("slayer_master") { _, _, to ->
            if (to == "vannaka") {
                set("challenge_vannaka_task", true)
            }
        }
    }
}
