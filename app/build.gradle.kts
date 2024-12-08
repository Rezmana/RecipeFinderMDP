plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.devtools.ksp")
    id ("kotlin-parcelize")
//    id ("org.jetbrains.kotlin.android1.9.10")
}

android {
    namespace = "com.example.recipefinder"
    compileSdk = 35



    defaultConfig {
        applicationId = "com.example.recipefinder"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/NOTICE.md",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
                 // other conflicting META-INF bits
            )
        )
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    testOptions {
        unitTests {
            var includeAndroidResources = true
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.picasso)
    implementation(libs.firebase.firestore)
    implementation (libs.navigation.fragment.ktx.v270)
    implementation (libs.androidx.navigation.ui.ktx.v270)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.android.lottie)
    implementation (libs.androidx.room.runtime)
    implementation(libs.gson)
    implementation(libs.androidx.junit.ktx)
//    testImplementation(libs.junit.jupiter)
    testImplementation (libs.junit.jupiter.v5100)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.rules)
    androidTestImplementation(libs.junit.jupiter)
//    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.core)

    // AndroidX Test Rules library
    androidTestImplementation (libs.androidx.rules)

    // AndroidX Test Runner library
    androidTestImplementation (libs.runner)
//    androidTestImplementation ("androidx.test:runner:1.5.2")
//    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation ("androidx.test:core:1.5.0")
//    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
//    debugImplementation ("androidx.fragment:fragment-testing:1.6.2")

    // Mockito
//    androidTestImplementation (libs.mockito.core)
//    androidTestImplementation (libs.mockito.kotlin)

    // Kotlin Coroutines Test
    androidTestImplementation (libs.kotlinx.coroutines.test)

    // Arch Core Testing
    androidTestImplementation (libs.androidx.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.inline) //just uncommented this
    testImplementation(libs.kotlinx.coroutines.test.v190)
    testImplementation (libs.kotlin.test.junit)
    testImplementation (libs.mockk)
    testImplementation (libs.mockk.android)
    testImplementation (libs.junit)
    testImplementation (libs.truth)

    // Android test dependencies
//    androidTestImplementation (libs.androidx.runner.v140)

//    testImplementation ("com.google.firebase:firebase-auth:21.0.3")
//    testImplementation ("com.google.firebase:firebase-firestore:23.0.3")

//    androidTestImplementation (libs.androidx.junit.v115)
    androidTestImplementation (libs.androidx.navigation.testing)

    debugImplementation (libs.androidx.fragment.testing)

    // Espresso and JUnit for testing
//    androidTestImplementation (libs.runner)
    androidTestImplementation (libs.androidx.navigation.testing)
    // Navigation testing
    androidTestImplementation (libs.androidx.junit)
//    testImplementation (libs.junit)
    testImplementation (libs.mockito.core.v461)
    testImplementation (libs.kotlin.test)



}