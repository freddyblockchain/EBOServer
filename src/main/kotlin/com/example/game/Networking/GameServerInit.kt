package com.example.game.Networking

import Player
import com.badlogic.gdx.math.Vector2
import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.playerTimeMap
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
        }

        fun InitPlayer(address: String, port: Int): Int{
            if(SessionManager.playerMap[address] != null){
                //Handle same player logging in before session expires
                val player = SessionManager.playerMap[address]!!
                player.setPosition(Vector2(playerInitPosition.x, playerInitPosition.y))
                playerTimeMap[address] = System.currentTimeMillis() + MAX_CLIENT_CONNECTION_TIME
                return player.playerNum
            }
            val gameObjectNum = GameObjectNumManager.getNextGameNum()
            val player = Player(GameObjectData(x = playerInitPosition.x.toInt(), y = playerInitPosition.y.toInt()), Vector2(32f,32f), gameObjectNum, AreaManager.getStartingArea())
            player.address = address
            SessionManager.connectionMap[address] = ConnectionSettings(port)
            SessionManager.playerMap[address] = player
            return gameObjectNum
        }
    }
}