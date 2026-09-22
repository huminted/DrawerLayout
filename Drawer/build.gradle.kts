plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")

}

android {
    namespace = "cn.iwakeup.drawerlayout"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    val versionNumber = "0.0.10"
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.huminted"
                artifactId = "drawerLayout"
                version = versionNumber
            }
        }
    }
}