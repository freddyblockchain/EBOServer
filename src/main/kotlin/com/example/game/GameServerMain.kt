package com.example.game

import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.Sessions.SessionManager.Companion.playerTimeMap
import com.example.Sessions.SessionManager.Companion.removePlayer
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.PlayerEvent
import com.example.game.Networking.Models.ServerGameObjectConverter
import com.mygdx.game.Area.Area
import com.mygdx.game.Managers.AreaManager
import java.util.UUID

val frameTime = 20
val tickTime = 1000L / frameTime// Frame time in milliseconds
val firstUUID = UUID.randomUUID().toString()
var activeUUIDS = mutableListOf<String>(firstUUID)
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
        //for areas in AreaManager.getAreas.

        playerMap.toMutableMap().forEach { entry ->
            if (playerTimeMap[entry.key] != null && System.currentTimeMillis() > playerTimeMap[entry.key]!!) {
                println("time passed removing player")
                removePlayer(entry.key)
            }
        }

        val nextUUID = UUID.randomUUID().toString()
        activeUUIDS.add(nextUUID)

        for(area in AreaManager.areas){
            PlayerInputHandler.processActions()
            val originalObjects = area.gameObjects
            val activeGameObjects = area.gameObjects.toMutableList()
            for(gameObject in activeGameObjects){
                //Check to see if other game objects removed the object
                if(gameObject in originalObjects){
                    gameObject.frameTask()
                }
            }
            area.gameState = calculateGameState(area, nextUUID)
        }
    }
    private fun calculateGameState(area: Area, nextUUID: String): GameState {
        val gameStateObjects = area.gameObjects.filter { it is ServerGameObjectConverter }
        val serverGameObjects = gameStateObjects.map {
            val gameStateConverter = it as ServerGameObjectConverter
            gameStateConverter.converToServerGameObject()
        }
        if(activeUUIDS.size > maxActiveUUIDs){
            activeUUIDS.removeAt(0)
        }
        return GameState(serverGameObjects, System.currentTimeMillis(), playerEvents, nextUUID)
    }
}