dependencies {
    // Extend core to access proxy factory code.
    implementation(project(":zillions-core"))
    implementation("uk.co.magictractor:jura:0.0.1-SNAPSHOT")
    
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    implementation("org.assertj:assertj-core:3.11.1")
}
