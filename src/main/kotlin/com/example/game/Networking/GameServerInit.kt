package com.example.game.Networking

import Player
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.game.GameObjectData
import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.initMappings
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

        fun InitPlayer(newSessionKey: String, ipAddress: String, port: Int): Int{
            playerNum += 1
            val player = Player(GameObjectData(x = 100, y = -100), Vector2(32f,32f), playerNum)
            SessionManager.connectionMap[newSessionKey] = ConnectionSettings(ipAddress, port)
            SessionManager.playerMap[newSessionKey] = player

            AreaManager.getActiveArea()!!.gameObjects.add(player)

            return playerNum
        }
    }
}