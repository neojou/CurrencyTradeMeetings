package com.neojou.tools.web

/**
 * 通用 HTTP 請求結果封裝類型。
 * 所有網路相關操作（文字、JSON、未來其他格式）統一使用這個 sealed class。
 * 
 * - Success：包含 HTTP 狀態碼與解析後資料
 * - Error：細分為 HttpError（非 2xx 狀態）、NetworkError（連線級錯誤）、UnknownError（其他）
 */
sealed class FetchResult<out T> {

    /**
     * 請求成功
     * @param statusCode HTTP 狀態碼（通常 200~299）
     * @param data 解析後的資料（String、data class、List 等）
     */
    data class Success<out T>(
        val statusCode: Int,
        val data: T
    ) : FetchResult<T>()

    /**
     * 請求失敗的基類
     */
    sealed class Error : FetchResult<Nothing>() {

        /**
         * HTTP 狀態非 2xx（例如 404、500）
         * @param statusCode 狀態碼
         * @param message 簡短描述
         * @param bodyText 可選的原始回應文字（限前 200 字，避免 log 爆炸）
         */
        data class HttpError(
            val statusCode: Int,
            val message: String,
            val bodyText: String? = null
        ) : Error()

        /**
         * 網路層級錯誤（逾時、無法連線、DNS 解析失敗等）
         * @param message 錯誤訊息
         * @param cause 原始 exception（可選）
         */
        data class NetworkError(
            val message: String,
            val cause: Throwable? = null
        ) : Error()

        /**
         * 其他未知錯誤（包含 JSON 解析失敗、內部邏輯錯誤等）
         * @param message 錯誤訊息
         * @param cause 原始 exception（可選）
         */
        data class UnknownError(
            val message: String,
            val cause: Throwable? = null
        ) : Error()
    }
}
