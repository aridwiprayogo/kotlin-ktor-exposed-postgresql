package com.aridwiprayogo

import com.aridwiprayogo.controller.UserController
import com.aridwiprayogo.models.User
import com.aridwiprayogo.models.UserDTO
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import org.jetbrains.exposed.sql.Database
import java.util.*
import javax.sql.DataSource

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initDB()
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val userController = UserController()

    routing {

        get(path = "/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get(path = "/users"){
            call.respond(HttpStatusCode.OK,userController.getAll())
        }

        post(path = "/users"){
            val user = call.receive<UserDTO>()
            userController.save(user)
            call.respond(HttpStatusCode.Created)
        }

        put("/users/{id}"){
            val id = UUID.fromString(call.parameters["id"])
            val userDTO = call.receive<UserDTO>()
            userController.update(userDTO,id)
            call.respond(HttpStatusCode.OK)
        }

        delete("/users/{id}"){
            val id = UUID.fromString(call.parameters["id"])
            userController.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

fun initDB(){
    val config = HikariConfig("resources/hikari.properties")
    config.apply{
        schema = "news"
        val ds: DataSource = HikariDataSource(config)
        Database.connect(ds)
    }
}

