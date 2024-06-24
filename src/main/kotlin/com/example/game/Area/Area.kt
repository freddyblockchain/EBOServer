package com.mygdx.game.Area

import com.example.game.Networking.Models.GameState
import com.mygdx.game.GameObjects.GameObject.GameObject

class Area(val areaIdentifier: String, val version: Int) {
    val gameObjects = mutableListOf<GameObject>()
    val levelList = mutableListOf<String>()

    var gameState = GameState(listOf(), -1, listOf(), "")
}