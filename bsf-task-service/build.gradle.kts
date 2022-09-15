plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
}

version = "final"
val mapstructVersion = "1.5.2.Final"
val restAssured: String = "4.2.0"

dependencies {

    implementation(project(":bsf-task-api"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("com.querydsl:querydsl-core")
    implementation("com.querydsl:querydsl-jpa")
    kapt("com.querydsl:querydsl-apt::jpa")
    implementation("javax.validation:validation-api")
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("com.h2database:h2")
    testImplementation("io.rest-assured:rest-assured:$restAssured")
    testImplementation("io.rest-assured:json-path:$restAssured")
    testImplementation("io.rest-assured:xml-path:$restAssured")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssured")
    testImplementation("io.rest-assured:spring-mock-mvc:$restAssured")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {

    processResources {
        filesMatching("application.yml") {
            expand(project.properties)
        }
    }

    bootJar {
        launchScript()
    }

}