package com.example.lloyddemoapplication.utils.analytics

interface Analytics  {
    fun setScreenAnalytics(className: String?)
    fun screenDestroyedAnalytics(className : String?)
}