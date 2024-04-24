package com.example.routes

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.example.INIT_POS
import com.example.models.AuthorizationData
import com.example.models.PlayerState
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.authorize(){
    val sessions = mutableMapOf<String, PlayerState>()
    route("/authorize") {
        post {
            val authorizationData = call.receive<AuthorizationData>()
            if(authorizationDataIsValid(authorizationData)){
                val newSessionKey = UUID.randomUUID().toString()
                sessions[newSessionKey] = PlayerState(INIT_POS)
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
