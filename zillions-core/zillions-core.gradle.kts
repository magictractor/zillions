dependencies {
    api(project(":zillions-api"))
    api("com.google.guava:guava:31.1-jre")
//    api("org.slf4j:slf4j-api:2.0.6")
    testImplementation("ch.qos.logback:logback-classic:1.4.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    
    // JavaRandomStrategy uses zillions-env to find a BigIntByteImporter implementation.
    // BitInt implementations could chose to use JavaRandomStrategy but most will provide a custom implementation.
    //implementation(project(":zillions-env"))
}

test {
    useJUnitPlatform()
}

description = "Zillions core classes"
