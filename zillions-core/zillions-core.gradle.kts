plugins {
    id("java-library")
}

dependencies {
    api(project(":zillions-api"))
    implementation("com.google.guava:guava:27.0.1-jre")
//    api("org.slf4j:slf4j-api:1.7.26")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    
    // JavaRandomStrategy uses zillions-env to find a BigIntByteImporter implementation.
    // BitInt implementations could chose to use JavaRandomStrategy but most will provide a custom implementation.
    //implementation(project(":zillions-env"))
}
