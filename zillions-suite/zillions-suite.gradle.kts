dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    implementation("org.junit.platform:junit-platform-launcher:1.4.2")
    implementation("org.assertj:assertj-core:3.11.1")

    // Use of JUnit4's @SelectClasses, @ExcludeTags etc is optional, so use compileOnly.
    // JUnitSuiteSuiteAnnotationReader should not be used if this project is not present.    
    compileOnly("org.junit.platform:junit-platform-suite-api:1.4.2")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.4.2")

    // MoreObjects is used for toString() implementations
    implementation("com.google.guava:guava:27.0.1-jre")
}
