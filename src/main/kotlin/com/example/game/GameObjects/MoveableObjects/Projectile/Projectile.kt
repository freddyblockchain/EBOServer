package com.mygdx.game.GameObjects.MoveableObjects.Projectile

import Player
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Managers.GameObjectNumManager
import com.example.game.Networking.GameServerInit
import com.example.game.Networking.Models.*
import com.mygdx.game.Area.Area
import com.mygdx.game.Collition.MoveCollision
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.minus
import com.mygdx.game.plus
import com.mygdx.game.times

abstract class Projectile(gameObjectData: GameObjectData, size: Vector2, var unitVectorDirection: Vector2, currentArea: Area) : MoveableObject(gameObjectData, size, currentArea), ServerGameObjectConverter{

    abstract val gameObjectType: GameObjectType
    override val collision = ProjectileCollision(this)
    var shooter: Player? = null
    val gameObjectNum = GameObjectNumManager.getNextGameNum()
    override fun frameTask() {
        super.frameTask()
        this.move(unitVectorDirection)
    }

    override fun converToServerGameObject(): ServerGameObject {
        return ServerGameObject(DefaultMoveableObjectData(gameObjectNum, this.gameObjectType))
    }
}

fun Player.shootProjectile(projectile: Projectile){
    val area = projectile.currentArea
    val projectileStartPos = this.currentMiddle + (projectile.unitVectorDirection * 50f) - Vector2(projectile.size.x / 2,projectile.size.y / 2)
    projectile.setPosition(projectileStartPos)
    projectile.shooter = this
    area.gameObjects.add(projectile)
}

open class ProjectileCollision(val projectile: Projectile): MoveCollision() {

    override var canMoveAfterCollision = true
    override fun collisionHappened(collidedObject: GameObject) {
        if(collidedObject is Player && projectile.shooter != collidedObject){
            collidedObject.isHit(projectile.currentUnitVector)
            projectile.currentArea.gameObjects.remove(projectile)
            collidedObject.lastAttacker = projectile.shooter
        }
    }
}