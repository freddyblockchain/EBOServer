package com.example.game.GameObjects.GameObject

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.Networking.Models.FIGHTER_STATE
import com.mygdx.game.Area.Area
import com.mygdx.game.GameObjects.GameObject.MoveableObject

abstract class FightableObject(gameObjectData: GameObjectData, size: Vector2, currentArea: Area): MoveableObject(gameObjectData, size, currentArea), FightableEntity {
    var launchUnitVector = Vector2(0f,0f)
    var fighterState = FIGHTER_STATE.FREE
    var stunDuration = 3
    val speedDecrease = 0.90f

    override fun frameTask() {
        if(this.fighterState == FIGHTER_STATE.STUNNED){
            this.handleStunned(currentUnitVector)
        }
        if(this.health <= 0){
            this.death()
        }
        super.frameTask()
    }
    open fun isHit(launchUnitVector: Vector2){
        this.health -= 10f
        fighterState = FIGHTER_STATE.STUNNED
        currentSpeed = stunDuration * normalSpeed
        this.launchUnitVector = launchUnitVector
        this.currentUnitVector = launchUnitVector
    }
    fun handleStunned(directionUnitVector: Vector2){
        currentSpeed *= speedDecrease
        if(currentSpeed <= normalSpeed){
            currentSpeed = normalSpeed
            this.fighterState = FIGHTER_STATE.FREE
        }
        super.move(directionUnitVector)
    }

    open fun death(){

    }
}