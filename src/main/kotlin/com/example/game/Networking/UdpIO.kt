import com.example.Sessions.SessionManager.Companion.connectionMap
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.game.Actions.Action
import com.example.game.JsonConfig
import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.globalOutgoingSocket
import com.example.game.Networking.serverPort
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets

fun udpReceive() {
    val buffer = ByteArray(1024)  // Buffer for incoming data
    val globalIngoingSocket = DatagramSocket(serverPort)  // Listen on port 50000
    globalIngoingSocket.use { udpSocket ->
        println("UDP Server is running on port 50000...")
        while (true) {
            val packet = DatagramPacket(buffer, buffer.size)
            udpSocket.receive(packet)  // Receive a packet (blocking call)
            val receivedText = String(packet.data, 0, packet.length).trim()
            val action = JsonConfig.json.decodeFromString<Action>(receivedText)
            println("Received from UDP client: $receivedText")
            println(action.toString())
            // Here you could add logic to process the data
        }
    }
}

fun broadcastGameState(gameState: GameState) {
    val message = Json.encodeToString(gameState).toByteArray(StandardCharsets.UTF_8)
    //Avoid concurrent modification shinanigans
    playerMap.forEach { entry ->
        val connectionSettings = connectionMap[entry.key] ?: ConnectionSettings("",0)
        sendUdpMessage(connectionSettings, message)
    }
}
fun sendUdpMessage(connectionSettings: ConnectionSettings, message: ByteArray) {
    globalOutgoingSocket.use { socket ->
        // Create an InetAddress object from the IP address string
        val address = InetAddress.getByName(connectionSettings.ipAddress)

        // Prepare the packet to send
        val packet = DatagramPacket(message, message.size, address, connectionSettings.port)

        // Send the packet
        socket.send(packet)
        println("Message sent to ${connectionSettings.ipAddress} on port ${connectionSettings.port}")
    }
}