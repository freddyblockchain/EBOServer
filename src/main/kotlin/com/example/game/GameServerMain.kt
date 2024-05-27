package com.example.game

import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.Sessions.SessionManager.Companion.playerTimeMap
import com.example.Sessions.SessionManager.Companion.removePlayer
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.ServerGameObjectConverter
import com.mygdx.game.Managers.AreaManager

val frameTime = 20
val tickTime = 1000L / frameTime// Frame time in milliseconds
var globalGameState = GameState(listOf(),0)
var delayTime = 0L
class GameServerMain {
    fun mainLoop() {
        while (true) {
            val startTime = System.currentTimeMillis()

            // Game update logic
            updateGameState()

            // Calculate how long to delay to maintain the frame rate
            val endTime = System.currentTimeMillis()
            val deltaTime = endTime - startTime
            if (deltaTime < tickTime) {
                delayTime = tickTime - deltaTime
                Thread.sleep(delayTime)
            }
        }
    }

    private fun updateGameState() {
        PlayerInputHandler.processActions()
        for(gameObject in AreaManager.getActiveArea()!!.gameObjects.toMutableList()){
            gameObject.frameTask()
        }
        playerMap.toMutableMap().forEach { entry ->
            if (playerTimeMap[entry.key] != null && System.currentTimeMillis() > playerTimeMap[entry.key]!!) {
                println("time passed removing player")
                removePlayer(entry.key)
            }
        }
        globalGameState = calculateGameState()
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