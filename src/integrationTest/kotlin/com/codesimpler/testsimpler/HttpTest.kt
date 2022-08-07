package com.codesimpler.testsimpler

import com.codesimpler.testsimpler.Simple.notFound
import com.codesimpler.testsimpler.Simple.ok
import com.codesimpler.testsimpler.Simple.response
import com.codesimpler.testsimpler.Simple.uri
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(TestSimplerExtension::class)
class HttpTest {

    val client = OkHttpClient()

    companion object {
        @JvmStatic
        fun mockAndCodeAndBody(): List<Arguments> =
            listOf(
                Arguments.of({ ok() }, HttpStatus.OK, ""),
                Arguments.of({ notFound() }, HttpStatus.NOT_FOUND, ""),
                Arguments.of({ response(HttpStatus.BAD_REQUEST) }, HttpStatus.BAD_REQUEST, "")
            )
    }

    @ParameterizedTest
    @MethodSource("mockAndCodeAndBody")
    fun `mock queues correct response`(mockCall: () -> Unit, httpStatus: HttpStatus, body: String) {
        val request = Request.Builder()
            .get()
            .url(uri())
            .build()

        mockCall()
        val response = client.newCall(request).execute()

        assertSoftly {
            response.code shouldBe httpStatus.value
            response.body?.string() shouldBe body
        }
    }

    @Test
    fun `fake failure()`() {
        fail("For testing")
    }
}
