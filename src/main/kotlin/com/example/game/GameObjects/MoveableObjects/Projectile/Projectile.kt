package com.mygdx.game.GameObjects.MoveableObjects.Projectile

import Player
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Managers.GameObjectNumManager
import com.example.game.Networking.GameServerInit
import com.example.game.Networking.Models.*
import com.mygdx.game.Collition.MoveCollision
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.minus
import com.mygdx.game.plus
import com.mygdx.game.times

abstract class Projectile(gameObjectData: GameObjectData, size: Vector2, var unitVectorDirection: Vector2) : MoveableObject(gameObjectData, size), ServerGameObjectConverter{

    override val collision = ProjectileCollision(this)
    val gameObjectNum = GameObjectNumManager.getNextGameNum()
    override fun frameTask() {
        super.frameTask()
        this.move(unitVectorDirection)
    }

    override fun converToServerGameObject(): ServerGameObject {
        return FireballData(DefaultMoveableObjectData(gameObjectNum, GameObjectType.FIREBALL))
    }
}

fun Player.shootProjectile(projectile: Projectile){
    val area = AreaManager.getActiveArea()!!
    val projectileStartPos = this.currentPosition() + (projectile.unitVectorDirection * 100f) - Vector2(projectile.size.x / 2,projectile.size.y / 2)
    projectile.setPosition(projectileStartPos)
    area.gameObjects.add(projectile)
}

class ProjectileCollision(val projectile: Projectile): MoveCollision() {

    override var canMoveAfterCollision = true
    override fun collisionHappened(collidedObject: GameObject) {
        if(collidedObject is Player){
            AreaManager.getActiveArea()!!.gameObjects.remove(projectile)
        }
    }
}