/*
 * This implementation uses java.net.URL and java.net.HttpURLConnection which are available in the JVM.
 * For true Kotlin Multiplatform compatibility, you would need to:
 * 
 * 1. Create an expect/actual declaration for HTTP client functionality:
 *    - expect class HttpClient { ... }
 *    - actual class HttpClient { ... } (platform-specific implementations)
 * 
 * 2. Use Kotlin's IO utilities from the stdlib where possible
 * 
 * 3. Consider using a multiplatform HTTP client library like Ktor Client
 *    if more complex HTTP functionality is needed
 */

import java.net.URL
import java.net.HttpURLConnection

fun main() {
    // Read API key from environment variable for security
    val apiKey = System.getenv("OPENAI_API_KEY")
    if (apiKey.isNullOrBlank()) {
        System.err.println("[ERROR] Please set the OPENAI_API_KEY environment variable.")
        return
    }

    // Create URL in a way that's compatible with Kotlin Multiplatform
    val urlString = "https://api.openai.com/v1/chat/completions"
    val url = URL(urlString)

    // Minimal JSON payload for a chat completion request
    val payload = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {"role": "user", "content": "What are the best ways to maximize airline loyalty program benefits?"}
            ]
        }
    """.trimIndent()

    try {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer $apiKey")
        connection.doOutput = true

        // Write the payload to the request body
        connection.outputStream.use { os ->
            val input = payload.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }

        val responseCode = connection.responseCode

        if (responseCode != 200) {
            System.err.println("[ERROR] HTTP $responseCode")
            connection.errorStream?.let { errorStream ->
                val errorResponse = errorStream.bufferedReader().use { it.readText() }
                System.err.println(errorResponse)
            }
            return
        }

        // Read and print the response
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        println(response)
    } catch (e: Exception) {
        System.err.println("[ERROR] Exception during API call: ${e.message}")
    }

    /*
    Limitations of this approach compared to using a framework like LangChain4j:
    - You must manually handle HTTP, JSON, and error handling (boilerplate code).
    - No abstractions for prompt management, retries, or streaming responses.
    - No type-safety or helper classes for request/response objects.
    - No built-in support for chaining, memory, or integrations.
    - Security and API key management are manual.
    Frameworks provide higher-level abstractions, better error handling, and more features for production use.
    */
}
