
import com.example.game.Entities
import com.example.game.GameObjectData
import com.example.game.Root
import com.mygdx.game.Area.Area
import com.mygdx.game.GameObjects.GameObject.GameObject
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object GameObjectFactory {
    private val registry = mutableMapOf<String, (GameObjectData, Area) -> GameObject>()

    fun register(type: String, constructorFunction: (GameObjectData, Area) -> GameObject) {
        registry[type] = constructorFunction
    }

    private fun create(data: GameObjectData, root: Root, area: Area): GameObject? {
        val constructor = registry[data.id]
        data.x += root.x
        data.y = root.height - data.y - data.height
        data.y += (-root.y) - root.height
        return constructor?.invoke(data, area)
    }

    fun GetGameObjectsFromJson(entities: Entities, root: Root, area: Area): List<GameObject> {
        val allEntities = mutableListOf<GameObject>()
        Entities::class.memberProperties.forEach { property ->
            property.isAccessible = true // Make sure we can access the property

            val value = property.get(entities) // Get the property value from the entities instance

            if (value is List<*>) { // Check if the value is a List
                value.forEach { item ->
                    if (item is GameObjectData) { // Check if the item implements GameObjectData
                        // At this point, item is safely cast to GameObjectData
                        // You can now add item to your List<GameObjectData>
                        allEntities.add(create(item, root, area)!!)
                    }
                }
            }
        }

        return allEntities
    }
}