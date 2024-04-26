package com.example.game

import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Actions.Action
import getUnitVectorTowardsPoint

class PlayerInputHandler {
    companion object {
        fun processActions(){
            val availableActions = SessionManager.actionList.toMutableList()
            for(entry in availableActions){
                processAction(entry.first, entry.second)
            }
            if(SessionManager.actionsProcessedSoFar == 0){
                SessionManager.actionsProcessedSoFar = availableActions.size
            }

        }
        private fun processAction(sessionKey: String, action: Action){
            if(action is Action.TouchAction){
                val player = playerMap[sessionKey]
                val toGo = getUnitVectorTowardsPoint(player!!.currentPosition(), Vector2(action.pos.first,action.pos.second))
                player.move(toGo)
            }
        }
    }
}