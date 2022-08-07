package com.codesimpler.testsimpler

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.Closeable

/**
 * Wrapper for [MockWebServer] to expose only the methods we need. In the future, if we decide
 * we need backends other than MockWebServer can be refactored to an interface.
 */
class SimpleMockServer(val mockWebServer: MockWebServer = MockWebServer()) : Closeable by mockWebServer {

    fun addResponse(responseCode: Int = HttpStatus.OK.value, headers: Map<String, Any>? = null, body: String? = null) {
        MockResponse()
            .setResponseCode(responseCode)
            .also { response -> headers?.forEach { response.addHeader(it.key, it.value) } }
            .also { response -> body?.let { response.setBody(it) } }
            .also { mockWebServer.enqueue(it) }
    }

    fun uri(path: String? = null): String = mockWebServer.url(path ?: "").toString()

    fun port(): Int = mockWebServer.port
}
