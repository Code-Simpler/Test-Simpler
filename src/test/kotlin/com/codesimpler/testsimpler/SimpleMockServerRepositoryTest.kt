package com.codesimpler.testsimpler

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SimpleMockServerRepositoryTest {

    @Nested
    inner class Get {

        @Test
        fun `returns a new SimpleMockServer if one did not exist`() {
            val mockServers = mutableMapOf<String, SimpleMockServer>()
            val simpleMockServerRepository = SimpleMockServerRepository(mockServers)
            val expectedKey = "ASDFJKL;"

            simpleMockServerRepository.get(expectedKey)

            mockServers shouldHaveSize 1
        }

        @Test
        fun `returns existing SimpleMockServer if one existed`() {
            val mockServers = mutableMapOf<String, SimpleMockServer>()
            val simpleMockServerRepository = SimpleMockServerRepository(mockServers)
            val expectedKey = "ASDFJKL;"

            repeat(2) {
                simpleMockServerRepository.get(expectedKey)
            }

            mockServers shouldHaveSize 1
        }
    }

    @Test
    fun `getAll returns all existing servers`() {
        val simpleMockServerRepository = SimpleMockServerRepository()

        val expectedSize = 3
        repeat(expectedSize) {
            simpleMockServerRepository.get(it.toString())
        }

        simpleMockServerRepository.getAll() shouldHaveSize expectedSize
    }

    @Test
    fun `delete removes a server`() {
        val simpleMockServerRepository = SimpleMockServerRepository()
        val serverKey = "ASDFJKL;"

        simpleMockServerRepository.get(serverKey)
        simpleMockServerRepository.get("other-key")

        simpleMockServerRepository.delete(serverKey)

        simpleMockServerRepository.getAll() shouldHaveSize 1
    }
}
