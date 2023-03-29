dependencies {
    api(project(":zillions-env"))
    implementation("uk.co.magictractor:jura:0.0.1-SNAPSHOT")
    // junit-jupiter-params for @ParameterizedTests
    api("org.junit.jupiter:junit-jupiter-params:5.9.2")
    api("org.junit.platform:junit-platform-suite-api:1.9.2")
    implementation("org.junit.platform:junit-platform-launcher:1.9.2")
    api("org.assertj:assertj-core:3.24.2")
}

description = "Zillions testbed"
