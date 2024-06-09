package com.example.game

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Abilities.AbilityManager
import com.example.game.Actions.PlayerAction
import com.example.game.Algorand.AlgorandManager
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
        private fun processAction(address: String, action: PlayerAction){
            val player = playerMap[address]
            if(action is PlayerAction.Move){
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
                if(player != null && player.abilities.any { it == AbilityManager.fireballAbility}){
                    val fireballAbility = player.abilities.first { it == AbilityManager.fireballAbility }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    fireballAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.IcicleAbility){
                if(player != null && player.abilities.any { it == AbilityManager.icicleAbility}){
                    val icicleAbility = player.abilities.first { it == AbilityManager.icicleAbility }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    icicleAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.SnowballAbility){
                if(player != null && player.abilities.any { it == AbilityManager.snowballAbility}){
                    val snowballAbility = player.abilities.first { it == AbilityManager.snowballAbility }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    snowballAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.UpdatePlayerState){
                if(player != null){
                    AlgorandManager.updatePlayerAbilities(player, address)
                }
            }
        }
    }
}