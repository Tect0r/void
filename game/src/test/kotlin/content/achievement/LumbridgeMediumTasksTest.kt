package content.achievement

import FakeRandom
import WorldTest
import dialogueContinue
import dialogueOption
import interfaceOption
import itemOnItem
import itemOnNpc
import itemOnObject
import npcOption
import objectOption
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.client.ui.dialogue
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.setRandom
import kotlin.test.assertTrue

internal class LumbridgeMediumTasksTest : WorldTest() {

    @Test
    fun `Everybody Loves Coal`() {
        setRandom(object : FakeRandom() {
            // until == 256 is the mining success roll. Return 1 so the gem
            // pre-roll (chance 1) fails but the ore roll (chance > 1) succeeds.
            override fun nextInt(until: Int) = if (until == 256) 1 else 0
        })
        val player = createPlayer(Tile(3145, 3150))
        player.levels.set(Skill.Mining, 100)
        val rocks = createObject("coal_rocks_rock_1", Tile(3145, 3151))
        player.inventory.add("bronze_pickaxe")

        player.objectOption(rocks, "Mine")
        tickIf { !player.inventory.contains("coal") }

        assertTrue(player.inventory.contains("coal"))
        assertTrue(player["everybody_loves_coal_task", false])
    }

    @Test
    fun `Hi Ho Silver`() {
        setRandom(object : FakeRandom() {
            // until == 256 is the mining success roll. Return 1 so the gem
            // pre-roll (chance 1) fails but the ore roll (chance > 1) succeeds.
            override fun nextInt(until: Int) = if (until == 256) 1 else 0
        })
        val player = createPlayer(Tile(3300, 3300))
        player.levels.set(Skill.Mining, 100)
        val rocks = createObject("silver_rocks_rock_1", Tile(3300, 3301))
        player.inventory.add("bronze_pickaxe")

        player.objectOption(rocks, "Mine")
        tickIf { !player.inventory.contains("silver_ore") }

        assertTrue(player.inventory.contains("silver_ore"))
        assertTrue(player["hi_ho_silver_task", false])
    }

    @Test
    fun `One Day You Shall Be a Fork`() {
        setRandom(object : FakeRandom() {
            override fun nextInt(until: Int) = 0
        })
        val player = createPlayer(Tile(3227, 3255))
        player.levels.set(Skill.Smithing, 100)
        val furnace = GameObjects.find(Tile(3226, 3256), "furnace_lumbridge")
        player.inventory.add("silver_ore")

        player.itemOnObject(furnace, 0)
        tick()
        player.interfaceOption("skill_creation_amount", "increment")
        player.dialogueOption(id = "dialogue_skill_creation", component = "choice1")
        tick(4)

        assertTrue(player.inventory.contains("silver_bar"))
        assertTrue(player["one_day_you_shall_be_a_fork_task", false])
    }

    @Test
    fun `Made to Order`() {
        val player = createPlayer(Tile(3227, 3255))
        player.levels.set(Skill.Crafting, 100)
        val furnace = GameObjects.find(Tile(3226, 3256), "furnace_lumbridge")
        player.inventory.add("silver_bar")
        player.inventory.add("holy_mould")

        player.itemOnObject(furnace, 0)
        tick()
        player.interfaceOption("silver_mould", "holy_mould_button", "Make 1")
        tick(3)

        assertTrue(player.inventory.contains("unstrung_symbol"))
        assertTrue(player["made_to_order_task", false])
    }

    @Test
    fun `A Meal Fit For a Duke`() {
        val player = createPlayer(Tile(3208, 3214))
        player.levels.set(Skill.Cooking, 100)
        player.inventory.add("raw_lobster")
        val range = createObject("cooking_range_lumbridge_castle", Tile(3208, 3215))

        player.itemOnObject(range, 0)
        tickIf { player.inventory.contains("raw_lobster") }

        assertTrue(player.inventory.contains("lobster"))
        assertTrue(player["a_meal_fit_for_a_duke_task", false])
    }

    @Test
    fun `Where's the Beef`() {
        val player = createPlayer(Tile(3180, 3321))
        val bill = createNPC("beefy_bill", Tile(3180, 3320))
        player.inventory.add("raw_beef", 12)

        player.itemOnNpc(bill, 0)
        tickIf { player.dialogue == null }
        player.dialogueOption(1) // "Bank 10, Bill keeps 2."

        assertTrue(player["wheres_the_beef_task", false])
    }

    @Test
    fun `Always Be Prepared`() {
        val player = createPlayer(Tile(3211, 3220, 1))
        player["dragon_slayer"] = "completed"
        val duke = createNPC("duke_horacio", Tile(3212, 3220, 1))

        player.npcOption(duke, "Talk-to")
        tick()
        player.dialogueContinue()
        player.dialogueOption(3)
        player.dialogueContinue()

        assertTrue(player["always_be_prepared_task", false])
        assertTrue(player.inventory.contains("anti_dragon_shield"))
    }

    @Test
    fun `Weeping Willow`() {
        val player = createPlayer(Tile(3233, 3230))
        player.levels.set(Skill.Woodcutting, 100)
        val willow = createObject("willow_2", Tile(3234, 3230))
        player.inventory.add("rune_hatchet")

        player.objectOption(willow, "Chop down")
        tickIf { !player.inventory.contains("willow_logs") }

        assertTrue(player.inventory.contains("willow_logs"))
        assertTrue(player["weeping_willow_task", false])
    }

    @Test
    fun `Steel Justice`() {
        val player = createPlayer(Tile(3111, 9689))
        player.levels.set(Skill.Smithing, 99)
        player.inventory.add("hammer")
        player.inventory.add("steel_bar", 2)
        val anvil = createObject("anvil", Tile(3112, 9689))

        player.itemOnObject(anvil, 1)
        tick()
        player.interfaceOption("smithing", "longsword_1", "Make 1 Longsword", optionIndex = 0)
        tick(5)

        assertTrue(player.inventory.contains("steel_longsword"))
        assertTrue(player["steel_justice_task", false])
    }

    @Test
    fun `Willow the Wisp of Smoke`() {
        val start = Tile(3228, 3218, 2)
        val player = createPlayer(start)
        player.levels.set(Skill.Firemaking, 100)
        player.inventory.add("tinderbox")
        player.inventory.add("willow_logs", 5)

        player.itemOnItem(0, 1) // tinderbox on willow logs
        tickIf { player.tile == start }

        assertTrue(player["willow_the_wisp_of_smoke_task", false])
    }

    @Test
    fun `Ease of Access`() {
        val player = createPlayer()
        player.levels.set(Skill.Magic, 99)
        player.inventory.add("law_rune", 5)
        player.inventory.add("earth_rune", 5)
        player.inventory.add("air_rune", 5)

        player.interfaceOption("modern_spellbook", "lumbridge_teleport", "Cast")
        tick(10)

        assertTrue(player["ease_of_access_task", false])
    }
}
