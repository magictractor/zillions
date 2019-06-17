dependencies {
    api("org.junit.platform:junit-platform-suite-api:1.4.2")
    implementation("org.junit.platform:junit-platform-launcher:1.4.2")
    implementation("org.assertj:assertj-core:3.11.1")
    
    // MoreObjects is used for toString() implementations
    implementation("com.google.guava:guava:27.0.1-jre")
}
