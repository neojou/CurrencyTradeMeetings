package com.neojou.dcical

import kotlin.math.*
import com.neojou.tools.*
import com.neojou.tools.sta.NormalDistribution

class DCI {
    companion object {
        private const val TAG = "DCI"
        private const val total_days_in_one_year = 360
    }

    private var money:Float = 1f
    private var trade_price:Float = 1.0f
    private var interest_year_rate:Float = 1f
    private var days = 1

    // money
    fun set_money(m:Float) {
        money = m;
    }
    fun get_money(): Float = money

    // trade_price
    fun set_trade_price(p:Float) {
        trade_price = p;
    }
    fun get_trade_price() : Float = trade_price

    // interest_year_rate
    fun set_interest_year_rate(r:Float) {
        interest_year_rate = r
    }
    fun get_interest_year_rate() : Float = interest_year_rate



    fun set_days(d: Int) {
        days = d
    }

    fun cal_interest() : Float {
        var interest : Float = money * interest_year_rate * days / total_days_in_one_year
        return interest
    }

    fun cal_torance_buffer(cur_price: Float) : Float {
        return cur_price - trade_price
    }

    fun cal_possibility_not_trigger(cur_price : Float, sigma: Float) : Float {
        val ln:Float = ln(cur_price/trade_price)
        //MyLog.add(TAG, "ln = $ln", LogLevel.DEBUG)

        val days_in_one_year_ratio : Float = days.toFloat() / total_days_in_one_year.toFloat()
        //MyLog.add(TAG, "days_in_one_year_ratio = $days_in_one_year_ratio", LogLevel.DEBUG)

        val variance:Float = ((sigma * sigma) / 2.0f) * days_in_one_year_ratio
        //MyLog.add(TAG, "variance = $variance", LogLevel.DEBUG)

        val a:Float = ln - variance
        //MyLog.add(TAG, "a= $a", LogLevel.DEBUG)

        val b:Float = sigma * sqrt(days_in_one_year_ratio)
        //MyLog.add(TAG, "b = $b", LogLevel.DEBUG)

        val d2:Float = a / b
        //MyLog.add(TAG, "d2 = $d2", LogLevel.DEBUG)

        val cdf = NormalDistribution.cdf(d2.toDouble())
        //MyLog.add(TAG, "cdf = $cdf", LogLevel.DEBUG)
        return cdf.toFloat()
    }

}