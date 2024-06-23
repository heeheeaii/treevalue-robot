package com.treevalue.test.websocketserver

import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


class EchoWebSocketHandler : TextWebSocketHandler() {
    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        session.sendMessage(message)
    }
}

