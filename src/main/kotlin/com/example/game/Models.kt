package com.example.game
import com.mygdx.game.GameObjects.Hazards.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class Root(val x: Int, val y: Int, val entities: Entities, val width: Int, val height: Int, val uniqueIdentifer: String, val identifier: String, val customFields: RootCustomFields)

@Serializable
data class RootCustomFields(val World: String)
@Serializable
data class Entities(
    val Lava: List<GameObjectData> = listOf(),
)
fun initMappings(){
    GameObjectFactory.register("Lava", ::Lava)
}
@Serializable
open class GameObjectData( var x: Int = 0,
                           var y: Int = 0,
                           val iid: String = "",
                           val id: String = "",
                           val width: Int = 0,
                           val height: Int = 0,
                           val customFields: JsonElement = JsonObject(emptyMap()))

@Serializable
data class EntityRefData(val entityIid: String, val layerIid: String, val levelIid: String, val worldIid: String)