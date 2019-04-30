plugins {
    id("java-library")
}

dependencies {
    implementation(project(":zillions-core"))
    implementation("net.java.dev.jna:jna:5.2.0")
    testImplementation(project(":zillions-testbed"))
}

