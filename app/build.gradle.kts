plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.symbol.processing)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    jacoco
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // Ensure tests run first

    reports {
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R\$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", "**/android/**/*.*"
    )

    val debugTree = fileTree("${buildDir}/intermediates/javac/debug") {
        exclude(fileFilter)
    }
    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(buildDir).include("jacoco/testDebugUnitTest.exec"))
}

android {
    namespace = "com.example.newsly"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    testCoverage {
        jacocoVersion = "0.8.10"
    }

    defaultConfig {
        applicationId = "com.example.newsly"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "APPLICATION_TITLE", "\"Newsly\"")
            buildConfigField("String", "BASE_URL", "\"https://api.nytimes.com\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.extensions)


    // Retrofit
    implementation(libs.retrofit)

    // Coil Image Processing
    implementation(libs.coil.compose)

    //Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    //Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    //Paging
    implementation(libs.androidx.paging.runtime)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.navigation.fragment.ktx)

    //JSON Serialization
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.converter)

    //Koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)

    //Testing
    implementation(libs.junit)
}