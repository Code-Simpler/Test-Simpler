package com.codesimpler.testsimpler

import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

/**
 * Extension for JUnit5 that provisions and handles the life cycle of [SimpleMockServer].
 * Additionally, supplies a [MockWebServer].
 */
class TestSimplerExtension : ParameterResolver, AfterEachCallback {

    companion object {
        private val serverRepository = SimpleMockServerRepository.instance

        private val supportedParameters = listOf(
            SimpleMockServer::class.java,
            MockWebServer::class.java
        )
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type in supportedParameters

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        serverRepository.get(Thread.currentThread().name)
            .let {
                when (parameterContext.parameter.type) {
                    SimpleMockServer::class.java -> it
                    MockWebServer::class.java -> it.mockWebServer
                    else -> null
                }
            }

    override fun afterEach(context: ExtensionContext?) {
        serverRepository.delete(Thread.currentThread().name)
    }
}
