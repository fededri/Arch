import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}
version = "1.0"
val javadocJar = tasks.register("javadocJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
}
val sonatypeUsername: String? = System.getenv("SONATYPE_USERNAME")
val sonatypePassword: String? = System.getenv("SONATYPE_PASSWORD")

group = "io.github.fededri.arch"
version = "0.1"

kotlin {
    android()
    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        else -> ::iosX64
    }
    iosTarget("ios") {}
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val iosMain by getting
    }
}
publishing {
    repositories {
        maven {
            name="oss"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            url = releasesRepoUrl
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("Arch-KMM")
                description.set("A KMM library for architecturing mobiles apps easily")
                url.set("https://github.com/fededri/Arch")
                licenses {
                    license {
                        name.set("MIT license")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/fededri/Arch/issues")
                }
                scm {
                    connection.set("https://github.com/.../playground-publish-kmp-to...")
                    url.set("https://github.com/.../playground-publish-kmp-to...")
                }
                developers {
                    developer {
                        name.set("Kodein Koders")
                        email.set("dev@kodein.net")
                    }
                }
            }
        }
    }
}
signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PRIVATE_PASSWORD")
    )
    sign(publishing.publications)
}
android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
}