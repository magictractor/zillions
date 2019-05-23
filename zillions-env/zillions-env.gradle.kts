plugins {
    id("java-library")
}

dependencies {
    api(project(":zillions-api"))
    implementation("com.google.guava:guava:27.0.1-jre")
    
    testImplementation(project(":zillions-junit"))
}
