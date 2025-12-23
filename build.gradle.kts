plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
    id("com.gradleup.shadow") version "9.0.0-beta16"
}

allprojects {

    group = "ruben_artz.toast"
    version = "0.9"

    apply(plugin = "java")

    repositories {
        mavenCentral()
        mavenLocal()
        //maven("https://oss.sonatype.org/content/repositories/snapshots")

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // For Spigot
        maven("https://repo.papermc.io/repository/maven-public/") // For Paper

        maven("https://jitpack.io")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    if ("nms" in project.path) {
        dependencies {
            compileOnly(project(":bukkit"))
        }
    }
}

dependencies {
    implementation(project(":bukkit"))

    allprojects.filter { ":nms:" in it.path }.forEach {
        val config = if (it.path.contains("v1_16", true)) {
            "default"
        } else {
            io.papermc.paperweight.util.constants.REOBF_CONFIG
        }
        implementation(project(it.path, config))
    }
}

project(":bukkit") {
    dependencies {
        implementation("com.github.Anon8281:UniversalScheduler:0.1.7")
    }
}


tasks {

    shadowJar {
        relocate("com.github.Anon8281.universalScheduler", "ruben_artz.toast.universalScheduler")

        archiveFileName.set("Toast API.jar")
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}