@file:Suppress("MagicNumber")

package com.codesimpler.testsimpler

/**
 * A listing of HTTP Status codes with their "value" (number) and "description" (message).
 * Referenced from [Mozilla](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
 * and [IANA](https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml)
 */
enum class HttpStatus(val value: Int, val description: String) {

    // Informational
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    EARLY_HINTS(103, "Early Hints"),

    // Successful
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208, "Already Reported"),
    IM_USED(226, "IM Used"),

    // Redirection
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_PROXY(305, "Use Proxy"),
    UNUSED(306, "Unused"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),

    // Client Error
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    CONTENT_TOO_LARGE(413, "Content Too Large"),
    URI_TOO_LONG(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    IM_A_TEAPOT(418, "I'm A Teapot"),
    MISDIRECTED_REQUEST(421, "Misdirected Request"),
    UNPROCESSABLE_CONTENT(422, "Unprocessable Content"),
    LOCKED(423, "Locked"),
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    TOO_EARLY(425, "Too Early"),
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

    // Server Error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    LOOP_DETECTED(508, "Loop Detected"),
    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),

    ;

    companion object {
        @JvmStatic
        fun fromValue(value: Int): HttpStatus? = HttpStatus.values().find { it.value == value }
    }

    /**
     * Returns the [ResponseClass] of the [HttpStatus] by checking its numerical value.
     */
    fun responseClass(): ResponseClass? =
        when (value) {
            in 100..199 -> ResponseClass.INFORMATIONAL
            in 200..299 -> ResponseClass.SUCCESSFUL
            in 300..399 -> ResponseClass.REDIRECTION
            in 400..499 -> ResponseClass.CLIENT_ERROR
            in 500..599 -> ResponseClass.SERVER_ERROR
            else -> null
        }

    /**
     * The HTTP Response Class - i.e. 1xx is Informational, 2xx is Successful, etc.
     */
    enum class ResponseClass {
        INFORMATIONAL,
        SUCCESSFUL,
        REDIRECTION,
        CLIENT_ERROR,
        SERVER_ERROR,
    }
}
