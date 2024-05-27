package com.example.routes

import com.example.game.GameServerMain
import com.example.game.JsonConfig
import com.example.game.Networking.Models.SseEvent
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
                    val startTime = System.currentTimeMillis()
                    val gameState = GameServerMain.gameTick()
                    id++
                    val sseEvent = SseEvent(id = id.toString(), event = "gameStateUpdate", data = gameState)
                    val sseEventJson = JsonConfig.json.encodeToString(sseEvent)

                    write("data: $sseEventJson\n\n")
                    flush()
                    val endTime = System.currentTimeMillis()
                    val deltaTime = endTime - startTime
                    if (deltaTime < tickTime) {
                        println("delaying: " + (tickTime - deltaTime))
                        delay(tickTime - deltaTime)
                    }
                }
            }
        }
    }
}