plugins {
    alias(libs.plugins.simpleproject.android.library)
    alias(libs.plugins.simpleproject.android.hilt)
    alias(libs.plugins.simpleproject.android.room)
    id("kotlinx-serialization")
}

android {
    namespace = "com.zosimadis.data"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.pager)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.room.paging)
    testImplementation(libs.androidx.test.coroutines)
}