package com.github.bgrebennikov

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.github.bgrebennikov.plugins.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        RabbitService().listenEvents()

        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
