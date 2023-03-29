dependencies {
    // Extend core to access proxy factory code.
    implementation(project(":zillions-core"))
    implementation("uk.co.magictractor:jura:0.0.1-SNAPSHOT")
    
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("org.assertj:assertj-core:3.24.2")
}
