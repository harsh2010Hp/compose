package com.example.common.analytics

interface Analytics  {
    fun setScreenAnalytics(className: String?)
    fun screenDestroyedAnalytics(className : String?)
}