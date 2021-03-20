import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    `java-library`
}

group = "cn.bugsnet"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")

//    maven { url "https://maven.aliyun.com/repository/public/" }
//    maven { url "https://plugins.gradle.org/m2/" }
//    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    mavenCentral()
    jcenter()
    google()
    maven("https://jitpack.io")
//    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("com.google.code.gson:gson:2.8.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
