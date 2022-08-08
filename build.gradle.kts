import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import java.net.URI
import java.net.URL

plugins {
    kotlin("jvm") version "1.7.10"
    jacoco
    id("com.diffplug.spotless") version "6.9.0"
    id("io.gitlab.arturbosch.detekt") version("1.21.0")
    id("org.jetbrains.dokka") version "1.7.10"
    id("com.github.johnrengelman.shadow") version("7.1.2")
    `maven-publish`
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.7.10")
    }
}

group = "com.code-simpler"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    implementation("junit:junit:4.13.2") // Security Finding, MockWebServer
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

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            artifactId = "test-simpler"

            artifact(tasks["shadowJar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Test Simpler")
                description.set("Libraries to facilitate simpler testing in your JVM projects.")
                url.set("https://github.com/Code-Simpler/Test-Simpler")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("rycowhi")
                        name.set("Ryan Whitcomb")
                        email.set("dev@code-simpler.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:Code-Simpler/Test-Simpler.git")
                    developerConnection.set("scm:git:ssh://github.com:Code-Simpler/Test-Simpler.git")
                    url.set("https://github.com/Code-Simpler/Test-Simpler")
                }
            }
        }
    }

    repositories {

        maven {
            name = "OSSRH"
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/Code-Simpler/Test-Simpler")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

    }

}

tasks.named("dokkaHtml", DokkaTask::class) {

    moduleName.set("Test Simpler")

    dokkaSourceSets {
        configureEach {
            displayName.set("Test Simpler")
            includes.from("dokka/documentation.md")

            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL("https://github.com/Code-Simpler/Test-Simpler/blob/main/src/main/kotlin"))
                remoteLineSuffix.set("#L")
            }

            jdkVersion.set(8)
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright @ 2022 Code Simpler"
        }

    }

}