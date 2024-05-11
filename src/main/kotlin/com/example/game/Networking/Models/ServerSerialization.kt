package com.example.game.Networking.Models

import PLAYER_STATUS
import kotlinx.serialization.Serializable

enum class GameObjectType{PLAYER, FIREBALL}

@Serializable
data class ServerGameObjectData(val position: Pair<Float, Float>, val unitVectorDirection: Pair<Float,Float>, val speed: Float, val gameObjectNum: Int, val gameObjectType: GameObjectType)

@Serializable
open class ServerGameObject(val serverGameObjectData: ServerGameObjectData)
@Serializable
class PlayerData(val playerGameObjectData: ServerGameObjectData, val status: PLAYER_STATUS): ServerGameObject(playerGameObjectData)

@Serializable
data class FireballData(val fireballGameObjectData: ServerGameObjectData): ServerGameObject(fireballGameObjectData)

@Serializable
data class GameState(val objectStates: List<ServerGameObject>, val gameTime: Long)

interface ServerGameObjectConverter {
    fun converToServerGameObject(): ServerGameObject
}