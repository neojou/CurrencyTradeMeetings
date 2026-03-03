package com.neojou.tools.web

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*

/**
 * 建立並回傳供 Desktop 平台使用的 [HttpClient] 實例。
 *
 * 使用 CIO（Coroutine I/O）引擎，以純 Kotlin Coroutines 驅動非同步 I/O，
 * 不依賴額外的 Java 執行緒池，適合 KMP Desktop 環境。
 *
 * 設定內容：
 * - 安裝 [Logging] 插件，等級為 [LogLevel.INFO]，便於開發時觀察網路請求。
 * - 設定 `requestTimeout` 為 15 秒，避免無限等待。
 *
 * 注意：
 * 回傳的 [HttpClient] 為有狀態資源，呼叫端應在適當時機呼叫
 * [HttpClient.close] 或透過 [HttpFetcher.close] 釋放。
 *
 * @return 已設定完畢的 [HttpClient] 實例。
 */
fun createHttpClient(): HttpClient = HttpClient(CIO) {
    install(Logging) { level = LogLevel.INFO }
    engine {
        requestTimeout = 15_000
    }
}
