package com.neojou.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode

object MyFloat {

    /**
     * 將 f 四捨五入到小數點後 digits 位。
     * 例如 digits=2 => 1.234f -> 1.23f、1.235f -> 1.24f
     */
    fun round(f: Float, digits: Int): Float {
        require(digits >= 0) { "digits must be >= 0" }

        // NaN / Infinity 直接回傳，避免後續轉換例外
        if (!f.isFinite()) return f

        val num = BigDecimal.fromFloat(f)
        val rounded = num.roundToDigitPositionAfterDecimalPoint(
            digits.toLong(),
            RoundingMode.ROUND_HALF_AWAY_FROM_ZERO
        )

        // 轉回 Float：用 expanded 字串避免科學記號造成解析差異
        return rounded.toStringExpanded().toFloat()
    }
}

