package com.example

import com.example.game.GameServerInit
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    GameServerInit.Init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
    }.start(wait = true)
}
