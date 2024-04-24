package com.example.game
import GameObjectFactory.GetGameObjectsFromJson
import com.mygdx.game.FileHandler.Companion.getFileJson
import com.mygdx.game.GameObjects.GameObject.GameObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

class JsonParser {
    companion object {
        fun getRoot(fileName: String): Root{
            val objectStrings = getFileJson(fileName)
            val json = Json { ignoreUnknownKeys = true } // Configure as needed
            return json.decodeFromString<Root>(objectStrings)
        }
        fun getGameObjects(root: Root): List<GameObject>{
            return GetGameObjectsFromJson(root.entities, root);
        }
    }
}