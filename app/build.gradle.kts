plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.hiltAndroid)
}

android {
    namespace = "com.example.lloyddemoapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lloyddemoapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    // Specifies one flavor dimension.
    flavorDimensions += "version"
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
        }
        create("pro") {
            dimension = "version"
            applicationIdSuffix = ".pro"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix  = ".debug"
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation(Libraries.coreKtx)
    implementation(Libraries.lifecycle)
    implementation(Libraries.appcompat)

    // Navigation
    implementation(Libraries.navigationUiKtx)

    // Dagger with hilt
    implementation(Libraries.hiltAndroid)
    kapt(Libraries.hiltAndroidCompiler)

    // Networking with Retrofit
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitGsonConverter)
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLoggingInterceptor)

    // Compose
    implementation(Libraries.composeUi)
    debugImplementation(Libraries.composeUiRuntime)
    implementation (Libraries.materialCompose)
    implementation(Libraries.hiltNavigationCompose)

    // test libraries
    testImplementation (Libraries.junit)
    testImplementation (Libraries.mockitoCore)
    testImplementation (Libraries.mockitoInline)
    testImplementation (Libraries.coroutineTest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}