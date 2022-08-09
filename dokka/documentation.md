# Module Test Simpler

Libraries to facilitate simpler testing in your Kotlin / Java / JVM projects.

## Background

Say you're writing tests with JUnit where you have to stand up a mocked back-end server for HTTP
calls that your application makes. 

Doing so is simple enough with [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) or [Wiremock](https://github.com/wiremock/wiremock).

You'll typically end up with some level of boilerplate with `@BeforeEach` or `@AfterEach` or similar semantics for your
choice of testing framework, however. 

This library serves as a wrapper for getting rid of that boilerplate and support common patterns used during tests involving 
HTTP.

## Example

Let's start with an example in Kotlin of a basic HTTP OK.

```kotlin
@ExtendWith(TestSimplerExtension::class) // Here's how you set it up! The server being setup and torn down is automatic.
internal class MyServiceTest {
    
    @Test
    fun `my test requiring a mock HTTP call`() {
        val expectedBody = "{\"escaping\" : \"isFun\"}"
        ok(body = expectedBody) 
        // Queue an HTTP OK returning a simple JSON response body
        
        // Set up your HTTP Call
        val httpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(uri()) // Grabs the endpoint that your OK response was queued up for
            .build()
        
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        
        assertEquals(200, response.code) // We got the right code!
        assertEquals(expectedBody, responseBody) // We got the right response body! 
    }
    
}
```

We know we need way more than HTTP OK. You can queue up your own response with the `response` method to customize the 
response code, headers, and body. Let's do this in Java this time.

```java
@ExtendWith(TestSimplerExtension.class)
class MyServiceTest {
    
    @Test
    void test_with_404() {
        response(HttpStatus.NOT_FOUND); 
        // response(404) acceptable as well! We provide a helpful HTTP Status reference enum

        OkHttpClient httpClient = OkHttpClient();
        Request request = Request.Builder()
                .get()
                .url(uri())
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(HttpStatus.NOT_FOUND.value, response.code); // We got the right code!
    }
    
} 
```

Let's do one more. We've all written a function that loads the contents of a file at least a thousand times over. 
To hopefully not make that 1001, we can do the below. (This is by no mean's necessary - the mock responses take in a 
String so feel free to that that value in however you want!)

```kotlin
@ExtendWith(TestSimplerExtension::class)
internal class MyServiceTest {

    @Test
    fun `my test requiring a OK call with a body`() {
        ok(body = fromResource("my-response.json")) 
        // Save a step for figuring out classpath, there's fromFile too! 
        
        val httpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(uri())
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        assertEquals(HttpStatus.OK.value, response.code)
        assertEquals("{\"some\"\"json_in_my_file\"}", responseBody) 
        // The contents of my-response.json on the classpath
    }

}
```

Take a look at the [Documentation](https://code-simpler.github.io/Test-Simpler/) for more details and to see the other utilities available.

Don't want all this? This test extension also provides an instance of [MockWebServer](https://github.com/square/okhttp/blob/master/mockwebserver/src/main/kotlin/mockwebserver3/MockWebServer.kt) 
via [JUnit's Dependency Injection](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection)
for tests requiring more advanced behavior.

## Supported Frameworks

* [JUnit5](https://junit.org/junit5/docs/current/user-guide/)

Other testing where you want per-thread HTTP mocks can work as well - you're on the hook for handling its start / stop.
Take a look at the documentation for examples of how you could do this using SimpleMockServerRepository.

Others may come in the future.

## Setup

### 1. Add the Dependency 

Setup via Maven

```xml
<dependency>
    <groupId>com.code-simpler</groupId>
    <artifactId>test-simpler</artifactId>
    <version>${YOUR.VERSION}</version>
</dependency>
```

Or Gradle

```kotlin
implementation("com.code-simpler:test-simpler:${YOUR.VERSION}")
```

### 2. Add the Extension to Your Test

```kotlin
@ExtendWith(TestSimplerExtension::class)
internal class MyTest {
    
    // ... your tests here
    
}
```

### 3. Mock HTTP Away!

```kotlin

@Test
fun `my test()`() {
    response(
        responseCode=200, 
        headers=mapOf("Content-Type", "application/json"), 
        body=fromResource("test.json")
    )
    
    val mockServiceEndpoint = uri()
    
    // ... make an HTTP call with the above endpoint!
    
}

```

# Package com.codesimpler.testsimpler

Contains test extensions and helpers for HTTP and File usage.