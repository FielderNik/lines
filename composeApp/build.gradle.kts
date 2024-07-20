import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

buildscript {
    dependencies {
        classpath(libs.proguard.gradle)
    }
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            implementation(libs.navigation)

            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            implementation(libs.coroutines.core)

            implementation(libs.stately.concurrent.collections)

            implementation(libs.filekit.compose)
            implementation(libs.ksoup.html)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.coroutines.swing)
        }

        wasmJsMain.dependencies {
            implementation(libs.coroutines.core)
        }

        jsMain.dependencies {
            implementation(libs.coroutines.core)
        }

    }

}

android {
    namespace = "com.nikulin.lines"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.nikulin.lines"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

//val obfuscate by tasks.registering(proguard.gradle.ProGuardTask::class)

//fun mapObfuscatedJarFile(file: File) =
//    File("${project.buildDir}/tmp/obfuscated/${file.nameWithoutExtension}.min.jar")

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.nikulin.lines"
            packageVersion = "1.0.0"
        }
//        buildTypes {
//            release {
//                proguard {
////                    getDefaultProguardFile("proguard-android-optimize.txt"),
//                    "proguard-rules.pro"
//                }
//
//            }
//        }
    }


}

//obfuscate.configure {
//
////    dependsOn(tasks.jar.get())
////
////    val allJars = tasks.jar.get().outputs.files + sourceSets.main.get().runtimeClasspath.filter { it.path.endsWith(".jar") }
////        .filterNot { it.name.startsWith("skiko-awt-") && !it.name.startsWith("skiko-awt-runtime-") } // walkaround https://github.com/JetBrains/compose-jb/issues/1971
////
////    for (file in allJars) {
////        injars(file)
////        outjars(mapObfuscatedJarFile(file))
////    }
////
////    libraryjars("${compose.desktop.application.javaHome ?: System.getProperty("java.home")}/jmods")
//
//    configuration("proguard-rules.pro")
//}


