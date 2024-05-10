plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

val majorVersion = 1
val minorVersion = 0
val patchVersion = 0
val preRelease = "0"

android {
    namespace = "id.hmd.itunesmovies"
    compileSdk = 34

    signingConfigs {
        create("development") {
            storeFile = file("../App Signed/test_key")
            storePassword = "Test@123"
            keyAlias = "iTunes"
            keyPassword = "Test@234"
        }
        create("production"){
            storeFile = file("../App Signed/test_key")
            storePassword = "Test@123"
            keyAlias = "iTunes"
            keyPassword = "Test@234"
        }
    }

    defaultConfig {
        applicationId = "id.hmd.itunesmovies"
        minSdk = 21
        targetSdk = 34
        versionCode = generateVersionCode()
        versionName = generateVersionName()
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        android.defaultConfig.vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            //signingConfig = signingConfigs.getByName("development")
        }

        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create ("development") {
            buildConfigField ("int", "STAGE_TYPE", "0")
            dimension = "env"
            applicationIdSuffix = ".dev"
            resValue ("string", "app_name", "iTunesMovies-DEV")
            //signingConfig = signingConfigs.getByName("development")
        }

        create ("production") {
            buildConfigField("int", "STAGE_TYPE", "1")//buildConfigField "int", "STAGE_TYPE", "1"
            dimension = "env"
            applicationIdSuffix = ""
            resValue ("string", "app_name", "iTunesMovies")
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "iTunes Movies-${variant.versionName}-${variant.flavorName}-${variant.buildType.name}.apk"
                output.outputFileName = outputFileName
            }
    }

    viewBinding {
        enable = true
    }

    buildFeatures{
        viewBinding = true
    }

    externalNativeBuild {
        // Encapsulates your CMake build configurations.
        // For ndk-build, instead use the ndkBuild block.
        cmake {
            // Specifies a path to your CMake build script that's
            // relative to the build.gradle file.
            path = file("CMakeLists.txt")
        }
    }

    lintOptions {
        isAbortOnError = true
        warning ("InvalidPackage", "MissingPermission")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //implementation (fileTree(dir: "libs", include: ["*.jar"]))

    //Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.github.JakeWharton:ViewPagerIndicator:2.4.1")
    implementation("com.afollestad.material-dialogs:core:3.1.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Multidex
    implementation(libs.androidx.multidex)

    //NETWORK
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    //Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Chucker - Debugging API
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    //UTILS
    //Logger
    implementation("com.jakewharton.timber:timber:5.0.1")
    //Gson
    implementation("com.google.code.gson:gson:2.10.1")
    //Rx Kotlin
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation ("com.pawegio.kandroid:kandroid:0.8.7@aar")

    //rootchecker
    implementation("com.scottyab:rootbeer-lib:0.1.0")
    //aes encrypt
    implementation("com.scottyab:aescrypt:0.0.1")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    //Dagger 2 - Dependency injection
    implementation("com.google.dagger:dagger:2.48")
    implementation("com.google.dagger:dagger-android-support:2.46.1")
    kapt("com.google.dagger:dagger-compiler:2.46.1")
    kapt("com.google.dagger:dagger-android-processor:2.46.1")

    //SecurePreference
    implementation("at.favre.lib:armadillo:1.0.0")

    //DB - Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$roomVersion")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    //SQL chipper
    implementation("net.zetetic:android-database-sqlcipher:4.4.0")
    implementation("androidx.sqlite:sqlite:2.1.0")

    //VIEW LIB
    implementation("com.github.arimorty:floatingsearchview:2.1.1")
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.tapadoo.android:alerter:4.0.0")
    //Skeleton Shimmer - Loading place holder
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")
    //Glide - image
    implementation("com.github.bumptech.glide:glide:4.16.0")

}

private fun generateVersionCode():Int {
    return majorVersion * 10000 + minorVersion * 100 + patchVersion
}

private fun generateVersionName():String {
    var versionName = "$majorVersion.$minorVersion.$patchVersion"
    if (preRelease.isNotEmpty()) {
        versionName = "$versionName.$preRelease"
    }
    return versionName
}
