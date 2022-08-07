package com.codesimpler.testsimpler

import com.codesimpler.testsimpler.ResponseLoader.file
import com.codesimpler.testsimpler.ResponseLoader.resource
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ResponseLoaderTest {

    @Nested
    inner class FromFile {

        @Test
        fun `loads existing file correctly`() {
            val response = file("./src/test/resources/sample.txt")

            assertSoftly {
                response.shouldNotBeNull()
                response shouldBe "Hi!"
            }
        }

        @Test
        fun `returns null when file does not exist`() {
            file("./DNE.txt").shouldBeNull()
        }
    }

    @Nested
    inner class FromClasspath {

        @Test
        fun `loads existing file correctly`() {
            val response = resource("/sample.txt")

            assertSoftly {
                response.shouldNotBeNull()
                response shouldBe "Hi!"
            }
        }

        @Test
        fun `returns null when file does not exist`() {
            resource("/DNE.txt").shouldBeNull()
        }
    }
}
