package com.example.glassofwater.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.glassofwater.ShortcutMoshi
import com.example.glassofwater.utils.fetchData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Composable
fun HomeScreen() {
    val (shortcutsList, setShortcutsList) = remember { mutableStateOf<List<ShortcutMoshi>?>(null) }
    val (currentShortcutClicked, setCurrentShortcutClicked) = remember {
        mutableStateOf<ShortcutMoshi?>(
            null
        )
    }

    LaunchedEffect(Unit) {
        val result = runCatching {
            Thread.sleep(1000)
            val fetchedItems = fetchData("https://api.livetype.xyz/shortcuts")
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

    if (currentShortcutClicked != null) {
        return Box(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(text = "How to remember: ${currentShortcutClicked.howToRemember}")
                Text(text = "Keyboard combination: ${currentShortcutClicked.shortcut}")
                Text(text = "Editors available: ${currentShortcutClicked.editorsUsing}")
                Button(onClick = { setCurrentShortcutClicked(null) }) { Text(text = "Click") }
            }
        }
    }

    if (shortcutsList != null) {
        return Column(modifier = Modifier.padding(16.dp)) {
            shortcutsList.forEach { shortcut ->
                Text(
                    text = shortcut.shortDescription,
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            setCurrentShortcutClicked(shortcut)
                        }
                )
            }
        }
    } else {
        return Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
        ) {
            return Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Waiting for data..."
            )
        }
    }
}

