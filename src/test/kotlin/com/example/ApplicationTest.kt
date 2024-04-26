@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example

import Customer
import com.example.game.Networking.GameServerInit
import com.example.models.AuthorizationData
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import com.mygdx.game.Area.Area
import com.mygdx.game.Managers.AreaManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ApplicationTest {
    @OptIn(InternalAPI::class)
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureSecurity()
            AreaManager.areas.add(Area("testArea"))
            AreaManager.setActiveArea("testArea")
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
       client.post("/customer") {
            contentType(ContentType.Application.Json)
            body = "{\"id\":\"3\",\"firstName\":\"Jet\",\"lastName\":\"Brains\",\"email\":\"mail\"}"
        }
        client.get("/customer/3").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("{\"id\":\"3\",\"firstName\":\"Jet\",\"lastName\":\"Brains\",\"email\":\"mail\"}", bodyAsText())
        }
        client.post("/authorize") {
            contentType(ContentType.Application.Json)
            body = Json.encodeToString(AuthorizationData("signedMessage", 1000, "dsffdwqdqdada", "2123421", 100))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            println("sessionKey " + bodyAsText())
        }

    }
}