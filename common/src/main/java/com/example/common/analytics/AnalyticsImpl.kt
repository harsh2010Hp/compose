package com.example.common.analytics

import com.example.common.logs.LoggerUtils

class AnalyticsImpl : Analytics {
    override fun setScreenAnalytics(className: String?) {
        LoggerUtils.info("$className landed Screen")
    }

    override fun screenDestroyedAnalytics(className: String?) {
        LoggerUtils.info("$className destroyed Screen")
    }
}