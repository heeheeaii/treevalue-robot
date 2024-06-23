package com.treevalue.tvrnet.server

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


class WebSocketHandler : TextWebSocketHandler() {
    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Connected: " + session.id)
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("Received: " + message.payload)
        session.sendMessage(TextMessage("Hello, " + message.payload + "!"))
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("Disconnected: " + session.id)
    }
}

