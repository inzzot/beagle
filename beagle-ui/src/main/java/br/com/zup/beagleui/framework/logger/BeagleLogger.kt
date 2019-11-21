package br.com.zup.beagleui.framework.logger

import android.util.Log
import br.com.zup.beagleui.framework.setup.BeagleEnvironment
import br.com.zup.beagleui.framework.setup.Environment

private const val BEAGLE_TAG = "BeagleSDK"

internal object BeagleLogger {

    fun warning(message: String) = runIfDebug {
        Log.w(BEAGLE_TAG, message)
    }

    fun error(message: String) = runIfDebug {
        Log.e(BEAGLE_TAG, message)
    }

    fun info(message: String) = runIfDebug {
        Log.i(BEAGLE_TAG, message)
    }

    fun debug(message: String) = runIfDebug {
        Log.d(BEAGLE_TAG, message)
    }

    fun verbose(message: String) = runIfDebug {
        Log.v(BEAGLE_TAG, message)
    }

    private fun runIfDebug(runBlock: () -> Unit) {
        if (BeagleEnvironment.environment == Environment.DEBUG) {
            runBlock()
        }
    }
}