package com.tydm.weatherApp.ui.util

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openCustomTab(context: Context, url: String){
    val customTabsIntent = CustomTabsIntent.Builder()
        .build();
    customTabsIntent.launchUrl(context, url.toUri())
}