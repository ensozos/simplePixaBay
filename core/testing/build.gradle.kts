plugins {
    alias(libs.plugins.simpleproject.android.library)
    alias(libs.plugins.simpleproject.android.hilt)
}

android {
    namespace = "com.zosimadis.testing"
}

dependencies {

    dependencies {
        api(projects.core.network)
        api(projects.core.data)
        api(projects.core.database)

        api(libs.androidx.test.coroutines)

        implementation(libs.androidx.test.rules)
        implementation(libs.hilt.android.testing)
    }
}