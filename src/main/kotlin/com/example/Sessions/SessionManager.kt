package com.example.Sessions

import Player
import com.example.game.Actions.Action
import com.example.game.Networking.Models.ConnectionSettings

class SessionManager {
    companion object  {
        val playerMap = mutableMapOf<String, Player>()
        val connectionMap = mutableMapOf<String, ConnectionSettings>()
        var actionList = mutableListOf<Pair<String,Action>>()
        var actionsProcessedSoFar = 0
    }
}