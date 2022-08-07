package com.codesimpler.testsimpler

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Parameter

internal class TestSimplerExtensionTest {

    val testSimplerExtension = TestSimplerExtension()

    companion object {

        @JvmStatic
        fun parameterClassAndSupportStatus(): List<Arguments> = listOf(
            Arguments.of(SimpleMockServer::class.java, true),
            Arguments.of(MockWebServer::class.java, true),
            Arguments.of(String::class.java, false)
        )
    }

    @ParameterizedTest
    @MethodSource("parameterClassAndSupportStatus")
    fun `supportsParameter supports appropriate classes`(clazz: Class<*>, supportStatus: Boolean) {
        val parameterContext = mockk<ParameterContext>()
        val parameter = mockk<Parameter>()
        every { parameter.type } returns clazz
        every { parameterContext.parameter } returns parameter

        testSimplerExtension.supportsParameter(parameterContext, mockk()) shouldBe supportStatus
    }
}
