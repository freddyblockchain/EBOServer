
package com.mygdx.game.GameObjects.GameObject
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.GameObjectType
import com.example.game.Networking.Models.ServerGameObjectData
import com.mygdx.game.Area.Area
import com.mygdx.game.CannotMoveStrategy.CannotMoveStrategy
import com.mygdx.game.Collition.CollisionType
import com.mygdx.game.Managers.CollitionManager.Companion.entityWithinLocations
import com.mygdx.game.Managers.CollitionManager.Companion.handleMoveCollisions
import com.mygdx.game.plus
import com.mygdx.game.times

abstract class MoveableObject(gameObjectData: GameObjectData, size: Vector2, currentArea: Area) :
    GameObject(gameObjectData, size, currentArea), RotationalObject by DefaultRotationalObject(),
    DirectionalObject {
    abstract var currentSpeed: Float
    abstract var normalSpeed: Float
    abstract val cannotMoveStrategy: CannotMoveStrategy
    private var canMove = true
    var currentUnitVector: Vector2 = Vector2(0f,0f)

    open fun move(newUnitVector: Vector2): Boolean {
        if (canMove) {
            val nextIncrement = newUnitVector * this.currentSpeed
            currentUnitVector = newUnitVector
            val moveSuccessfull = moveObject(nextIncrement)
            return moveSuccessfull
        } else {
            return false
        }
    }

    fun freezeMoving() {
        canMove = false
    }

    fun enableMoving() {
        canMove = true
    }

    fun canMove(): Boolean {
        return canMove
    }

    fun stopMovement(){
        currentUnitVector = Vector2(0f,0f)
    }

    fun moveObject(movementIncrement: Vector2): Boolean{
        val sprite = this.sprite
        val polygonToCheck = Polygon(this.polygon.transformedVertices + movementIncrement)
        val moveCollisionObjects = this.currentArea.gameObjects.filter { it.collision.collitionType == CollisionType.MOVE }
        val canMove = handleMoveCollisions(this,polygonToCheck,moveCollisionObjects)
        val inLocation = entityWithinLocations(polygonToCheck, this.currentArea)
        if(inLocation && canMove){
            this.setPosition(Vector2(sprite.x,sprite.y) + movementIncrement)
            return true
        }else{
            cannotMoveStrategy.CannotMoveAction(this)
            return false
        }
    }

    fun DefaultMoveableObjectData(gameObjectNum: Int, type: GameObjectType): ServerGameObjectData {
        return ServerGameObjectData(gameObjectNum = gameObjectNum, size = Pair(size.x, size.y), unitVectorDirection = Pair(currentUnitVector.x, currentUnitVector.y), speed = this.currentSpeed, position = Pair(sprite.x, sprite.y), gameObjectType = type)
    }
}