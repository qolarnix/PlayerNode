plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}


group = "io.github.qolarnix"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("org.slf4j:slf4j-nop:2.0.7")
    implementation("net.minestom:minestom-snapshots:5edc7c1af9")
    implementation("com.github.TogAr2:MinestomPvP:master-SNAPSHOT")
    implementation("dev.hollowcube:polar:1.14.7")
}



tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "io.github.qolarnix.Node"
        }
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }

    register<Copy>("copyJarToMinecraft") {
        from(layout.buildDirectory.dir("libs"))
        include("*.jar")
        into("/Users/polarnix/minecraft/playernode")
        rename { "server.jar" }
    }
}