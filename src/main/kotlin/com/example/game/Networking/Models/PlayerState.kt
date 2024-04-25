package com.example.game.Networking.Models

import PLAYER_STATUS
import Player
import kotlinx.serialization.Serializable

@Serializable
data class PlayerState(val position: Pair<Float,Float>, val status: PLAYER_STATUS)