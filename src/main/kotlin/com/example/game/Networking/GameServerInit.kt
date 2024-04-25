package com.example.game.Networking

import Player
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.game.GameObjectData
import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.initMappings
import com.example.game.Networking.Models.PlayerState
import com.mygdx.game.Managers.AreaManager
import initAreas
import initObjects

class GameServerInit {
    val udpPort = 10
    companion object {
        var playerNum = 0
        fun Init() {
            initMappings()
            initAreas()
            initObjects()
            AreaManager.setActiveArea("World1")
        }

        fun InitPlayer(newSessionKey: String, ipAddress: String, port: Int){
            SessionManager.connectionMap[newSessionKey] = ConnectionSettings(ipAddress, port)
            SessionManager.playerMap[newSessionKey] = PlayerState(position = Pair(0f,120f), PLAYER_STATUS.ALIVE)
            AreaManager.getActiveArea()!!.gameObjects.add(Player(GameObjectData(x = 0, y = 120), Vector2(32f,32f), newSessionKey))

            playerNum += 1
            SessionManager.playerNumMap[newSessionKey] = playerNum
        }
    }
}