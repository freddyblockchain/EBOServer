package com.example.game

import com.example.game.Actions.TouchAction
import com.example.game.Networking.Models.CustomFields
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object JsonConfig {
    val json: Json = Json {
        serializersModule = SerializersModule {
            polymorphic(TouchAction::class) {
                subclass(TouchAction.Move::class, TouchAction.Move.serializer())
                subclass(TouchAction.FireAbility::class, TouchAction.FireAbility.serializer())
            }
            polymorphic(CustomFields::class) {
                subclass(CustomFields.EmptyCustomFields::class, CustomFields.EmptyCustomFields.serializer())
                subclass(CustomFields.PlayerCustomFields::class, CustomFields.PlayerCustomFields.serializer())
            }
        }
        classDiscriminator = "type"
    }
}