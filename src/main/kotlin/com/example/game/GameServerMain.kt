package com.example.game

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
    }
}