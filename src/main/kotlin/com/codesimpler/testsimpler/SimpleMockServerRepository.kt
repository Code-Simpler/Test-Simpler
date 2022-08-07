package com.codesimpler.testsimpler

/**
 * Maintains instances of [SimpleMockServer].
 */
class SimpleMockServerRepository(private val mockServers: MutableMap<String, SimpleMockServer> = mutableMapOf()) {

    companion object {
        val instance = SimpleMockServerRepository()
    }

    /**
     * Get a [SimpleMockServer] by key - creating and returning one if it doesn't exist.
     */
    fun get(key: String): SimpleMockServer = mockServers.getOrPut(key) { SimpleMockServer() }

    /**
     * Retrieve all currently tracked [SimpleMockServer]s.
     */
    fun getAll(): List<SimpleMockServer> = mockServers.entries.map { it.value }

    /**
     * Delete and close a [SimpleMockServer] by key if present.
     */
    fun delete(key: String) {
        mockServers[key]?.close()
        mockServers.remove(key)
    }
}
