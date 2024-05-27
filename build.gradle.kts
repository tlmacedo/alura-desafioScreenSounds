plugins {
    kotlin("jvm") version "1.9.23"
}

group = "br.com.tlmacedo.alura.desafio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("mysql:mysql-connector-java:8.0.31")
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}