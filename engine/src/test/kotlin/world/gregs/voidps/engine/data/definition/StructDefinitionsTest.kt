package world.gregs.voidps.engine.data.definition

import world.gregs.voidps.cache.config.data.StructDefinition
import world.gregs.voidps.cache.config.decoder.StructDecoder

internal class StructDefinitionsTest : DefinitionsDecoderTest<StructDefinition, StructDecoder, StructDefinitions>() {

    override var decoder: StructDecoder = StructDecoder()
    override lateinit var definitions: Array<StructDefinition>
    override val id: String = "lit_candle"
    override val intId: Int = 34

    override fun expected(): StructDefinition = StructDefinition(
        intId,
        stringId = id,
    )

    override fun empty(): StructDefinition = StructDefinition.EMPTY

    override fun definitions(): StructDefinitions = StructDefinitions.init(definitions)

    override fun load(definitions: StructDefinitions) {
        val uri = StructDefinitionsTest::class.java.getResource("test-struct.toml")!!
        StructDefinitions.load(uri.path)
    }
}
