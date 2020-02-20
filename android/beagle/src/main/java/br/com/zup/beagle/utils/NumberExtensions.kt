package br.com.zup.beagle.utils

import br.com.zup.beagle.setup.BeagleEnvironment

fun Int.px(): Int = pxToDp(this.toDouble()).toInt()

fun Int.dp(): Int = dpToPx(this.toDouble()).toInt()

fun Float.px(): Float = pxToDp(this.toDouble()).toFloat()

fun Float.dp(): Float = dpToPx(this.toDouble()).toFloat()

fun Double.px(): Double = pxToDp(this)

fun Double.dp(): Double = dpToPx(this)

private fun pxToDp(value: Double): Double {
    return value / BeagleEnvironment.application.resources.displayMetrics.density
}

private fun dpToPx(value: Double): Double {
    return value * BeagleEnvironment.application.resources.displayMetrics.density
}