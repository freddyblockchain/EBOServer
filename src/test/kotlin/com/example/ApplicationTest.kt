@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import com.mygdx.game.Area.Area
import com.mygdx.game.Managers.AreaManager
import io.ktor.utils.io.*

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
       /*client.post("/customer") {
            contentType(ContentType.Application.Json)
            body = "{\"id\":\"3\",\"firstName\":\"Jet\",\"lastName\":\"Brains\",\"email\":\"mail\"}"
        }*/
        client.get("/customer/3").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("{\"id\":\"3\",\"firstName\":\"Jet\",\"lastName\":\"Brains\",\"email\":\"mail\"}", bodyAsText())
        }
        /*client.post("/authorize") {
            contentType(ContentType.Application.Json)
            body = Json.encodeToString(AuthorizationData("signedMessage", 1000, "dsffdwqdqdada", "2123421", 100))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            println("sessionKey " + bodyAsText())
        }*/

    }
}