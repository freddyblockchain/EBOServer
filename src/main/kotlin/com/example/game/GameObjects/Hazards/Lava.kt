package com.mygdx.game.GameObjects.Hazards

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Layer
import com.mygdx.game.GameObjects.GameObject.GameObject

class Lava(gameObjectData: GameObjectData)
    : GameObject(gameObjectData, Vector2(gameObjectData.width.toFloat(),gameObjectData.height.toFloat())){
    override val collision = CanMoveCollision()
}