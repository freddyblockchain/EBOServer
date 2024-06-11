package com.example.plugins

import algorand
import com.example.routes.authenticate
import com.example.routes.gameState
import customerRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        customerRouting()
        algorand()
        authenticate()
        gameState()
    }
}
