package com.treevalue.test.websoketclient

import jakarta.websocket.ContainerProvider
import jakarta.websocket.Session
import java.io.Closeable
import java.net.URI

class WebsocketClient : Closeable {
    private val container = ContainerProvider.getWebSocketContainer()
    private lateinit var session: Session
    private lateinit var path: URI
    private lateinit var annotatedEndpointClass: Class<*>

    constructor(annotatedEndpointClass: Class<*>, path: URI) {
        session = container.connectToServer(annotatedEndpointClass, path)
        this.annotatedEndpointClass = annotatedEndpointClass
        this.path = path
    }

    fun sendMessage(msg: String) {
        session.basicRemote.sendText(msg)
    }

    fun updateEndPoint(annotatedEndpointClass: Class<*>) {
        session.close()
        session = container.connectToServer(annotatedEndpointClass, path)
    }

    override fun close() {
        session.close()
    }
}
