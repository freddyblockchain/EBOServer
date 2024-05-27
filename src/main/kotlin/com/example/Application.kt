package com.example

import com.example.game.GameServerMain
import com.example.game.Networking.GameServerInit
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import udpReceive

fun main() {
    GameServerInit.Init()
    val receiveInputScope = CoroutineScope(Dispatchers.IO)
    receiveInputScope.launch {  udpReceive() }
    val gameLoopScope = CoroutineScope(Dispatchers.IO)
    gameLoopScope.launch { GameServerMain().mainLoop() }
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
    }.start(wait = true)
}
