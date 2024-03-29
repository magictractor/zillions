version = "0.0.1-SNAPSHOT"
group = "uk.co.magictractor"

repositories {
    jcenter()
}

plugins {
    `java-library`
    `jacoco`
    
    // TODO! restore checkstyle plugin with config copied from JUnit5
    //`checkstyle`
    
    // See https://docs.gradle.org/current/userguide/publishing_maven.html
    `maven-publish`
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("org.junit.platform:junit-platform-launcher:1.9.2")
    implementation("org.assertj:assertj-core:3.11.1")

    // Use of JUnit4's @SelectClasses, @ExcludeTags etc is optional, so use compileOnly.
    // JUnitSuiteSuiteAnnotationReader should not be used if this project is not present.    
    compileOnly("org.junit.platform:junit-platform-suite-api:1.9.2")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.9.2")

    // MoreObjects is used for toString() implementations
    implementation("com.google.guava:guava:31.1-jre")
}

// See https://docs.gradle.org/current/userguide/build_environment.html
//tasks.withType<JavaCompile>().configureEach {
tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xdoclint:none", "-Xlint:none", "-nowarn")
}

tasks.withType<Test> {
    useJUnitPlatform() {
        filter {
            excludeTags("within-suite")
        }
    }
    finalizedBy("jacocoTestReport")
}

publishing {
    publications {
        create<MavenPublication>("${rootProject.name}") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
