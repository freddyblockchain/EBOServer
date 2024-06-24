package com.example.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.GameObjectType
import com.mygdx.game.Area.Area
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Projectile
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.ProjectileCollision
import com.mygdx.game.Managers.AreaManager


class Snowball(val gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2, currentArea: Area) : Projectile(gameObjectData, size, unitVectorDirection, currentArea) {
    override var normalSpeed = 3f
    override val gameObjectType = GameObjectType.SNOWBALL
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = RemoveObject()
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true
    override val collision = SnowballCollision(this)
    var lvl = 2

    init {
        setRotation(unitVectorDirection,this,0f)
    }
}

fun getSnowballSize(lvl: Int): Vector2{
    return when(lvl){
        2 -> Vector2(64f,64f)
        1 -> Vector2(32f,32f)
        else -> Vector2(16f,16f)
    }
}

class SnowballCollision(val snowball: Snowball): ProjectileCollision(snowball){
    override fun collisionHappened(collidedObject: GameObject) {
        if(collidedObject is Fireball){
            collidedObject.currentArea.gameObjects.remove(collidedObject)
            when(snowball.lvl){
                0 -> snowball.currentArea.gameObjects.remove(snowball)
                1 -> createNewSnowballs(collidedObject.currentUnitVector, 0, collidedObject)
                2 -> createNewSnowballs(collidedObject.currentUnitVector, 1, collidedObject)
            }
        }
        if(collidedObject is Snowball && snowball.lvl < collidedObject.lvl){
            collidedObject.currentArea.gameObjects.remove(snowball)
        }
        super.collisionHappened(collidedObject)
    }
    fun createNewSnowballs(unitVectorDirection: Vector2, lvl: Int, fireball: Fireball){
        val adjustment = 0.1f // Small adjustment value, you can tune this

        // First snowball direction (approx +10 degrees)
        val newX1 = unitVectorDirection.x + adjustment
        val newY1 = unitVectorDirection.y + adjustment
        val snowball1Direction = Vector2(newX1, newY1).nor()

        // Second snowball direction (approx -10 degrees)
        val newX2 = unitVectorDirection.x - adjustment
        val newY2 = unitVectorDirection.y - adjustment
        val snowball2Direction = Vector2(newX2, newY2).nor()

        val size = getSnowballSize(lvl)
        val snowball1 = Snowball(snowball.gameObjectData, size, snowball1Direction, snowball.currentArea)
        val snowball2 = Snowball(snowball.gameObjectData, size, snowball2Direction, snowball.currentArea)
        snowball1.lvl = lvl
        snowball2.lvl = lvl
        snowball1.setPosition(snowball.currentPosition())
        snowball2.setPosition(snowball.currentPosition())
        snowball1.shooter = fireball.shooter
        snowball2.shooter = fireball.shooter

        snowball.currentArea.gameObjects.remove(snowball)
        snowball.currentArea.gameObjects.addAll(listOf(snowball1, snowball2))

    }
}