plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.azenithtech.azenithai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.azenithtech.azenithai"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Satu APK saja untuk semua arsitektur - sengaja TIDAK di-split per-ABI
    // supaya cuma ada 1 file APK yang perlu diinstal/dibagikan (permintaan: 1 aplikasi saja).
    splits {
        abi { isEnable = false }
        density { isEnable = false }
    }
}

dependencies {
    // Sengaja TANPA AndroidX/Compose/library pihak ketiga apa pun:
    // hanya Android SDK bawaan supaya build di GitHub Actions selalu ringan & stabil,
    // tidak ada versi dependency yang bisa pecah/konflik di kemudian hari.
}
