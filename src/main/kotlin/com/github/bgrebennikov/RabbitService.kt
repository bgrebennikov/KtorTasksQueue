package com.github.bgrebennikov

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.bgrebennikov.data.models.CreateModel
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import io.ktor.utils.io.charsets.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class RabbitService {

    private val connectionFactory : ConnectionFactory = ConnectionFactory()

    init{
        connectionFactory.apply {
            newConnection(
                "amqp://admin:25050032@0.0.0.0:15672/"
            ).use { connection ->
                connection.createChannel().use { channel ->

                    val args = hashMapOf<String, Any>()
                    args["x-delayed-type"] = "direct"

                    channel.exchangeDeclare(EXCHANGE_NAME, "x-delayed-message", true, false, args);
                    channel.queueDeclare(QUEUE_NAME, true, false, true, emptyMap())
                    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY)

//                    channel.close()
//                    connection.close()
                }
            }
        }
    }

    fun listenEvents(){
        val connection = connectionFactory.newConnection()
        val channel = connection.createChannel()

        channel.basicConsume(
            QUEUE_NAME,
            true,
            { _, message ->
                val data : CreateModel = jacksonObjectMapper().readValue(String(message.body), CreateModel::class.java)
                println("Consuming message: $data}")
            },
            { consumerTag ->
                println("Cancelled... $consumerTag")
            }
        )
    }

    fun getInstance() : ConnectionFactory{
        return connectionFactory
    }


    companion object{
        const val QUEUE_NAME = "test_queue"
        const val EXCHANGE_NAME = "my_ex"
        const val ROUTING_KEY = "test_rk"

    }

}