// com.neojou.tools.web/HttpFetchJson.kt
package com.neojou.tools.web

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.SerializationException
import com.neojou.tools.*
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class HttpFetcherJson(client: HttpClient) : HttpFetcher(client) {

    companion object {
        private const val TAG = "HttpFetchJson"
    }

    internal suspend inline fun <reified T> fetchJson(
        url: String,
        headers: Map<String, String> = emptyMap(),
        queryParameters: Map<String, String> = emptyMap()
    ): FetchResult<T> {
        MyLog.add(TAG, "Enter fetchJson", LogLevel.DEBUG)

        return try {
            // 呼叫父類的 fetch() 取得文字結果
            val textResult = fetch(url, headers, queryParameters)

            when (textResult) {
                is FetchResult.Success -> {
                    val bodyText = textResult.data
                    MyLog.add(TAG, "raw body: $bodyText", LogLevel.DEBUG)

                    try {
                        // 這裡需要手動解析（因為我們沒有直接存取 HttpResponse）
                        // 但若要用 body<T>() 必須保留 HttpResponse，所以改用 executeGet
                        // 以下為暫時方案：假設上層已知是 JSON，可用 Json.decodeFromString
                        // 但為了保持 body<T>()，建議保留 response 物件
                        val body = Json.decodeFromString<T>(bodyText)
                        FetchResult.Success(textResult.statusCode, body)
                    } catch (e: SerializationException) {
                        FetchResult.Error.UnknownError("JSON 解析失敗：${e.message}", e)
                    } catch (e: Exception) {
                        FetchResult.Error.UnknownError("回應處理失敗：${e.message}", e)
                    }
                }
                is FetchResult.Error -> textResult  // 直接傳遞錯誤
            }
        } catch (e: ClientRequestException) {
            FetchResult.Error.UnknownError("回應驗證失敗：${e.message}", e)
        } catch (e: Exception) {
            FetchResult.Error.NetworkError(e.message ?: "網路請求失敗", e)
        }
    }
}