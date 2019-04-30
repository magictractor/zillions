plugins {
    id("java-library")
}

dependencies {
    api(project(":zillions-core"))
    api("org.junit.jupiter:junit-jupiter-params:5.4.2")
    api("org.junit.platform:junit-platform-suite-api:1.4.1")
    api("org.junit.platform:junit-platform-runner:1.4.1")
    api("org.assertj:assertj-core:3.11.1")
}
