package content.achievement

import content.entity.combat.killer
import content.entity.player.dialogue.Happy
import content.entity.player.dialogue.Neutral
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.npc
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.inv.*
import world.gregs.voidps.network.login.protocol.visual.update.player.EquipSlot

class FaladorEasyTasks : Script {

    init {
        // It's Not Wabbit Season - Kill a duck in Falador Park.
        npcDeath("duck*") {
            val killer = killer
            if (killer is Player && killer.tile in Areas["falador_park"]) {
                killer["its_not_wabbit_season_task"] = true
            }
        }

        // Stand and Deliver - Kill a highwayman on the road south of Falador.
        npcDeath("highwayman_falador") {
            val killer = killer
            if (killer is Player) {
                killer["stand_and_deliver_task"] = true
            }
        }

        // Amulet of Weedspeak - Buy a Farming amulet from Sarah on the farm north of Port Sarim.
        bought("amulet_of_farming*") {
            set("amulet_of_weedspeak_task", true)
        }

        // Chain Store - Buy a black chainbody from Wayne's Chains and try it on in the shop.
        slotChanged("worn_equipment") {
            val (_, index, item) = it
            if (index == EquipSlot.Chest.index &&
                item.id == "black_chainbody" &&
                interfaces.contains("shop") &&
                get("shop", "") == "waynes_chains_chainmail_specialist"
            ) {
                set("chain_store_task", true)
            }
        }

        // Going Along With the 'Fro - Talk to Party Pete in the Party Room.
        npcOperate("Talk-to", "party_pete") {
            set("going_along_with_the_fro_task", true)
            npc<Happy>("Welcome to the Falador Party Room! Here to paaaarty?")
            choice {
                option<Happy>("Yes!") {
                    npc<Happy>("That's the spirit!")
                }
                option<Neutral>("No, thanks.")
            }
        }
    }
}
