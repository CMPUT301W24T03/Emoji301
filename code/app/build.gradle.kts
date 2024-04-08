plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.emojibrite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.emojibrite"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // image retrival
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.test.uiautomator:uiautomator-v18:2.2.0-alpha1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    // Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    // sdk related for javadocs
    compileOnly(files("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //add navigation component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.activity:activity:1.8.2")
    //Unit testing + espresso
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") {
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Fragment + fragment scenario testing
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    // NavigationTesting for navigation graph
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // firebase stuff
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.karumi:dexter:6.2.2")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")





    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation ("com.google.guava:guava:31.1-jre")







    implementation("pub.devrel:easypermissions:3.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    //QR Code dependency
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    


    // Firebase Cloud Messaging : Notifications + OKHTTP
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics:21.6.1")
}



