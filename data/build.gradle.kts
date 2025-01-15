plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("org.jetbrains.kotlin.kapt")
  id("androidx.room")
}

android {
  namespace = "com.example.data"
  compileSdk = 34

  defaultConfig {
    minSdk = 30

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

    buildConfigField("String", "API_BASE_URL", "\"https://api.themoviedb.org/3/\"")
    buildConfigField(
      "String",
      "ACCESS_TOKEN",
      "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MWMwMjE4ODdmNmMxZTRkZjZkMWVjN2E1NWIxNWNiMSIsIm5iZiI6MTcwMTE5MDU0Ni42MDQsInN1YiI6IjY1NjYxYjkyMTdiNWVmMDBjYmRjMTI0OSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.H_T5hNRz6a2z0r7On_FxGJUD473HZQd0JxBPVVh2iVc\""
    )
    room {
      schemaDirectory("$projectDir/schemas")
    }
  }

  buildFeatures {
    buildConfig = true
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
  implementation(project(":domain"))
  implementation(project(":cori"))
// room
  val room_version = "2.6.1"
  implementation("androidx.room:room-runtime:$room_version")
  implementation("androidx.room:room-ktx:$room_version")
  kapt("androidx.room:room-compiler:$room_version")


  // serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
  // retrofit
  implementation("com.squareup.retrofit2:retrofit:2.10.0")
  // gson converter
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
  // data store
  implementation("androidx.datastore:datastore-preferences:1.1.1")


  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}