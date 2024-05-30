package com.example.routes

import com.example.game.Networking.GameServerInit
import com.example.models.AuthorizationData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlayerInitData(val sessionKey: String, val playerNum: Int)

fun Route.authorize() {
    route("/authorize") {
        post {
            val authorizationData = call.receive<AuthorizationData>()
            if (authorizationDataIsValid(authorizationData)) {
                val newSessionKey = UUID.randomUUID().toString()
                val playerNum = GameServerInit.InitPlayer(newSessionKey, authorizationData.localPort)
                call.respond(HttpStatusCode.OK, PlayerInitData(newSessionKey, playerNum))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Authorization failed")
            }
        }
    }
}

fun authorizationDataIsValid(authorizationData: AuthorizationData): Boolean {
    authorizationData.roundSeed
    return true
}
