package com.example.game.Networking.Models

import kotlinx.serialization.Serializable

@Serializable
data class GameState(val playerStates: Map<String, PlayerServerData>)