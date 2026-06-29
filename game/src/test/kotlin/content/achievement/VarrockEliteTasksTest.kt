package content.achievement

import WorldTest
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Tile
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class VarrockEliteTasksTest : WorldTest() {

    private val earthAltarTile = Tile(2657, 4840)

    @Test
    fun `A Ton of Earth - craft 100+ earth runes simultaneously`() {
        // 28 pure essence at level 99 yields a 4x multiplier = 112 earth runes in one action.
        val player = createPlayer(earthAltarTile.addY(-1))
        player.levels.set(Skill.Runecrafting, 99)
        player.inventory.add("pure_essence", 28)

        val altar = GameObjects.find(earthAltarTile, "earth_altar")
        player.objectOption(altar, "Craft-rune")
        tick(1)
        tickIf { player.visuals.moved }

        assertTrue(player.inventory.count("earth_rune") >= 100)
        assertTrue(player["a_ton_of_earth_task", false])
    }

    @Test
    fun `A Ton of Earth not awarded for fewer than 100 earth runes`() {
        // 10 pure essence at level 99 yields 4x = 40 earth runes, below the threshold.
        val player = createPlayer(earthAltarTile.addY(-1))
        player.levels.set(Skill.Runecrafting, 99)
        player.inventory.add("pure_essence", 10)

        val altar = GameObjects.find(earthAltarTile, "earth_altar")
        player.objectOption(altar, "Craft-rune")
        tick(1)
        tickIf { player.visuals.moved }

        assertTrue(player.inventory.count("earth_rune") in 1..99)
        assertFalse(player["a_ton_of_earth_task", false])
    }
}
