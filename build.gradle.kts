import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    jacoco
    kotlin("jvm") version "1.7.10"
    id("com.diffplug.spotless") version "6.9.0"
    id("io.gitlab.arturbosch.detekt") version("1.21.0")
    id("org.jetbrains.dokka") version "1.7.10"
}

group = "com.code-simpler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.10.0")

    testImplementation(kotlin("test"))

}

testing {
    suites {

        configureEach {
            if (this is JvmTestSuite) {
                useJUnitJupiter()
                dependencies {
                    implementation(project)
                    implementation("io.kotest:kotest-assertions-core-jvm:5.4.1")
                    implementation("io.mockk:mockk:1.12.5")
                }
            }
        }

        val integrationTest by registering(JvmTestSuite::class)
        val integrationTestImplementation by configurations.getting {
            extendsFrom(configurations.testImplementation.get())
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }

    }
}

spotless {
    kotlin {
        ktlint()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
    }
}

tasks.named("test") {
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
    dependsOn(tasks.named("jacocoTestCoverageVerification"))
}