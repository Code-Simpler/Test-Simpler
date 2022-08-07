package com.codesimpler.testsimpler

import com.codesimpler.testsimpler.Simple.ok
import com.codesimpler.testsimpler.Simple.uri
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Test

internal class AsyncTest {

    val client = OkHttpClient()

    @Test
    fun `stubs in threads use unique SimpleMockServers with their own responses`() {
        val threadCount = 3

        val threadNames = mutableListOf<String>()
        runBlocking {
            repeat(threadCount) {
                launch {
                    val threadName = Thread.currentThread().name
                    threadNames.add(threadName)
                    ok()

                    val request = Request.Builder()
                        .get()
                        .url(uri())
                        .build()
                    client.newCall(request).execute()
                }
            }
        }

        assertSoftly {
            threadNames.forEach {
                SimpleMockServerRepository.instance.get(it).mockWebServer.requestCount shouldBe 1
            }
        }

        threadNames.forEach { SimpleMockServerRepository.instance.get(it).close() }
    }
}
