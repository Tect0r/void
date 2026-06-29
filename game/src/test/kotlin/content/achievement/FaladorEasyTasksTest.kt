package content.achievement

import FakeRandom
import WorldTest
import kotlinx.coroutines.test.runTest
import npcOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inv.equipment
import world.gregs.voidps.network.login.protocol.visual.update.player.EquipSlot
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.setRandom
import kotlin.test.assertTrue

internal class FaladorEasyTasksTest : WorldTest() {

    override var loadNpcs = true

    @Test
    fun `It's Not Wabbit Season`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = until
            override fun nextInt(from: Int, until: Int) = until
        })
        val player = createPlayer(Tile(2989, 3379))
        val duck = NPCs.first(Tile(2990, 3379)) { it.id.startsWith("duck") }

        player.equipment.set(EquipSlot.Weapon.index, "dragon_longsword")
        player.levels.set(Skill.Attack, 100)
        player.levels.set(Skill.Strength, 100)
        player.levels.set(Skill.Defence, 100)

        player.npcOption(duck, "Attack")
        tick(10)

        assertTrue(player["its_not_wabbit_season_task", false])
    }

    @Test
    fun `Stand and Deliver`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = until
            override fun nextInt(from: Int, until: Int) = until
        })
        val player = createPlayer(Tile(3048, 3276))
        val highwayman = NPCs.first(Tile(3049, 3276)) { it.id == "highwayman_falador" }

        player.equipment.set(EquipSlot.Weapon.index, "dragon_longsword")
        player.levels.set(Skill.Attack, 100)
        player.levels.set(Skill.Strength, 100)
        player.levels.set(Skill.Defence, 100)
        player.levels.set(Skill.Constitution, 990)

        player.npcOption(highwayman, "Attack")
        tick(30)

        assertTrue(player["stand_and_deliver_task", false])
    }

    @Test
    fun `Sniffing Out the Mole`() = runTest {
        val player = createPlayer(Tile(2989, 3378))

        player.tele(1752, 5237)
        tick(2)

        assertTrue(player["sniffing_out_the_mole_task", false])
    }

    @Test
    fun `Going Along With the 'Fro`() {
        val player = createPlayer(Tile(3051, 3373))
        val pete = NPCs.first(Tile(3052, 3373)) { it.id == "party_pete" }

        player.npcOption(pete, "Talk-to")
        tick(2)

        assertTrue(player["going_along_with_the_fro_task", false])
    }
}
