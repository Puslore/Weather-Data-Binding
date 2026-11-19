import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

fun getApiKey(): String {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.readLines().forEach { line ->
            val trimmed = line.trim()
            if (!trimmed.startsWith("#") && trimmed.contains("=")) {
                val parts = trimmed.split("=", limit = 2)
                val key = parts[0].trim()
                if (key == "WEATHER_API_KEY" || key == "OPENWEATHER_API_KEY") {
                    return parts[1].trim().trim('"', '\'')
                }
            }
        }
    }
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
        val fromProp = localProperties.getProperty("WEATHER_API_KEY")
            ?: localProperties.getProperty("weather_api_key")
        if (!fromProp.isNullOrBlank()) {
            return fromProp.trim().trim('"', '\'')
        }
    }
    return System.getenv("WEATHER_API_KEY")
        ?: System.getenv("OPENWEATHER_API_KEY")
        ?: ""
}

android {
    namespace = "com.example.weatherdatabinding"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.weatherdatabinding"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue("string", "weather_api_key", getApiKey())
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        resValues = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}