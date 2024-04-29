package com.example.game

import broadcastGameState
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.Models.PlayerServerData
import com.mygdx.game.Managers.AreaManager

val tickTime = 1000L / 2// Frame time in milliseconds for 60 FPS
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
        val player = SessionManager.playerMap.firstNotNullOfOrNull { it.value}
        player?.move(Vector2(1f,0f))
        //println("${player?.currentPosition()}  time is " + System.currentTimeMillis())
        val gameState = calculateGameState()
        broadcastGameState(gameState)
        /*for(i in 1 .. 3){
            runBlocking {
                broadcastGameState(gameState)
                delay(1L)
            }
        }*/

    }
    fun calculateGameState(): GameState{
        val gameStateMap = SessionManager.playerMap.mapValues { (key, value) ->
            val playerPos = value.currentPosition()
            PlayerServerData(Pair(playerPos.x, playerPos.y), value.state, value.playerNum, value.speed, Pair(value.currentUnitVector.x, value.currentUnitVector.y))
        }
        return GameState(gameStateMap, System.currentTimeMillis())
    }

}