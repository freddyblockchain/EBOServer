package com.example.routes

import com.example.game.*
import com.example.game.Networking.Models.CustomFields
import com.example.game.Networking.Models.GameObjectType
import com.example.game.Networking.Models.SseEvent
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.SocketException
fun Route.gameState() {
    route("/gameState") {
        get {
            call.respondTextWriter(ContentType.Text.EventStream) {
                var id = 0
                try {
                    var currentGameState = globalGameState.copy()
                    while (true) {
                        if(currentGameState != globalGameState){
                            currentGameState = globalGameState.copy()
                            id++
                            val sseEvent = SseEvent(id = id.toString(), event = "gameStateUpdate", data = currentGameState)
                            val sseEventJson = JsonConfig.json.encodeToString(sseEvent)

                            write("data: $sseEventJson\n\n")
                            flush()
                        } else {
                            delay(5)
                        }
                    }
                } catch (e: Exception) {
                    println("client probably disconnected")
                    e.printStackTrace()
                }
            }
        }
    }
}