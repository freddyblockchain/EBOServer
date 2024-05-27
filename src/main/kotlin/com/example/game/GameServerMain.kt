package com.example.game

import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.ServerGameObjectConverter
import com.mygdx.game.Managers.AreaManager
import kotlinx.coroutines.delay

class GameServerMain {
    companion object {
        fun gameTick(): GameState {
            return updateGameState()
        }

        private fun updateGameState(): GameState {
            PlayerInputHandler.processActions()
            for (gameObject in AreaManager.getActiveArea()!!.gameObjects.toMutableList()) {
                gameObject.frameTask()
            }
            return calculateGameState()
        }

        private fun calculateGameState(): GameState {
            val gameStateObjects = AreaManager.getActiveArea()!!.gameObjects.filter { it is ServerGameObjectConverter }
            val serverGameObjects = gameStateObjects.map {
                val gameStateConverter = it as ServerGameObjectConverter
                gameStateConverter.converToServerGameObject()
            }
            return GameState(serverGameObjects, System.currentTimeMillis())
        }
    }
}