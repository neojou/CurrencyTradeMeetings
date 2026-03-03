package com.neojou.dcical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode

import com.neojou.tools.*

/**
 * Log tag used by [CurrencyViewer] for logging UI events.
 */
private const val TAG = "DCICal"

fun dci_cal() {
    val cur_market_price = 157.4f
    val cur_sigma = 0.09f

    val base_money = 2000000.00f
    val days = 9
    val dci_order:DCI = DCI()

    dci_order.set_money(base_money)
    dci_order.set_days(days)
    dci_order.set_trade_price(157.0f)
    dci_order.set_interest_year_rate(0.1134f)


    dci_order_calculate(cur_market_price, cur_sigma, dci_order)

}

fun dci_order_calculate(cur_market_price:Float, sigma:Float, dci_order:DCI) {
    val trade_price = MyFloat.round(dci_order.get_trade_price(), 1)
    val tolerance = MyFloat.round(dci_order.cal_torance_buffer(cur_market_price), 1)
    val interest_year_rate = MyFloat.round(dci_order.get_interest_year_rate() * 100.0f, 2)
    val interest = MyFloat.round(dci_order.cal_interest(), 0)
    val cdf = MyFloat.round(
        dci_order.cal_possibility_not_trigger(cur_market_price, sigma) * 100.0f,
        4)
    MyLog.add(TAG,
        "$trade_price $tolerance $interest_year_rate%  $interest JPY $cdf%" )

}


/**
 * The primary UI screen for the DCI calculation application.
 *
 * Responsibilities (current stage):
 * - Logs a one-time "Enter" event when the screen first enters the Composition.
 * - Displays a centered placeholder UI for the upcoming data layer integration.
 * - Exposes a user action entry point ("Fetch Exchange Rates") for the next task.
 *
 * Note:
 * Logging is executed in [LaunchedEffect] to avoid being triggered on every recomposition.
 * Compose recommends using Effect APIs for side effects to ensure predictable execution timing.
 */
@Composable
fun DCICal() {
    LaunchedEffect(Unit) {
        MyLog.add(TAG, "Enter", LogLevel.DEBUG)

        dci_cal()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DCI Calculator",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "In Construction",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    MyLog.add(TAG, "DCI Calculate button is clicked", LogLevel.DEBUG)
                    // TODO(Task 2): Trigger latest + time-series fetch.
                }
            ) {
                Text("Calculate")
            }
        }
    }
}
