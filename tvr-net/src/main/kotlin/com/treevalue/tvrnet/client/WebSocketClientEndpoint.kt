package com.treevalue.tvrnet.client

import jakarta.websocket.*
import java.net.URI


@ClientEndpoint
class WebSocketClientEndpoint(private val url: String) {
    private var session: Session? = null

    init {
        connectToServer()
    }

    private fun connectToServer() {
        try {
            val container = ContainerProvider.getWebSocketContainer()
            val uri = URI.create(url)
            val endpoint = this
            container.connectToServer(endpoint, uri)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @OnOpen
    fun onOpen(session: Session?) {
        println("Connected to server.")
        this.session = session
    }

    @OnMessage
    fun onMessage(message: String) {
        println("Message received: $message")
    }

    @OnClose
    fun onClose(closeReason: CloseReason) {
        println("Connection closed: " + closeReason.reasonPhrase)
    }

    @Throws(java.lang.Exception::class)
    fun sendMessage(message: String?) {
        if (session != null && session!!.isOpen) {
            session!!.basicRemote.sendText(message)
        } else {
            throw java.lang.Exception("Session is not open.")
        }
    }

    @Throws(java.lang.Exception::class)
    fun closeConnection() {
        if (session != null && session!!.isOpen) {
            session!!.close()
        } else {
            throw java.lang.Exception("Session is already closed.")
        }
    }
}
