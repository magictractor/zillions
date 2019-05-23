plugins {
    id("java-library")
}

dependencies {
    // Extend core to access proxy factory code.
    testImplementation(project(":zillions-core"))
}
