import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}


kotlin {
    androidLibrary {
        namespace = "com.example.jetcaster.sharedui"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        androidResources.enable = true
    }

    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    // iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    // Desktop target (JVM)
    jvm()

    sourceSets {

        commonMain.dependencies {
            // TODO do we want to use this module the same way on android and ios? Should it be the umbrella module on Android as well?
            api(projects.core.data)
            api(projects.core.domain)
            api(projects.core.designsystem)

            implementation(projects.core.domainTesting)


            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)
            // Dependency injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            // Compose Multiplatform dependencies
            implementation(compose.runtime)
            implementation(compose.foundation)

            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material3.adaptive.layout)
            implementation(libs.compose.material3.adaptive.navigation)

            implementation(compose.ui)
            // TODO this needs to be added, otherwise BackHandler build fails unresolved
            implementation(libs.compose.ui.backhandler)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.compose.navigation)

            //Image loading
            implementation(libs.coil.kt.compose)

            implementation(libs.uri.kmp)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.androidx.lifecycle.runtime.compose)

        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.activity.compose)
            // TODO we should probably use ktor, cause other platforms won't work currently
            implementation(libs.coil.network.okhttp)
            implementation(libs.androidx.window)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil.network.ktor3)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
            implementation(libs.coil.network.okhttp)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.jetcaster.shared"
    generateResClass = auto
}

compose.desktop.application {
    mainClass = "com.example.jetcaster.shared.MainKt"
}