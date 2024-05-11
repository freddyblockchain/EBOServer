package com.example.game.Managers

class GameObjectNumManager {
    companion object {
        private var gameObjectNum = 0

        fun getNextGameNum(): Int{
            gameObjectNum += 1
            return gameObjectNum
        }
    }
}