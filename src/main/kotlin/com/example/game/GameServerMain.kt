package com.example.game

import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.Sessions.SessionManager.Companion.playerTimeMap
import com.example.Sessions.SessionManager.Companion.removePlayer
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.PlayerEvent
import com.example.game.Networking.Models.ServerGameObjectConverter
import com.mygdx.game.Managers.AreaManager
import java.util.UUID

val frameTime = 20
val tickTime = 1000L / frameTime// Frame time in milliseconds
val firstUUID = UUID.randomUUID().toString()
var activeUUIDS = mutableListOf<String>(firstUUID)
var globalGameState = GameState(listOf(),0, listOf(), firstUUID)
var delayTime = 0L
var id = 0
val playerEvents = mutableListOf<PlayerEvent>()
val eventDeletionTime = 150

val maxActiveUUIDs = 5
class GameServerMain {
    fun mainLoop() {
        while (true) {
            val startTime = System.currentTimeMillis()

            //remove old events
            for(playerEvent in playerEvents.toMutableList()){
                if(System.currentTimeMillis() > (playerEvent.timestamp + eventDeletionTime)){
                    playerEvents.remove(playerEvent)
                }
            }
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
        val originalObjects = AreaManager.getActiveArea()!!.gameObjects
        val activeGameObjects = AreaManager.getActiveArea()!!.gameObjects.toMutableList()
        for(gameObject in activeGameObjects){
            //Check to see if other game objects removed the object
            if(gameObject in originalObjects){
                gameObject.frameTask()
            }
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
        val nextUUID = UUID.randomUUID().toString()
        activeUUIDS.add(nextUUID)
        if(activeUUIDS.size > maxActiveUUIDs){
            activeUUIDS.removeAt(0)
        }
        return GameState(serverGameObjects, System.currentTimeMillis(), playerEvents, nextUUID)
    }
}