package com.example.routes

import com.example.game.GameServerMain
import com.example.game.JsonConfig
import com.example.game.Networking.Models.SseEvent
import com.example.game.delayTime
import com.example.game.globalGameState
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val tickTime = 1000L / 20// Frame time in milliseconds for 60 FPS
fun Route.gameState() {
    route("/gameState") {
        get {
            call.respondTextWriter(ContentType.Text.EventStream) {
                var id = 0
                while (true) {
                    id++
                    val sseEvent = SseEvent(id = id.toString(), event = "gameStateUpdate", data = globalGameState)
                    val sseEventJson = JsonConfig.json.encodeToString(sseEvent)

                    write("data: $sseEventJson\n\n")
                    flush()
                    delay(delayTime)
                }
            }
        }
    }
}