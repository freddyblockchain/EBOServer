package com.example.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.GameObjectType
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Projectile

class Snowball(gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2) : Projectile(gameObjectData, size, unitVectorDirection) {
    override var normalSpeed = 4f
    override val gameObjectType = GameObjectType.SNOWBALL
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = RemoveObject()
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true

    init {
        setRotation(unitVectorDirection,this,0f)
    }
}