val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val gdxVersion = "1.12.1"

plugins {
    application
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20"
    id("io.ktor.plugin") version "2.3.0"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sse-jvm:$ktor_version")
    implementation ("io.ktor:ktor-server-forwarded-header:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.algorand:algosdk:1.13.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

val dockerHubUsername = System.getenv("DOCKER_HUB_USERNAME") ?: "default_username"
val dockerHubPassword = System.getenv("DOCKER_HUB_PASSWORD") ?: "default_password"

println("DOCKER_HUB_USERNAME: $dockerHubUsername")
println("DOCKER_HUB_PASSWORD: $dockerHubPassword")

ktor {
    docker {
        localImageName.set("elementalbrawlonline")
        imageTag.set("0.0.1-preview")
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                80,
                8080,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))

        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "kotlin-blockchain-server" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME").forUseAtConfigurationTime(),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD").forUseAtConfigurationTime()
            )
        )
    }
}