plugins {
    alias(libs.plugins.simpleproject.android.library)
    alias(libs.plugins.simpleproject.android.hilt)

    id("kotlinx-serialization")
}

android {
    namespace = "com.zosimadis.network"
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.http.logging)
}