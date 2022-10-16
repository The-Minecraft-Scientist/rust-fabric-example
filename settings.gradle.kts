pluginManagement.repositories {
    mavenLocal()
    gradlePluginPortal()
    maven {
        name = "FabricMC"
        url = uri("https://maven.fabricmc.net/")
    }
}

rootProject.name = "rust-fabric-example"
include("rust-library:natives","rust-library:wrapper","fabric-example-mod")
