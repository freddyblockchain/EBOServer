package com.example.game

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Abilities.AbilityManager
import com.example.game.Abilities.DashAbility
import com.example.game.Actions.PlayerAction
import com.example.game.Algorand.AlgorandManager
import com.example.game.Networking.Models.FIGHTER_STATE
import com.mygdx.game.Abilities.ABILITY_TYPE
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
                if(player != null && player.fighterState == FIGHTER_STATE.FREE){
                    val playerPos = player.currentPosition()
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    val toGo = getUnitVectorTowardsPoint(playerPos,targetPos)
                    val distance = ceil((distance(playerPos, targetPos) / player.currentSpeed))

                    player.movementFrames = distance
                    player.currentUnitVector = toGo
                }
            }
            if(action is PlayerAction.DashAbility){
                if(player != null && player.abilities.any { it.abilityType == ABILITY_TYPE.DASH }){
                    val dashAbility = player.abilities.first { it.abilityType == ABILITY_TYPE.DASH }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    dashAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.FireAbility){
                if(player != null && player.abilities.any { it.abilityType == ABILITY_TYPE.FIREBALL }){
                    val fireballAbility = player.abilities.first { it.abilityType == ABILITY_TYPE.FIREBALL }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    fireballAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.IcicleAbility){
                if(player != null && player.abilities.any { it.abilityType == ABILITY_TYPE.ICICLE}){
                    val icicleAbility = player.abilities.first { it.abilityType == ABILITY_TYPE.ICICLE }
                    val targetPos = Vector2(action.pos.first, action.pos.second)
                    icicleAbility.tryActivate(targetPos, player)
                }
            }
            if(action is PlayerAction.SnowballAbility){
                if(player != null && player.abilities.any { it.abilityType == ABILITY_TYPE.SNOWBALL }){
                    val snowballAbility = player.abilities.first { it.abilityType == ABILITY_TYPE.SNOWBALL }
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