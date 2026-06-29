package content.achievement

import WorldTest
import interfaceOption
import npcOption
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Tile
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class VarrockMediumTasksTest : WorldTest() {

    override var loadNpcs = true

    @Test
    fun `The Body Shop`() {
        val player = createPlayer(Tile(3068, 3517))
        val oziach = NPCs.find(Tile(3068, 3516), "oziach")

        player.npcOption(oziach, "Trade")
        tick(2)

        assertTrue(player["the_body_shop_task", false])
    }

    @Test
    fun `Promised the Earth`() {
        val player = createPlayer(Tile(2655, 4830))

        player.inventory.add("earth_tiara")

        assertTrue(player["promised_the_earth_task", false])
    }

    @Test
    fun `Promised the Earth not crafted elsewhere`() {
        val player = createPlayer(emptyTile)

        player.inventory.add("earth_tiara")

        assertFalse(player["promised_the_earth_task", false])
    }

    @Test
    fun `Like a Varrocket`() {
        val player = createPlayer()

        player.interfaceOption("modern_spellbook", "varrock_teleport", "Cast")
        tick(2)

        assertTrue(player["like_a_varrocket_task", false])
    }

    @Test
    fun `Like a Varrocket not for other teleports`() {
        val player = createPlayer()

        player.interfaceOption("modern_spellbook", "falador_teleport", "Cast")
        tick(2)

        assertFalse(player["like_a_varrocket_task", false])
    }

    @Test
    fun `Challenge Vannaka`() {
        val player = createPlayer()

        player["slayer_master"] = "vannaka"

        assertTrue(player["challenge_vannaka_task", false])
    }

    @Test
    fun `Challenge Vannaka not for other masters`() {
        val player = createPlayer()

        player["slayer_master"] = "turael"

        assertFalse(player["challenge_vannaka_task", false])
    }

    @Test
    fun `Champion`() {
        val player = createPlayer(Tile(3191, 3364))
        player["quest_points"] = 32
        val door = GameObjects.find(Tile(3191, 3363), "door_champions_guild_closed")

        player.objectOption(door, "Open")
        tick(4)

        assertTrue(player["champion_task", false])
    }

    @Test
    fun `Unlocking Your Emotions`() {
        val player = createPlayer()
        player["unlocked_emote_stomp"] = true
        player["performed_emote_flap"] = true
        player["performed_emote_slap_head"] = true
        player["performed_emote_idea"] = true

        player.interfaceOption("emotes", "stomp", "Stomp")
        tick(2)

        assertTrue(player["unlocking_your_emotions_task", false])
    }
}
