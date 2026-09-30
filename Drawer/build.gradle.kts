plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")

}

android {
    namespace = "cn.iwakeup.slidedrawer"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.material)
}

afterEvaluate {
    println("Publishing")
    val versionNumber = "0.0.2"
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "cn.iwakeup"
                artifactId = "slidedrawer"
                version = versionNumber
            }
        }
    }
}