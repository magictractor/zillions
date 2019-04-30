plugins {
    id("java-library")
}

dependencies {
    implementation(project(":zillions-core"))
    testImplementation(project(":zillions-testbed"))
}

