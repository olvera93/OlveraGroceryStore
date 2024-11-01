import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

	kotlin("plugin.jpa") version "1.9.23"
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.jetbrains.kotlin.kapt") version "1.9.23"
	id("org.openapi.generator") version "7.4.0"
	jacoco
}

group = "com.olvera"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

jacoco {
	toolVersion = "0.8.8"
	reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

val jwtVersion = "0.12.5"
val openApiWebMvc = "2.5.0"
val jupiterVersion = "5.10.2"
val assertjVersion = "3.25.3"
val mockkVersion = "1.13.11"




dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openApiWebMvc")
	implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

	// databases
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	// testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
	testImplementation("org.assertj:assertj-core:$assertjVersion")
	testImplementation("io.mockk:mockk:$mockkVersion")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kapt {
	correctErrorTypes = true
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	inputSpec.set("$rootDir/src/main/resources/static/api/open-api.yml")
	configFile.set("$rootDir/src/main/resources/api-config.json")
	apiPackage.set("com.olvera.groceries.api")
	modelPackage.set("com.olvera.groceries.dto")
	configOptions.set(mapOf("useSpringBoot3" to "true"))
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

configure<SourceSetContainer> {
	named("main") {
		java.srcDir("build/generate-resources/main/src/main/kotlin")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.5".toBigDecimal()
			}
		}

		rule {
			isEnabled = false
			element = "CLASS"
			includes = listOf("org.gradle.*")

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "0.3".toBigDecimal()
			}
		}
	}
}

tasks.withType<JacocoReport> {
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude()
		}
	)
}
