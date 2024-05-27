package com.example.game.Networking

import Player
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.game.GameObjectData
import com.example.game.Managers.GameObjectNumManager
import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.initMappings
import com.mygdx.game.Managers.AreaManager
import initAreas
import initObjects

class GameServerInit {
    companion object {
        val playerInitPosition = Vector2(100f,-100f)
        fun Init() {
            initMappings()
            initAreas()
            initObjects()
            AreaManager.setActiveArea("World1")
        }

        fun InitPlayer(newSessionKey: String, ipAddress: String, port: Int): Int{
            println("IpAdress is: " + ipAddress)
            val gameObjectNum = GameObjectNumManager.getNextGameNum()
            val player = Player(GameObjectData(x = playerInitPosition.x.toInt(), y = playerInitPosition.y.toInt()), Vector2(32f,32f), gameObjectNum)
            SessionManager.connectionMap[newSessionKey] = ConnectionSettings(ipAddress, port)
            SessionManager.playerMap[newSessionKey] = player
            AreaManager.getActiveArea()!!.gameObjects.add(player)
            return gameObjectNum
        }
    }
}