package com.codesimpler.testsimpler

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class HttpStatusTest {

    companion object {
        @JvmStatic
        fun provideEnumAndResponseClass(): List<Arguments> =
            listOf(
                Arguments.of(HttpStatus.CONTINUE, HttpStatus.ResponseClass.INFORMATIONAL),
                Arguments.of(HttpStatus.OK, HttpStatus.ResponseClass.SUCCESSFUL),
                Arguments.of(HttpStatus.FOUND, HttpStatus.ResponseClass.REDIRECTION),
                Arguments.of(HttpStatus.NOT_FOUND, HttpStatus.ResponseClass.CLIENT_ERROR),
                Arguments.of(HttpStatus.BAD_GATEWAY, HttpStatus.ResponseClass.SERVER_ERROR)
            )
    }

    @ParameterizedTest
    @MethodSource("provideEnumAndResponseClass")
    fun `responseClass returns correct class for HttpStatus`(
        httpStatus: HttpStatus,
        responseClass: HttpStatus.ResponseClass
    ) {
        httpStatus.responseClass() shouldBe responseClass
    }

    @Nested
    inner class FromValue {

        @Test
        fun `returns valid HttpStatus`() {
            HttpStatus.fromValue(200) shouldBe HttpStatus.OK
        }

        @Test
        fun `returns null for invalid code`() {
            HttpStatus.fromValue(9001).shouldBeNull()
        }
    }
}
