// com.neojou.tools.web/HttpFetch.kt
package com.neojou.tools.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.util.network.*

open class HttpFetcher(val client: HttpClient) {

    /**
     * 執行 GET 請求，回傳 FetchResult<String>（body 為原始文字）
     * 所有錯誤已分類成 FetchResult.Error 的子類
     */
    suspend fun fetch(
        url: String,
        headers: Map<String, String> = emptyMap(),
        queryParameters: Map<String, String> = emptyMap()
    ): FetchResult<String> = try {
        val response = client.get(url) {
            headers.forEach { (k, v) -> header(k, v) }
            if (!headers.containsKey(HttpHeaders.Accept)) {
                header(HttpHeaders.Accept, "*/*")
            }
            url {
                queryParameters.forEach { (k, v) ->
                    parameters.append(k, v)
                }
            }
        }

        val status = response.status.value
        val bodyText = response.bodyAsText()

        if (status in 200..299) {
            FetchResult.Success(status, bodyText)
        } else {
            FetchResult.Error.HttpError(
                statusCode = status,
                message = "HTTP ${response.status}",
                bodyText = bodyText.take(200)  // 避免過長 log
            )
        }
    } catch (e: Throwable) {
        when (e) {
            is ConnectTimeoutException, is SocketTimeoutException ->
                FetchResult.Error.NetworkError("連線逾時：${e.message}", cause = e)
            is UnresolvedAddressException ->
                FetchResult.Error.NetworkError("無法解析網址：${e.message}", cause = e)
            else ->
                FetchResult.Error.UnknownError("未知錯誤：${e.message}", cause = e)
        }
    }

    open fun close() = client.close()
}
