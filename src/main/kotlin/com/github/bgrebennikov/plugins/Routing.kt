package com.github.bgrebennikov.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.bgrebennikov.RabbitService
import com.github.bgrebennikov.RabbitService.Companion.EXCHANGE_NAME
import com.github.bgrebennikov.RabbitService.Companion.ROUTING_KEY
import com.github.bgrebennikov.data.models.CreateModel
import com.rabbitmq.client.AMQP
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/create") {

            val createData = call.receive<CreateModel>()
            val connection = RabbitService().getInstance().newConnection()
            val channel = connection.createChannel()

            channel.basicPublish(
                EXCHANGE_NAME,
                ROUTING_KEY,
                AMQP.BasicProperties.Builder()
                    .headers(
                        mapOf(
                            "x-delay" to 5000
                        )
                    ).build(),
                jacksonObjectMapper().writeValueAsBytes(createData)
            )

            call.respondText { "OK" }

            channel.close()
            connection.close()

//
//            val connection = RabbitService().getInstance().newConnection()
//            val channel = connection.createChannel()
//
//            channel.basicPublish(
//                EXCHANGE_NAME,
//                ROUTING_KEY,
//                null,
//                createData.toString().toByteArray()
//            )


        }

    }

}
