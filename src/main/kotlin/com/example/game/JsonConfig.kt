package com.example.game

import com.example.game.Actions.TouchAction
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
        }
        classDiscriminator = "type"
    }
}