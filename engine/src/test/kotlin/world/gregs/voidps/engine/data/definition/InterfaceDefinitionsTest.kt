package world.gregs.voidps.engine.data.definition

import org.junit.jupiter.api.BeforeEach
import world.gregs.voidps.cache.definition.data.InterfaceDefinition
import world.gregs.voidps.cache.definition.decoder.InterfaceDecoder

internal class InterfaceDefinitionsTest : DefinitionsDecoderTest<InterfaceDefinition, InterfaceDecoder, InterfaceDefinitions>() {

    override var decoder: InterfaceDecoder = InterfaceDecoder()
    override lateinit var definitions: Array<InterfaceDefinition>
    override val id: String = "test_interface"
    override val intId: Int = 34

    /**
     * Unlike the other decoders the interface loader guards with
     * `require(definitions[id].stringId == "")`, so the slot must stay blank
     * and be filled by [load] instead of being pre-seeded with [expected].
     */
    @BeforeEach
    override fun setup() {
        definitions = decoder.create(intId + 1)
    }

    override fun expected(): InterfaceDefinition = InterfaceDefinition(
        intId,
        stringId = id,
    )

    override fun empty(): InterfaceDefinition = InterfaceDefinition.EMPTY

    override fun definitions(): InterfaceDefinitions = InterfaceDefinitions.init(definitions)

    override fun load(definitions: InterfaceDefinitions) {
        val config = InterfaceDefinitionsTest::class.java.getResource("test-interface.toml")!!
        val types = InterfaceDefinitionsTest::class.java.getResource("test-interface-types.toml")!!
        definitions.load(listOf(config.path), types.path)
    }
}
