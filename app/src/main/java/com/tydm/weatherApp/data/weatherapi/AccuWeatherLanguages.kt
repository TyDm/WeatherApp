package com.tydm.weatherApp.data.weatherapi

import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccuWeatherLanguages @Inject constructor() {
    val language: String
        get() = Locale.getDefault().language.takeIf { it in supportedLanguages } ?: "en"
    
    companion object {
        val supportedLanguages = listOf(
            "ar", "az", "bn", "bs", "bg", "ca", "zh", "hr", "cs", "da", "nl", "en", 
            "et", "fi", "fr", "de", "el", "he", "hi", "hu", "id", "it", "ja", "ko", 
            "lv", "lt", "mk", "ms", "no", "fa", "pl", "pt", "ro", "ru", "sr", "sk", 
            "sl", "es", "sv", "ta", "th", "tr", "uk", "ur", "vi", "cy"
        )
    }
} 