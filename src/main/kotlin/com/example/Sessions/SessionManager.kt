package com.example.Sessions

import Player
import com.example.game.Actions.PlayerAction
import com.example.game.Networking.Models.ConnectionSettings
import com.mygdx.game.Managers.AreaManager

class SessionManager {
    companion object  {
        val playerTimeMap = mutableMapOf<String,Long>()
        val playerMap = mutableMapOf<String, Player>()
        val connectionMap = mutableMapOf<String, ConnectionSettings>()
        var actionList = mutableListOf<Pair<String,PlayerAction>>()
        var actionsProcessedSoFar = 0

        fun removePlayer(address: String){
            val player = playerMap[address]
            if(player != null){
                AreaManager.getActiveArea()!!.gameObjects.remove(player)
            }
            playerTimeMap.remove(address)
            playerMap.remove(address)
            connectionMap.remove(address)
        }
    }
}