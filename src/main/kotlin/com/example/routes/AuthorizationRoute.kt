package com.example.routes

import VerificationManager
import com.example.game.Algorand.AlgorandManager
import com.example.game.Networking.GameServerInit
import com.example.game.globalGameState
import com.example.models.AuthenticationData
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlayerInitData(val address: String, val playerNum: Int, val serverUUID: String)

fun Route.authenticate() {
    route("/authenticate") {
        post {
            try {
                val authenticationData = call.receive<AuthenticationData>()
                if (authorizationDataIsValid(authenticationData)) {
                    val algorandAddress = authenticationData.verificationData.address
                    AlgorandManager.handleNewPlayer(algorandAddress)
                    val playerNum = GameServerInit.InitPlayer(algorandAddress, authenticationData.localPort)
                    call.respond(HttpStatusCode.OK, PlayerInitData(algorandAddress, playerNum, globalGameState.serverUUID))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Authentification failed")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

fun authorizationDataIsValid(authorizationData: AuthenticationData): Boolean {
    val currentTime = System.currentTimeMillis()
    val verificationData = authorizationData.verificationData
    val playerTime = verificationData.message.toLong()
    //Check that the time to verify is not too old, and not in the future.
    val timeWithinBounds = (currentTime - playerTime) < 3000 && (currentTime - playerTime) > -3000

    val isMessageVerified = VerificationManager.verifyMessage(verificationData)
    return isMessageVerified && timeWithinBounds
}
