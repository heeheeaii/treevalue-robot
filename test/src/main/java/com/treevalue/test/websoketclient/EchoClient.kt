package com.treevalue.test.websoketclient

import jakarta.websocket.ClientEndpoint
import jakarta.websocket.OnMessage
import jakarta.websocket.Session


@ClientEndpoint
class EchoClient {
    @OnMessage
    fun onMessage(message: String, session: Session?) {
        println("Received from server: $message")
    }
}

