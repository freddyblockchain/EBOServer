package com.example.Sessions

import Player
import com.example.game.Actions.TouchAction
import com.example.game.Networking.Models.ConnectionSettings
import com.mygdx.game.Managers.AreaManager

class SessionManager {
    companion object  {
        val playerTimeMap = mutableMapOf<String,Long>()
        val playerMap = mutableMapOf<String, Player>()
        val connectionMap = mutableMapOf<String, ConnectionSettings>()
        var actionList = mutableListOf<Pair<String,TouchAction>>()
        var actionsProcessedSoFar = 0

        fun removePlayer(sessionKey: String){
            val player = playerMap[sessionKey]
            if(player != null){
                AreaManager.getActiveArea()!!.gameObjects.remove(player)
            }
            playerTimeMap.remove(sessionKey)
            playerMap.remove(sessionKey)
            connectionMap.remove(sessionKey)
        }
    }
}