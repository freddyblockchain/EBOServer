package com.example.routes

import com.example.game.Algorand.AlgorandManager
import com.example.game.Networking.GameServerInit
import com.example.models.AuthorizationData
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class PlayerInitData(val address: String, val playerNum: Int)

fun Route.authorize() {
    route("/authorize") {
        post {
            val authorizationData = call.receive<AuthorizationData>()
            if (authorizationDataIsValid(authorizationData)) {
                AlgorandManager.handleNewPlayer(authorizationData.algorandAddress)
                val playerNum = GameServerInit.InitPlayer(authorizationData.algorandAddress, authorizationData.localPort)
                call.respond(HttpStatusCode.OK, PlayerInitData(authorizationData.algorandAddress, playerNum))
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
