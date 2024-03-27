plugins {
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("xzoyart.game.Main");
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}