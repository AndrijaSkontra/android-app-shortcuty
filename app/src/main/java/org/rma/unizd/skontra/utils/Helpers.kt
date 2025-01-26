package org.rma.unizd.skontra.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend fun fetchData(url: String): String {
    val client = HttpClient()
    val response: HttpResponse = client.get(url)
    client.close()
    val fetchedData = response.bodyAsText()
    return fetchedData
}
