package com.example.game.Actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Action {
    val sessionKey: String
    @Serializable
    @SerialName("TouchAction")
    data class TouchAction(val pos: Pair<Float,Float>, override val sessionKey: String): Action
}