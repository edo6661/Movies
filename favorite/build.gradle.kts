plugins {
  alias(libs.plugins.android.dynamic.feature)
  alias(libs.plugins.kotlin.android)
  id("org.jetbrains.kotlin.kapt")
  id("com.google.dagger.hilt.android")

}
android {

  namespace = "com.example.favorite"
  compileSdk = 34

  defaultConfig {
    minSdk = 30
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  implementation(project(":app"))
  implementation(project(":domain"))
  implementation(project(":cori"))
  implementation("com.google.android.play:core-ktx:1.8.1")



  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // image
  implementation("io.coil-kt.coil3:coil-compose:3.0.0")
  implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0")

  // navigation
  val nav_version = "2.8.3"
  implementation("androidx.navigation:navigation-compose:$nav_version")

  // hilt
  val hilt_version = "2.52"
  implementation("com.google.dagger:hilt-android:$hilt_version")
  kapt("com.google.dagger:hilt-compiler:$hilt_version")

  implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
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


  // icons
  implementation("androidx.compose.material:material-icons-core")
  implementation("androidx.compose.material:material-icons-extended")

  // lottie
  implementation("com.airbnb.android:lottie-compose:6.4.0")
  // swipe refresh
  implementation("com.google.accompanist:accompanist-swiperefresh:0.26.2-beta")


}