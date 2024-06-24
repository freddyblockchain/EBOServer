package com.mygdx.game.GameObjects

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Area.Area
import com.mygdx.game.Enums.Layer
import com.mygdx.game.GameObjects.GameObject.GameObject

class Ground(gameObjectData: GameObjectData, size: Vector2, textureName: String, currentArea: Area) : GameObject(gameObjectData, size, currentArea) {
}