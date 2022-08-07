package com.codesimpler.testsimpler

/**
 * A collection of methods for stubbing out HTTP responses.
 * All create a mocked response against the executing [Thread]'s name, creating isolation per test Thread pool
 * to allow parallel tests to work in isolation.
 *
 * Note: If called from an additional thread within a test case the isolation will not work so these responses
 * should be called at the level of the thread in the root of the test case itself.
 */
object Simple {

    /**
     * Create a mocked HTTP response matching this thread's HTTP Client.
     * @param responseCode The HTTP response code supplied as an [Int].
     * @param headers Any HTTP headers to return on the mocked response.
     * @param body The body of the mocked HTTP response, as a [String].
     */
    fun response(responseCode: Int = HttpStatus.OK.value, headers: Map<String, Any> = mapOf(), body: String? = null) =
        SimpleMockServerRepository.instance.get(Thread.currentThread().name)
            .addResponse(responseCode, headers, body)

    /**
     * Create a mocked HTTP response matching this thread's HTTP Client.
     * @param httpStatus The HTTP response code supplied as an [HttpStatus].
     * @param headers Any HTTP headers to return on the mocked response.
     * @param body The body of the mocked HTTP response, as a [String].
     */
    fun response(httpStatus: HttpStatus = HttpStatus.OK, headers: Map<String, Any> = mapOf(), body: String? = null) =
        response(httpStatus.value, headers, body)

    /**
     * Create a mocked HTTP response matching this thread's HTTP Client as an [HttpStatus.OK]
     * @param headers Any HTTP headers to return on the mocked response.
     * @param body The body of the mocked HTTP response, as a [String].
     */
    fun ok(headers: Map<String, Any> = mapOf(), body: String? = null) =
        response(HttpStatus.OK, headers, body)

    /**
     * Create a mocked HTTP response matching this thread's HTTP Client as an [HttpStatus.NOT_FOUND]
     * @param headers Any HTTP headers to return on the mocked response.
     * @param body The body of the mocked HTTP response, as a [String].
     */
    fun notFound(headers: Map<String, Any> = mapOf(), body: String? = null) =
        response(HttpStatus.NOT_FOUND, headers, body)

    /**
     * Get the URI of this thread's [SimpleMockServer]. Can be used to pass to HTTP clients / calls under test.
     * @param path A path to extend the base URL this thread's HTTP client serves.
     * @param body The body of the mocked HTTP response, as a [String].
     */
    fun uri(path: String? = null): String =
        SimpleMockServerRepository.instance.get(Thread.currentThread().name)
            .uri(path)

    /**
     * Get this thread's instance of [SimpleMockServer].
     */
    fun mockServer(): SimpleMockServer =
        SimpleMockServerRepository.instance.get(Thread.currentThread().name)
}
