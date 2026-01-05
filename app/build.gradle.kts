plugins {
    id("com.android.application")
}

android {
    namespace = "org.weglide.copilot"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.weglide.copilot"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // No external dependencies needed
}
