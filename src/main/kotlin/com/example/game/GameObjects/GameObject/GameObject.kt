package com.mygdx.game.GameObjects.GameObject

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Area.Area
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Collition.Collision
import com.mygdx.game.Collition.CollisionMask
import com.mygdx.game.Collition.DefaultCollisionMask
import com.mygdx.game.InitPolygon
import com.mygdx.game.InitSprite
import com.mygdx.game.Managers.AreaManager

abstract class GameObject(gameObjectData: GameObjectData, val size: Vector2, var currentArea: Area){
    val initPosition = Vector2(gameObjectData.x.toFloat(), gameObjectData.y.toFloat())
    val topleft = Vector2(initPosition.x,initPosition.y + size.y)
    val topright = Vector2(initPosition.x + size.x,initPosition.y + size.y)
    val bottomright =  Vector2(initPosition.x + size.x,initPosition.y)
    val bottomleft = initPosition
    val x = initPosition.x
    val y = initPosition.y
    val width = size.x
    val height = size.y

    val originalMiddle: Vector2 = Vector2(topleft.x + (topright.x - topleft.x) / 2,bottomleft.y + (topleft.y - bottomleft.y)/2)
    val currentMiddle: Vector2
    get() = Vector2(sprite.x + sprite.width/2, sprite.y + sprite.height/2)
    var startingPosition = initPosition

    open val sprite: Sprite by lazy { InitSprite()}
    open val polygon: Polygon by lazy {InitPolygon(sprite)}
    open val shouldCollide = true
    open val collision: Collision = CanMoveCollision()
    val gameObjectIid = gameObjectData.iid
    var collidingObjects: List<GameObject> = listOf()

    init {
        currentArea.gameObjects.add(this)
    }

    open fun frameTask(){
    }
    open fun initObject(){

    }
    val onLocationEnterActions: MutableList<()-> Unit> = mutableListOf()
    val onRemoveAction: MutableList<()->Unit> = mutableListOf({})
    open val collitionMask: CollisionMask = DefaultCollisionMask()

    open fun setPosition(position: Vector2) {
        sprite.setPosition(position.x,position.y)
        polygon.setPosition(position.x - polygon.vertices[0],position.y - polygon.vertices[1])
    }

    fun currentPosition(): Vector2 {
        return Vector2(sprite.x,sprite.y)
    }

    fun remove(){
        this.currentArea.gameObjects.remove(this)
    }
    fun add(){
        this.currentArea.gameObjects.add(this)
    }
}