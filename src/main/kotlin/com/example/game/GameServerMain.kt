package com.example.game

import broadcastGameState
import com.example.Sessions.SessionManager
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.PlayerServerData
import com.mygdx.game.Managers.AreaManager

class GameServerMain {
    fun mainLoop() {
        val frameTime = 1000L / 60  // Frame time in milliseconds for 60 FPS
        while (true) {
            val startTime = System.currentTimeMillis()

            // Game update logic
            updateGameState()

            // Calculate how long to delay to maintain the frame rate
            val endTime = System.currentTimeMillis()
            val deltaTime = endTime - startTime
            if (deltaTime < frameTime) {
                Thread.sleep(frameTime - deltaTime)
            }
        }
    }

    private fun updateGameState() {
        //Input first
        //inputProcessor.handleInput()
        for(gameObject in AreaManager.getActiveArea()!!.gameObjects.toMutableList()){
            gameObject.frameTask()
        }
        PlayerInputHandler.processActions()

        val gameState = calculateGameState()
        broadcastGameState(gameState)
    }
    fun calculateGameState(): GameState{
        val gameStateMap = SessionManager.playerMap.mapValues { (key, value) ->
            val playerPos = value.currentPosition()
            PlayerServerData(Pair(playerPos.x, playerPos.y), value.state, value.playerNum)
        }
        return GameState(gameStateMap)
    }
}