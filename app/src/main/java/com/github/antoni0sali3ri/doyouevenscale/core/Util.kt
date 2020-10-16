package com.github.antoni0sali3ri.doyouevenscale.core

fun Int.trueMod(divisor: Int): Int {
    val result = this % divisor
    return if (result < 0) result + divisor else result
}