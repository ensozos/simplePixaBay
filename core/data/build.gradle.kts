plugins {
    alias(libs.plugins.simpleproject.android.library)
    alias(libs.plugins.simpleproject.android.hilt)
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
    api(projects.core.database)
    api(projects.core.network)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.pager)
}