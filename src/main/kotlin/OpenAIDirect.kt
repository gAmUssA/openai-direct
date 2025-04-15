import java.lang.System.getenv
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

fun main() {
    // Read API key from environment variable for security
    val apiKey = getenv("OPENAI_API_KEY")
    if (apiKey.isNullOrBlank()) {
        System.err.println("[ERROR] Please set the OPENAI_API_KEY environment variable.")
        return
    }

    val client = HttpClient.newBuilder().build()
    val url = "https://api.openai.com/v1/chat/completions"

    // Minimal JSON payload for a chat completion request
    val payload = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {"role": "user", "content": "What are the best ways to maximize airline loyalty program benefits?"}
            ]
        }
    """.trimIndent()

    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Authorization", "Bearer $apiKey")
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
        .build()

    try {
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            System.err.println("[ERROR] HTTP ${response.statusCode()}")
            System.err.println(response.body())
            return
        }
        // Print the raw JSON response
        println(response.body())
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
