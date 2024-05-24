
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.chaquo.python")
}

android {
    namespace = "com.example.cryptostadisticsms"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.cryptostadisticsms"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "pyVersion"
    productFlavors {
        create("py38") { dimension = "pyVersion" }
        create("py310") { dimension = "pyVersion" }
        create("py311") { dimension = "pyVersion" }
        create("py39") { dimension = "pyVersion" }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

chaquopy {
    defaultConfig {
        buildPython("/usr/lib/python3.8")
        buildPython("/usr/lib/python3.10")
        buildPython("/usr/lib/python3.11")
        buildPython("C:\\Users\\sergio\\AppData\\Local\\Programs\\Python\\Python39")
    }
    productFlavors {
        getByName("py310") { version = "3.8" }
        getByName("py310") { version = "3.10" }
        getByName("py311") { version = "3.11" }
        getByName("py39") { version = "3.9" }
    }
    sourceSets {
        getByName("main") {
            srcDir("src/main/bot")
        }
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}