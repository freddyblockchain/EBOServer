import com.example.Sessions.SessionManager
import com.example.Sessions.SessionManager.Companion.connectionMap
import com.example.Sessions.SessionManager.Companion.playerMap
import com.example.Sessions.SessionManager.Companion.playerTimeMap
import com.example.Sessions.SessionManager.Companion.removePlayer
import com.example.game.Actions.TouchAction
import com.example.game.JsonConfig
import com.example.game.Networking.MAX_CLIENT_CONNECTION_TIME
import com.example.game.Networking.Models.ConnectionSettings
import com.example.game.Networking.Models.GameState
import com.example.game.Networking.serverOutgoingSocket
import com.example.game.Networking.serverPort
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets

@Serializable
data class UdpPacket(val action: TouchAction, val sessionKey: String)
fun udpReceive() {
    val buffer = ByteArray(1024)  // Buffer for incoming data
    val globalIngoingSocket = DatagramSocket(serverPort)  // Listen on port 50000
    globalIngoingSocket.use { udpSocket ->
        println("new version: UDP Server is running on port 50000...")
        while (true) {
            val packet = DatagramPacket(buffer, buffer.size)
            udpSocket.receive(packet)  // Receive a packet (blocking call)
            val receivedText = String(packet.data, 0, packet.length).trim()
            val packetData = JsonConfig.json.decodeFromString<UdpPacket>(receivedText)

            if(SessionManager.actionsProcessedSoFar > 0){
                SessionManager.actionList =  SessionManager.actionList.drop(SessionManager.actionsProcessedSoFar + 1).toMutableList()
                SessionManager.actionsProcessedSoFar = 0
            }
            SessionManager.actionList.add(Pair(packetData.sessionKey, packetData.action))

            println("Received from UDP client: $receivedText")
            println(packetData.toString())
            playerTimeMap[packetData.sessionKey] = System.currentTimeMillis() + MAX_CLIENT_CONNECTION_TIME
            // Here you could add logic to process the data
        }
    }
}