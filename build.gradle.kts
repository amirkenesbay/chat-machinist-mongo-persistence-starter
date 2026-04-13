import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("maven-publish")
}

group = "com.chatmachinist"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	maven {
		name = "GitHubPackagesChatMachinist"
		url = uri("https://maven.pkg.github.com/amirkenesbay/chat-machinist")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
		}
	}
}

val chatMachinistVersion: String by project


dependencies {
	implementation("com.chatmachinist:chat-machinist:$chatMachinistVersion")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	api("org.springframework.boot:spring-boot-starter-data-mongodb")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs.add("-Xjsr305=strict")
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

tasks.named<BootJar>("bootJar") {
	enabled = false
}

tasks.named<Jar>("jar") {
	enabled = true
}

sourceSets {
	main {
		java {
			srcDirs("src/main/kotlin")
		}
		kotlin {
			srcDirs("src/main/kotlin")
		}
	}
}


publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
	repositories {
		mavenLocal()
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/amirkenesbay/chat-machinist-mongo-persistence-starter")
			credentials {
				username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
				password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
			}
		}
	}
}