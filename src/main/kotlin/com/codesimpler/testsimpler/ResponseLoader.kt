package com.codesimpler.testsimpler

import java.io.File

/**
 * Utility methods for loading test responses.
 */
object ResponseLoader {

    /**
     * Load the contents of a file to a [String].
     * @param filePath The file path to a file.
     */
    fun file(filePath: String): String? =
        File(filePath)
            .takeIf { it.canRead() }
            ?.readText()

    /**
     * Loads the contents of a resource on the classpath to a [String].
     * @param resourcePath The path to a resource on the classpath.
     */
    fun resource(resourcePath: String): String? =
        Thread.currentThread().javaClass
            .getResource(resourcePath)
            ?.readText()
}
