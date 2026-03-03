package com.neojou.tools.sta

import kotlin.math.abs
import kotlin.math.exp

object NormalDistribution {
    private const val SQRT2 = 1.4142135623730951

    /** Standard normal CDF: N(z) = P(Z <= z), Z ~ N(0,1) */
    fun cdf(z: Double): Double = 0.5 * (1.0 + erf(z / SQRT2))

    fun cdfPercent(z: Double): Double = cdf(z) * 100.0

    /**
     * erf(x) approximation (Abramowitz/Stegun style).
     * Multiplatform friendly: only uses abs/exp.
     */
    fun erf(x: Double): Double {
        val sign = if (x < 0.0) -1.0 else 1.0
        val ax = abs(x)
        val t = 1.0 / (1.0 + 0.5 * ax)

        val tau = t * exp(
            -ax * ax - 1.26551223 +
                    t * (1.00002368 +
                    t * (0.37409196 +
                    t * (0.09678418 +
                    t * (-0.18628806 +
                    t * (0.27886807 +
                    t * (-1.13520398 +
                    t * (1.48851587 +
                    t * (-0.82215223 +
                    t * (0.17087277)))))))))
        )

        return sign * (1.0 - tau)
    }
}
