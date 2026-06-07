package content.achievement

import FakeRandom
import WorldTest
import dialogueContinue
import dialogueOption
import itemOnItem
import npcOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.equipment
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.engine.inv.replace
import world.gregs.voidps.network.login.protocol.visual.update.player.EquipSlot
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.setRandom
import kotlin.test.assertTrue

internal class LumbridgeEasyTasksTest : WorldTest() {

    @Test
    fun `And It Was THIS Big`() {
        val player = createPlayer(Tile(3239, 3252))
        player.levels.set(Skill.Fishing, 100)
        val fishingSpot = createNPC("fishing_spot_lure_bait_lumbridge", Tile(3238, 3252))
        player.inventory.add("fishing_rod", "fishing_bait")

        player.npcOption(fishingSpot, "Bait")
        tick(7)

        assertTrue(player["and_it_was_this_big_task", false])
    }

    @Test
    fun `Belter of a Smelter`() {
        val player = createPlayer(Tile(3225, 3254))
        player.softTimers.start("smelting")

        player.inventory.add("steel_bar")

        assertTrue(player["belter_of_a_smelter_task", false])
    }

    @Test
    fun `Slippery When Wet`() {
        val player = createPlayer()
        player.softTimers.start("runecrafting")

        player.inventory.add("water_rune")

        assertTrue(player["slippery_when_wet_task", false])
    }

    @Test
    fun `Ratatouille`() {
        val player = createPlayer(Tile(3200, 3185))
        player.softTimers.start("cooking")
        player.inventory.add("raw_rat_meat")

        player.inventory.replace("raw_rat_meat", "rat_meat")

        assertTrue(player["ratatouille_task", false])
    }

    @Test
    fun `Camping Trip`() {
        val player = createPlayer(Tile(3200, 3185))
        player.levels.set(Skill.Firemaking, 100)
        player.inventory.add("tinderbox", "logs")

        player.itemOnItem(0, 1)
        tick(6)

        assertTrue(player["camping_trip_task", false])
    }

    @Test
    fun `You Doity Rat`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = until
            override fun nextInt(from: Int, until: Int) = until
        })
        val player = createPlayer(Tile(3200, 3185))
        val rat = createNPC("giant_rat", Tile(3201, 3185))

        player.equipment.set(EquipSlot.Weapon.index, "dragon_longsword")
        player.levels.set(Skill.Attack, 100)
        player.levels.set(Skill.Strength, 100)

        player.npcOption(rat, "Attack")
        tick(10)

        assertTrue(player["you_doity_rat_task", false])
    }

    @Test
    fun `Money Down the Drayn`() {
        val player = createPlayer(Tile(3093, 3243))

        player.open("bank")
        tick()

        assertTrue(player["money_down_the_drayn_task", false])
    }

    @Test
    fun `Klept-Old-Man-ia`() {
        val player = createPlayer(Tile(3088, 3254))
        player["wise_old_man_met"] = true
        val wiseOldMan = createNPC("wise_old_man_draynor", Tile(3088, 3255))

        player.npcOption(wiseOldMan, "Talk-to")
        tick()
        player.dialogueContinue() // "Greetings, name."
        player.dialogueOption("line2") // "Could you check my items for junk, please?"
        player.dialogueContinue() // player echoes the option
        player.dialogueOption("line1") // "Could you check my bank for junk, please?"
        player.dialogueContinue() // player echoes the option
        player.dialogueContinue() // "Certainly, but I should warn you..." then the task is set

        assertTrue(player["klept_old_man_ia_task", false])
    }
}
