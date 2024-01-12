package com.example.common.logs

import com.example.common.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.Level
import java.util.logging.Logger

object LoggerUtils {
    private val logger = Logger.getLogger("MyAppLogger")
    private const val TAG = "Lloyd App"

    private fun log(level: Level, message: String) {
        if (BuildConfig.DEBUG) {
            val timestamp =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            logger.log(level, "[$timestamp][$TAG]: $message")
        }
    }

    fun debug(message: String) {
        log(Level.FINE, message)
    }

    fun info(message: String) {
        log(Level.INFO, message)
    }

    fun warn(message: String) {
        log(Level.WARNING, message)
    }

    fun error(message: String) {
        log(Level.SEVERE, message)
    }
}