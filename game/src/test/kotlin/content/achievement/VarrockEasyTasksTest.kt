package content.achievement

import FakeRandom
import WorldTest
import npcOption
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.setRandom
import kotlin.test.assertTrue

internal class VarrockEasyTasksTest : WorldTest() {

    override var loadNpcs = true

    @Test
    fun `Doing the Ironing`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = if (until == 256) until else 0
        })
        val player = createPlayer(Tile(3286, 3365))
        player.levels.set(Skill.Mining, 100)
        val rock = createObject("iron_rocks_old_1", Tile(3285, 3365))
        player.inventory.add("bronze_pickaxe")

        player.objectOption(rock, "Mine")
        tick(9)

        assertTrue(player["doing_the_ironing_task", false])
    }

    @Test
    fun `Sherpa's Delight`() {
        val player = createPlayer(Tile(3105, 3424))
        player.levels.set(Skill.Fishing, 30)
        val fishingSpot = createNPC("fishing_spot_barbarian_village", Tile(3104, 3424))
        player.inventory.add("fly_fishing_rod", "feather")

        player.npcOption(fishingSpot, "Lure")
        tick(10)

        assertTrue(player["sherpas_delight_task", false])
    }

    @Test
    fun `Strike a Pose`() {
        val player = createPlayer(Tile(3204, 3418))
        val thessalia = NPCs.find(Tile(3204, 3417), "thessalia")

        player.npcOption(thessalia, "Change-clothes")
        tick(6)

        assertTrue(player["strike_a_pose_task", false])
    }

    @Test
    fun `Essential Facilitator`() {
        val player = createPlayer(Tile(3253, 3403))
        val aubury = NPCs.find(Tile(3253, 3402), "aubury")

        player.npcOption(aubury, "Teleport")
        tick(2)

        assertTrue(player["essential_facilitator_task", false])
    }

    @Test
    fun `Journey to the Centre of the Earth Altar`() {
        val player = createPlayer(Tile(2655, 4640))
        player.inventory.add("earth_talisman")

        player.tele(2655, 4830)
        tick(2)

        assertTrue(player["journey_to_the_centre_of_the_earth_altar_task", false])
    }

    @Test
    fun `On the Ragged Edge`() {
        val player = createPlayer(Tile(3097, 3468))
        val trapdoor = createObject("trapdoor_80_opened", Tile(3097, 3468))

        player.objectOption(trapdoor, "Climb-down")
        tick(6)

        assertTrue(player["on_the_ragged_edge_task", false])
    }

    @Test
    fun `Making Learning Fun`() {
        val player = createPlayer(Tile(1903, 5222))
        player["warning_stronghold_of_security_ladders"] = 7
        val ladder = GameObjects.find(Tile(1902, 5222), "stronghold_war_ladder_down")

        player.objectOption(ladder, "Climb-down")
        tick(3)

        assertTrue(player["making_learning_fun_task", false])
    }
}
