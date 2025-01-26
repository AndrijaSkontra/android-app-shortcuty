package com.example.glassofwater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Creating MainActivity")
        this.enableEdgeToEdge()
        this.setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val (shortcutsList, setShortcutsList) = remember { mutableStateOf<List<ShortcutMoshi>?>(null) }

    LaunchedEffect(Unit) {
        val result = runCatching {
            Thread.sleep(1000)
            val response = fetchData("https://api.livetype.xyz/shortcuts")
            val fetchedItems = response.bodyAsText()
            println("fetched items: $fetchedItems")
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val listType = Types.newParameterizedType(List::class.java, ShortcutMoshi::class.java)
            val listAdapter = moshi.adapter<List<ShortcutMoshi>>(listType)
            setShortcutsList(listAdapter.fromJson(fetchedItems))
        }

        result.onSuccess { resp ->
            println("Success: $resp")
        }
        result.onFailure { fail ->
            println("Failed fetching or Moshi: $fail")
        }
    }
    if (shortcutsList != null) {
        return LazyColumn(modifier = Modifier.padding(16.dp)) {
            shortcutsList.forEach { shortcut ->
                item {
                    Text(
                        text = shortcut.shortDescription,
                        color = Color.Blue,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
    } else {
        return Box(modifier = Modifier.fillMaxSize().background(color = Color.LightGray)) {
            return Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Waiting for data..."
            )
        }
    }
}

suspend fun fetchData(url: String): HttpResponse {
    val client = HttpClient()
    val response: HttpResponse = client.get(url)
    client.close()
    return response
}

