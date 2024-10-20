plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.mehedi.filter"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    viewBinding {
        enable = true
    }
    kapt {
        correctErrorTypes = true
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
    
    implementation(libs.androidx.fragment.ktx)
    
    implementation(libs.epoxy)
    kapt(libs.epoxy.processor)
    implementation(libs.epoxy.databinding)
    implementation(libs.glide)
    implementation(libs.gson)

    implementation (libs.flexbox)
    implementation ("com.airbnb.android:lottie:6.3.0")
}