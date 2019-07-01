dependencies {
    api(project(":zillions-env"))
    implementation("uk.co.magictractor:zillions-suite:0.0.1-SNAPSHOT")
    // junit-jupiter-params for @ParameterizedTests
    api("org.junit.jupiter:junit-jupiter-params:5.4.2")
    api("org.junit.platform:junit-platform-suite-api:1.4.2")
    implementation("org.junit.platform:junit-platform-launcher:1.4.2")
    api("org.assertj:assertj-core:3.11.1")
}

description = "Zillions testbed"
