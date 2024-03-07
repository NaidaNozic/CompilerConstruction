plugins {
    java
    antlr
    idea
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    antlr("org.antlr:antlr4:4.13.1")
}

tasks.compileKotlin {
    dependsOn("generateGrammarSource")
}

tasks.compileTestKotlin {
    dependsOn("generateTestGrammarSource")
}

tasks.test {
    useJUnitPlatform()
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-long-messages", "-package", "at.tugraz.ist.cc")
}

task("testRig", JavaExec::class) {
    mainClass.set("org.antlr.v4.gui.TestRig")

    val param = if (project.hasProperty("fileName")) project.property("fileName").toString() else ""
    println(param)

    args = listOf("at.tugraz.ist.cc.Jova", "program", param, "-gui")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("jasmin") {
    val jout = "out/jasmin"
    doFirst {
        mkdir(jout)
    }
    doLast {
        javaexec {
            mainClass.set("-jar")
            val param = if (project.hasProperty("fileName")) project.property("fileName").toString() else ""
            args = listOf("libs/jasmin.jar", "-d", jout, param)
        }
    }
}
tasks.clean {
    doFirst {
        delete("out/jasmin", "out")
    }
}

tasks.register("jasminAll") {
    val jout = "out/jasmin"
    doFirst {
        mkdir(jout)
    }
    doLast {
        val param = if (project.hasProperty("dir")) project.property("dir").toString() else ""
        fileTree(param).forEach { file: File ->
            run {
                javaexec {
                    mainClass.set("-jar")
                    args = listOf("libs/jasmin.jar", "-d", jout, file.absolutePath)
                }
            }
        }
    }
}

idea.module {
    generatedSourceDirs.add(file("build/generated-src/antlr/main"))
}


java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "at.tugraz.ist.cc.Main"
    }
}

tasks.register<Jar>("fatJar") {
    manifest {
        attributes["Main-Class"] = "at.tugraz.ist.cc.Main"
    }
    archiveBaseName.set("jovac")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}
