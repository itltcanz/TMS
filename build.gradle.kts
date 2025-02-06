plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.itltcanz"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.1")
    testImplementation("org.springframework.security:spring-security-test:6.4.1")

    implementation("org.springframework.boot:spring-boot-starter-cache:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.4.1")

    implementation("org.springframework.boot:spring-boot-starter-hateoas:3.4.1")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.postgresql:postgresql:42.7.4")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    implementation("org.modelmapper:modelmapper:3.2.2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
