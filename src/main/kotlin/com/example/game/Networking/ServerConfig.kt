package com.example.game.Networking

import java.net.DatagramSocket

val basePath = System.getenv("ASSETS_PATH") ?: throw IllegalStateException("Assets path not configured")
val serverPort = 50000
val serverOutgoingSocket = DatagramSocket(0)