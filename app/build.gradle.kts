plugins {
    alias(libs.plugins.simpleproject.android.application)
    alias(libs.plugins.simpleproject.android.hilt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.zosimadis.simpleproject"

    defaultConfig {
        applicationId = "com.zosimadis.simpleproject"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.zosimadis.testing.PixaBayTestRunner"
        vectorDrawables {
            useSupportLibrary = true
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.pager)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.test)

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation(libs.androidx.fragment.testing.manifest)
}