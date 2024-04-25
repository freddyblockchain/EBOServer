package com.example.routes

import com.example.game.Networking.GameServerInit
import com.example.models.AuthorizationData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.authorize(){
    route("/authorize") {
        post {
            val authorizationData = call.receive<AuthorizationData>()
            if(authorizationDataIsValid(authorizationData)){
                val newSessionKey = UUID.randomUUID().toString()
                val ipAddress = call.request.origin.remoteHost
                GameServerInit.InitPlayer(newSessionKey, ipAddress, authorizationData.localPort)
                call.respond(HttpStatusCode.OK, newSessionKey)
            }
            else {
                call.respond(HttpStatusCode.Unauthorized, "Authorization failed")
            }
        }
    }
}
fun authorizationDataIsValid(authorizationData: AuthorizationData): Boolean{
    authorizationData.roundSeed
    return true
}
