package com.example.Sessions

import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.Networking.Models.PlayerState

class SessionManager {
    companion object  {
        val playerMap = mutableMapOf<String, PlayerState>()
        val connectionMap = mutableMapOf<String, ConnectionSettings>()
        val playerNumMap = mutableMapOf<String, Int>()
    }
}