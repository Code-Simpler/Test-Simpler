package com.codesimpler.testsimpler

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldNotBeEqualIgnoringCase
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class SimpleMockServerTest {

    @SpyK
    var mockWebServer: MockWebServer = MockWebServer()

    @InjectMockKs
    lateinit var simpleMockServer: SimpleMockServer

    @AfterEach
    fun close() {
        mockWebServer.close()
    }

    @Test
    fun `addResponse adds a response to the underlying web server`() {
        simpleMockServer.addResponse()

        verify { mockWebServer.enqueue(any()) }
    }

    @Nested
    inner class Uri {

        @Test
        fun `returns the MockWebServer's uri`() {
            simpleMockServer.uri()

            verify { mockWebServer.url(any()) }
        }

        @Test
        fun `returns the MockWebServer's uri with the provided path appended`() {
            val path = "test"

            assertSoftly(simpleMockServer.uri(path)) {
                it shouldEndWith path
                it shouldNotBeEqualIgnoringCase path
            }
        }
    }

    @Test
    fun `port returns the appropriate value`() {
        simpleMockServer.port() shouldBe mockWebServer.port
    }
}
