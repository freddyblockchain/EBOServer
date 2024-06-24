package com.example.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.GameObjectType
import com.mygdx.game.Area.Area
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Projectile


class Fireball(gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2, currentArea: Area) : Projectile(gameObjectData, size, unitVectorDirection, currentArea) {
    override var normalSpeed = 6f
    override val gameObjectType = GameObjectType.FIREBALL
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = RemoveObject()
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true

    init {
        setRotation(unitVectorDirection,this,0f)
    }

    override fun frameTask() {
        super.frameTask()
        val breaky = 0
        println(breaky)
    }
}