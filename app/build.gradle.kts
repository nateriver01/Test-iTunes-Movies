
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

    lint {
        baseline = file("lint-baseline.xml")
        warning += "InvalidPackage"
        warning += "MissingPermission"
        abortOnError = true
    }

    signingConfigs {
        create("development") {
            storeFile = file("../App Signed/test_key")
            storePassword = "Test@123"
            keyAlias = "iTunes"
            keyPassword = "Test@234"
        }
        //just for
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
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.viewpagerindicator)
    implementation(libs.core)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Multidex
    implementation(libs.androidx.multidex)

    //NETWORK
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    //Retrofit2
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava3)
    implementation(libs.retrofit2.converter.gson)

    //Chucker - Debugging API
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    //UTILS
    //Logger
    implementation(libs.timber)
    //Gson
    implementation(libs.gson)
    //Rx Kotlin
    implementation(libs.rxkotlin)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation ("com.pawegio.kandroid:kandroid:0.8.7@aar")

    //rootchecker
    implementation(libs.rootbeer.lib)
    //aes encrypt
    implementation(libs.aescrypt)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    //Dagger 2 - Dependency injection
    implementation(libs.dagger)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    //SecurePreference
    implementation(libs.armadillo)

    //DB - Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.compiler)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    //SQL chipper
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)

    //VIEW LIB
    implementation(libs.floatingsearchview)
    implementation(libs.android.spinkit)
    implementation(libs.alerter)
    //Skeleton Shimmer - Loading place holder
    implementation(libs.shimmer)
    //Glide - image
    implementation(libs.glide)

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
