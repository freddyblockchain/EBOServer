package com.example.game

import broadcastGameState
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.ServerGameObjectConverter
import com.mygdx.game.Managers.AreaManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val tickTime = 1000L / 20// Frame time in milliseconds for 60 FPS
class GameServerMain {
    fun mainLoop() {
        var gameStateNum = 0
        while (true) {
            val startTime = System.currentTimeMillis()

            // Game update logic
            updateGameState()

            // Calculate how long to delay to maintain the frame rate
            val endTime = System.currentTimeMillis()
            val deltaTime = endTime - startTime
            if (deltaTime < tickTime) {
                Thread.sleep(tickTime - deltaTime)
            }
            gameStateNum += 1
        }
    }

    private fun updateGameState() {
        PlayerInputHandler.processActions()
        for(gameObject in AreaManager.getActiveArea()!!.gameObjects.toMutableList()){
            gameObject.frameTask()
        }
        /*val player = SessionManager.playerMap.firstNotNullOfOrNull { it.value}
        player?.move(Vector2(1f,0f))*/
        //println("${player?.currentPosition()}  time is " + System.currentTimeMillis())
        val gameState = calculateGameState()

        val receiveInputScope = CoroutineScope(Dispatchers.IO)
        receiveInputScope.launch {
            for(i in 1..3){
                broadcastGameState(gameState)
                delay(1L)
            }
        }
    }
    fun calculateGameState(): GameState{
        val gameStateObjects = AreaManager.getActiveArea()!!.gameObjects.filter { it is ServerGameObjectConverter }
        val serverGameObjects = gameStateObjects.map {
            val gameStateConverter = it as ServerGameObjectConverter
            gameStateConverter.converToServerGameObject()
        }
        return GameState(serverGameObjects, System.currentTimeMillis())
    }

}