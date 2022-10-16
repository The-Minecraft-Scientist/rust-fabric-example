plugins {
    id("java")
    id("fr.stardustenterprises.rust.importer") version "3.2.5"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    rust(project(":rust-library:natives"))
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.lwjgl:lwjgl-glfw:3.3.1")
}

rustImport {
    layout.set("hierarchical")
}

tasks.withType<JavaCompile> {
    options.release.set(18)
    options.compilerArgs.add("--add-modules=jdk.incubator.foreign")
}
