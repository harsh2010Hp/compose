package com.example.core.analytics

import com.example.core.logs.LoggerDelegateProvider

class AnalyticsImpl : Analytics {

    private val logger by LoggerDelegateProvider()

    override fun setScreenAnalytics(className: String?) {
        // To call set screen analytics
        logger.info("Screen Analytics set for $className")
    }

    override fun screenDestroyedAnalytics(className: String?) {
        // To call screen destroyed analytics
        logger.info("Screen finished for $className")
    }
}