package com.codesimpler.testsimpler

import io.kotest.matchers.nulls.shouldNotBeNull
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSimplerExtension::class)
internal class JUnit5Test(val mockWebServer: MockWebServer, val simpleMockServer: SimpleMockServer) {

    @Nested
    inner class TestExtension {

        @Test
        fun `provides test method parameter MockWebServer`(mockWebServer: MockWebServer) {
            mockWebServer.shouldNotBeNull()
        }

        @Test
        fun `provides test method parameter SimpleMockServer`(simpleMockServer: SimpleMockServer) {
            simpleMockServer.shouldNotBeNull()
        }

        @Test
        fun `provides test constructor MockWebServer`() {
            mockWebServer.shouldNotBeNull()
        }

        @Test
        fun `provides test constructor SimpleMockServer`() {
            simpleMockServer.shouldNotBeNull()
        }
    }
}
