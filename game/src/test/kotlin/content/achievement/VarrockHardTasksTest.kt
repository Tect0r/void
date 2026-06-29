package content.achievement

import FakeRandom
import WorldTest
import itemOnItem
import itemOption
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.charge
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.setRandom
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class VarrockHardTasksTest : WorldTest() {

    @Test
    fun `Battle of the Elements`() {
        val player = createPlayer()
        player.levels.set(Skill.Crafting, 100)
        player.inventory.add("battlestaff")
        player.inventory.add("air_orb")

        player.itemOnItem(0, 1)
        tick(3)

        assertTrue(player.inventory.contains("air_battlestaff"))
        assertTrue(player["battle_of_the_elements_task", false])
    }

    @Test
    fun `Battle of the Elements not for other battlestaves`() {
        val player = createPlayer()
        player.levels.set(Skill.Crafting, 100)
        player.inventory.add("battlestaff")
        player.inventory.add("water_orb")

        player.itemOnItem(0, 1)
        tick(3)

        assertTrue(player.inventory.contains("water_battlestaff"))
        assertFalse(player["battle_of_the_elements_task", false])
    }

    @Test
    fun `Waka-Waka-Waka`() {
        val player = createPlayer()

        player["canoe_state_edgeville"] = "waka"

        assertTrue(player["waka_waka_waka_task", false])
    }

    @Test
    fun `Waka-Waka-Waka not for lesser canoes`() {
        val player = createPlayer()

        player["canoe_state_edgeville"] = "stable_dugout"

        assertFalse(player["waka_waka_waka_task", false])
    }

    @Test
    fun `Intersceptre`() {
        val player = createPlayer(Tile(3081, 3421))
        player.inventory.add("skull_sceptre")
        player.inventory.charge(player, player.inventory.indexOf("skull_sceptre"), 9)

        player.itemOption("Invoke", "skull_sceptre")
        tick(2)

        assertTrue(player["intersceptre_task", false])
    }

    @Test
    fun `Burning Bush`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = until
        })
        val tile = Tile(3181, 3356)
        val player = createPlayer(tile)
        player.inventory.add("spade")
        player.levels.set(Skill.Farming, 99)
        player["farming_bush_patch_varrock"] = "poison_ivy_life1"
        val patch = GameObjects.find(tile.addY(1), "farming_bush_patch_varrock")

        player.objectOption(patch, "Pick-from")
        tickIf { player["farming_bush_patch_varrock", "empty"] != "weeds_0" }

        assertTrue(player.inventory.contains("poison_ivy_berries"))
        assertTrue(player["burning_bush_task", false])
    }
}
