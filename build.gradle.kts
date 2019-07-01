plugins {
    //id("project-report")
}

allprojects {
    group = "uk.co.magictractor"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {

    apply(plugin="java-library")
    apply(plugin="jacoco")
    apply(plugin="checkstyle")

    dependencies {
        // The reason for quotes around "api" and "testImplementation" is explained
        // in https://github.com/gradle/kotlin-dsl/issues/843
        "api"("org.slf4j:slf4j-api:1.7.26")
        //"testImplementation"("org.slf4j:slf4j-api:1.7.26")
        "testImplementation"("ch.qos.logback:logback-classic:1.2.3")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.5.0-RC1")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:5.5.0-RC1")
        "testImplementation"("org.assertj:assertj-core:3.11.1")
    }

    // See https://docs.gradle.org/current/userguide/build_environment.html
    //tasks.withType<JavaCompile>().configureEach {
    tasks.withType<JavaCompile> {
        options.compilerArgs = listOf("-Xdoclint:none", "-Xlint:none", "-nowarn")
        
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType<Test> {
        useJUnitPlatform() {
            filter {
                excludeTags("within-suite")
            }
        }
        finalizedBy("jacocoTestReport")
    }

}
