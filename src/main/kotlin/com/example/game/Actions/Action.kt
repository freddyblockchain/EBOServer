package com.example.game.Actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerAction {
    @Serializable
    @SerialName("Move")
    data class Move(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("FireAbility")
    data class FireAbility(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("IcicleAbility")
    data class IcicleAbility(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("SnowballAbility")
    data class SnowballAbility(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("UpdatePlayerState")
    class UpdatePlayerState: PlayerAction
}