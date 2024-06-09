package com.example.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.GameObjectType
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Projectile
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.ProjectileCollision
import com.mygdx.game.Managers.AreaManager

class Icicle(gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2) : Projectile(gameObjectData, size, unitVectorDirection) {
    override var normalSpeed = 8f
    override val gameObjectType = GameObjectType.ICICLE
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = RemoveObject()
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true
    override val collision = IcicleCollision(this)

    init {
        setRotation(unitVectorDirection,this,0f)
    }
}

class IcicleCollision(private val icicle: Icicle): ProjectileCollision(icicle){
    override fun collisionHappened(collidedObject: GameObject) {
        super.collisionHappened(collidedObject)
        if(collidedObject is Projectile){
            AreaManager.getActiveArea()!!.gameObjects.remove(icicle)
        }
    }
}