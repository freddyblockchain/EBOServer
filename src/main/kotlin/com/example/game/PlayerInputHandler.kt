package com.example.game

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Actions.TouchAction
import com.example.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.shootProjectile
import distance
import getUnitVectorTowardsPoint

class PlayerInputHandler {
    companion object {
        fun processActions(){
            val availableActions = SessionManager.actionList.toMutableList()
            if(SessionManager.actionsProcessedSoFar == 0){
                for(entry in availableActions){
                    processAction(entry.first, entry.second)
                }
                SessionManager.actionsProcessedSoFar = availableActions.size
            }

        }
        private fun processAction(sessionKey: String, action: TouchAction){
            if(action is TouchAction.Move){
                println("im here!")
                val player = playerMap[sessionKey]
                if(player != null){
                    val playerPos = player.currentPosition()
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    val toGo = getUnitVectorTowardsPoint(playerPos,targetPos)
                    val distance = ceil((distance(playerPos, targetPos) / player.speed))

                    println("trying to go to " + targetPos)
                    player.movementFrames = distance
                    player.currentUnitVector = toGo
                }

            }
            if(action is TouchAction.FireAbility){
                val player = playerMap[sessionKey]
                if(player != null){
                    val playerPos = player.currentPosition()
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    val direction = getUnitVectorTowardsPoint(playerPos,targetPos)
                    player.shootProjectile(Fireball(GameObjectData(x = playerPos.x.toInt(), y= playerPos.y.toInt()),  Vector2(60f,30f), direction))
                }
            }
        }
    }
}