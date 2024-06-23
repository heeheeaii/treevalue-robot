import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

data class TestDataClass1(val name: String, val age: Int)
data class TestDataClass2(val id: Int, val email: String)

//test
val gson = Gson()

inline fun <reified T> parseJson(data: String): T? {
    return try {
        gson.fromJson(data, T::class.java)
    } catch (e: JsonSyntaxException) {
        println("Invalid JSON format: $data")
        null
    }
}

fun method1(data: String) {
    val input: TestDataClass1? = parseJson(data)
    if (input != null) {
        println(input::class.java)
        println("Method 1 called with data: $input")
    }
}

fun method2(data: String) {
    val input: TestDataClass2? = parseJson(data)
    if (input != null) {
        println("Method 2 called with data: $input")
    }
}

val dataClassMap = hashMapOf<String, Class<*>>(
    ::method1.name to TestDataClass1::class.java,
    ::method2.name to TestDataClass2::class.java
)

fun main() {
    val map = hashMapOf<String, (String) -> Unit>(
        "k1" to ::method1,
        "k2" to ::method2
    )
    val key = "k1"
    val jsonString = "{\"name\":\"John\", \"age\":30}"

    val method = map[key]

    if (method != null) {
        method.invoke(jsonString)
    } else {
        println("Method not found for key: $key")
    }
}
