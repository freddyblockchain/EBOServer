package com.example.game.Networking.Models

import PLAYER_STATUS
import kotlinx.serialization.Serializable

@Serializable
data class PlayerServerData(val position: Pair<Float,Float>, val status: PLAYER_STATUS, val playerNum: Int, val speed: Float, val unitVectorDirection: Pair<Float,Float>)