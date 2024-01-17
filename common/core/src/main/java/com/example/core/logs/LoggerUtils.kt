package com.example.core.logs

import com.example.core.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KProperty

interface LoggerDelegate {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
}

private const val TAG = "Lloyd App"

private class LoggerUtils : LoggerDelegate {
    private val logger = Logger.getLogger("MyAppLogger")

    private fun log(level: Level, message: String) {
        if (BuildConfig.DEBUG) {
            val timestamp =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            logger.log(level, "[$timestamp][$TAG]: $message")
        }
    }

    override fun debug(message: String) {
        log(Level.FINE, message)
    }

    override fun info(message: String) {
        log(Level.INFO, message)
    }

    override fun warn(message: String) {
        log(Level.WARNING, message)
    }

    override fun error(message: String) {
        log(Level.SEVERE, message)
    }
}

class LoggerDelegateProvider {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): LoggerDelegate {
        return LoggerUtils()
    }
}