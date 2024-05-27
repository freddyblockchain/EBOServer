package com.example.game.Networking

import java.net.DatagramSocket

val basePath = System.getenv("ASSETS_PATH") ?: throw IllegalStateException("Assets path not configured")
val serverPort = 50000
val serverOutgoingSocket = DatagramSocket(0)
//if player is not active for 2 minutes, boot them out.
val MAX_CLIENT_CONNECTION_TIME = 120 * 1000