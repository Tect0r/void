package content.achievement

import WorldTest
import intEntry
import interfaceOnItem
import interfaceOption
import itemOnItem
import itemOnObject
import npcOption
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.item.Item
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.equipment
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.network.login.protocol.visual.update.player.EquipSlot
import world.gregs.voidps.type.Tile
import kotlin.test.assertTrue

internal class LumbridgeHardTasksTest : WorldTest() {

    @Test
    fun `Building Up Strength`() {
        val player = createPlayer(Tile(3222, 3218))
        player.levels.set(Skill.Magic, 99)
        player.inventory.add("ruby_amulet")
        player.inventory.add("fire_rune", 5)
        player.inventory.add("cosmic_rune")

        player.interfaceOnItem("modern_spellbook", "enchant_level_3", Item("ruby_amulet"), 0)
        tick(1)

        assertTrue(player.inventory.contains("amulet_of_strength"))
        assertTrue(player["building_up_strength_task", false])
    }

    @Test
    fun `Have Your Cake and Eat It`() {
        val player = createPlayer(Tile(3208, 3214))
        player.levels.set(Skill.Cooking, 50)
        player.inventory.add("cake")
        player.inventory.add("chocolate_dust")

        player.itemOnItem(0, 1)
        tick(1)
        player["skill_creation_amount"] = 1
        player.intEntry(0)
        tick(2)

        assertTrue(player.inventory.contains("chocolate_cake"))
        assertTrue(player["have_your_cake_and_eat_it_task", false])
    }

    @Test
    fun `Not Waving But Drowning`() {
        val player = createPlayer(Tile(3483, 4834))
        player.levels.set(Skill.Runecrafting, 99)
        player.inventory.add("pure_essence", 27)
        val altar = GameObjects.find(Tile(3483, 4835), "water_altar")

        player.itemOnObject(altar, 0)
        tick(1)
        tickIf { player.visuals.moved }

        assertTrue(player.inventory.count("water_rune") >= 100)
        assertTrue(player["not_waving_but_drowning_task", false])
    }

    @Test
    fun `Blast and Hellfire`() {
        val player = createPlayer(Tile(3250, 3240))
        val goblin = createNPC("goblin_turquoise_grey_ponytail", Tile(3250, 3242))
        player.levels.set(Skill.Magic, 99)
        player.equipment.set(EquipSlot.Weapon.index, "staff_of_fire")
        player.inventory.add("air_rune", 100)
        player.inventory.add("fire_rune", 100)
        player.inventory.add("death_rune", 100)

        player.interfaceOption("modern_spellbook", "fire_blast", option = "Autocast")
        player.npcOption(goblin, "Attack")
        tickIf(limit = 50) { !player["blast_and_hellfire_task", false] }

        assertTrue(player["blast_and_hellfire_task", false])
    }

    @Test
    fun `A Body in the Sewers`() {
        val player = createPlayer(Tile(3113, 9689))
        player.levels.set(Skill.Smithing, 99)
        player.inventory.add("hammer")
        player.inventory.add("mithril_bar", 5)
        val anvil = GameObjects.find(Tile(3112, 9689), "anvil")

        player.itemOnObject(anvil, 1)
        tick()

        player.interfaceOption("smithing", "platebody_1", "Make 1 Platebody", optionIndex = 0)
        tickIf(limit = 20) { !player.inventory.contains("mithril_platebody") }

        assertTrue(player.inventory.contains("mithril_platebody"))
        assertTrue(player["a_body_in_the_sewers_task", false])
    }

    @Test
    fun `Are You As Fired Up As I Am`() {
        val player = createPlayer(Tile(3229, 3218, 2))
        player.levels.set(Skill.Firemaking, 99)
        player.inventory.add("tinderbox")
        player.inventory.add("yew_logs", 5)

        player.itemOnItem(0, 1)
        tickIf(limit = 20) { !player["are_yew_as_fired_up_as_i_am_task", false] }

        assertTrue(player["are_yew_as_fired_up_as_i_am_task", false])
    }

    @Test
    fun `Gods Give Me Strength`() {
        val player = createPlayer(Tile(3244, 3207))
        player.addVarbit("activated_prayers", "mystic_might")
        val altar = GameObjects.find(Tile(3243, 3206), "prayer_altar_lumbridge")

        player.objectOption(altar, "Pray")
        tick()

        assertTrue(player["gods_give_me_strength_task", false])
    }
}
