plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.compose")   // <-- NUEVO
}

android {
  namespace = "cl.tu.nombre.obsimod"
  compileSdk = 35

  defaultConfig {
    applicationId = "cl.tu.nombre.obsimod"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"
    vectorDrawables.useSupportLibrary = true
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
    debug { isMinifyEnabled = false }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }

  buildFeatures { compose = true }

  // ❌ BORRAR este bloque si lo tienes (el plugin de Compose lo gestiona):
  // composeOptions {
  //   kotlinCompilerExtensionVersion = "1.5.15"
  // }

  packaging.resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
}

dependencies {
  // Mantén el BOM de Compose
  val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
  implementation(composeBom)
  androidTestImplementation(composeBom)

  implementation("androidx.activity:activity-compose:1.9.2")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3:1.3.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
  debugImplementation("androidx.compose.ui:ui-tooling")

  implementation("com.github.jeziellago:compose-markdown:0.5.0")
  implementation("androidx.core:core-ktx:1.13.1")
}
