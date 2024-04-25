package com.example.game

import com.example.game.Actions.Action
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object JsonConfig {
    val json: Json = Json {
        serializersModule = SerializersModule {
            polymorphic(Action::class) {
                subclass(Action.TouchAction::class, Action.TouchAction.serializer())
            }
        }
        classDiscriminator = "type"
    }
}