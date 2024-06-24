package com.example.routes

import com.example.Sessions.SessionManager
import com.example.game.*
import com.example.game.Networking.Models.CustomFields
import com.example.game.Networking.Models.GameObjectType
import com.example.game.Networking.Models.SseEvent
import com.mygdx.game.Managers.AreaManager
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.SocketException
fun Route.gameState() {
    route("/gameState/{address}") {
        get {
            val address = call.parameters["address"]
            val player = SessionManager.playerMap[address]!!
            call.respondTextWriter(ContentType.Text.EventStream) {
                var id = 0
                try {
                    var currentGameState = player.currentArea.gameState
                    while (true) {
                        if(currentGameState != player.currentArea.gameState){
                            currentGameState = player.currentArea.gameState.copy()
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