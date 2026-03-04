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
        return money * interest_year_rate * days.toFloat() / total_days_in_one_year.toFloat()
    }

    fun cal_torance_buffer(cur_price: Float) : Float {
        return cur_price - trade_price
    }

    fun cal_p_not_trigger(cur_price : Float, sigma: Float) : Float {
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

    fun cal_ev(cur_price:Float, sigma: Float) : Float {
        val s_end = cal_s_end(cur_price, sigma)
        //MyLog.add(TAG, "s_end = $s_end", LogLevel.DEBUG)

        return cal_ev_with_s_end(cur_price, sigma, s_end)
    }

    fun cal_ev_with_s_end(cur_price : Float, sigma: Float, s_end: Float) : Float {
        // step 0
        val s0 = cur_price
        val k1 = trade_price
        val t = days.toFloat() / total_days_in_one_year.toFloat()

        // step 1
        val interest = cal_interest()
        val r_win : Float = interest / money
        //MyLog.add(TAG, "r_win = $r_win", LogLevel.DEBUG)

        // step 2
        val p_not_trigger = cal_p_not_trigger(cur_price, sigma)
        val p_trigger : Float = 1.0f - p_not_trigger
        //MyLog.add(TAG, "p_trigger = $p_trigger", LogLevel.DEBUG)

        // step 3 : s_end

        // step 4
        val usd:Float = (money + interest) / k1
        //MyLog.add(TAG, "usd = $usd", LogLevel.DEBUG)
        val jpy_end:Float = usd * s_end
        //MyLog.add(TAG, "jpy_end = $jpy_end", LogLevel.DEBUG)
        val r_lose = (jpy_end - money) / money
        //MyLog.add(TAG, "r_lose = $r_lose", LogLevel.DEBUG)

        val ev:Float = p_not_trigger * r_win + p_trigger * r_lose
        //MyLog.add(TAG, "ev = $ev", LogLevel.DEBUG)
        return ev
    }

    fun cal_s_end(cur_price : Float, sigma: Float) : Float{
        // step 0
        val s0 = cur_price
        val k1 = trade_price
        val t = days.toFloat() / total_days_in_one_year.toFloat()

        val ln:Float = ln(s0/k1)
        val st:Float = sigma * sqrt(t)
        val d2:Float = (ln - ((sigma * sigma)*t/2.0f)) / st
        //MyLog.add(TAG, "d2 = $d2", LogLevel.DEBUG)

        val d1:Float = d2 + st
        //MyLog.add(TAG, "d1 = $d1", LogLevel.DEBUG)

        val n_neg_d1 = NormalDistribution.cdf(-d1.toDouble())
        //MyLog.add(TAG, "n_neg_d1 = $n_neg_d1", LogLevel.DEBUG)

        val n_neg_d2 = NormalDistribution.cdf(-d2.toDouble())
        //MyLog.add(TAG, "n_neg_d2 = $n_neg_d2", LogLevel.DEBUG)

        val s_end:Float = cur_price * n_neg_d1.toFloat() / n_neg_d2.toFloat()
        //MyLog.add(TAG, "s_end = $s_end", LogLevel.DEBUG)

        return s_end
    }


}