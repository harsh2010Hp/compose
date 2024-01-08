package com.example.lloyddemoapplication.utils.analytics

import com.example.lloyddemoapplication.utils.logs.LoggerUtils

class AnalyticsImpl : Analytics {
    override fun setScreenAnalytics(className: String?) {
        LoggerUtils.info("$className landed Screen")
    }

    override fun screenDestroyedAnalytics(className: String?) {
        LoggerUtils.info("$className destroyed Screen")
    }
}