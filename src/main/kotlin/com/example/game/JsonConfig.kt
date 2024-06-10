package com.example.game

import com.example.game.Actions.PlayerAction
import com.example.game.Networking.Models.CustomFields
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object JsonConfig {
    val json: Json = Json {
        serializersModule = SerializersModule {
            polymorphic(PlayerAction::class) {
                subclass(PlayerAction.Move::class, PlayerAction.Move.serializer())
                subclass(PlayerAction.FireAbility::class, PlayerAction.FireAbility.serializer())
                subclass(PlayerAction.IcicleAbility::class, PlayerAction.IcicleAbility.serializer())
                subclass(PlayerAction.SnowballAbility::class, PlayerAction.SnowballAbility.serializer())
                subclass(PlayerAction.DashAbility::class, PlayerAction.DashAbility.serializer())
                subclass(PlayerAction.UpdatePlayerState::class, PlayerAction.UpdatePlayerState.serializer())
            }
            polymorphic(CustomFields::class) {
                subclass(CustomFields.EmptyCustomFields::class, CustomFields.EmptyCustomFields.serializer())
                subclass(CustomFields.PlayerCustomFields::class, CustomFields.PlayerCustomFields.serializer())
            }
        }
        classDiscriminator = "type"
    }
}