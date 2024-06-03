package com.example.game

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Abilities.FireballAbility
import com.example.game.Actions.PlayerAction
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
        private fun processAction(sessionKey: String, action: PlayerAction){
            if(action is PlayerAction.Move){
                val player = playerMap[sessionKey]
                if(player != null){
                    val playerPos = player.currentPosition()
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    val toGo = getUnitVectorTowardsPoint(playerPos,targetPos)
                    val distance = ceil((distance(playerPos, targetPos) / player.currentSpeed))

                    player.movementFrames = distance
                    player.currentUnitVector = toGo
                }

            }
            if(action is PlayerAction.FireAbility){
                val player = playerMap[sessionKey]
                if(player != null && player.abilities.any { it is FireballAbility }){
                    val fireballAbility = player.abilities.first { it is FireballAbility }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    fireballAbility.tryActivate(targetPos)
                }
            }
        }
    }
}