import org.xml.sax.InputSource
import java.io.StringReader

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt.gradle.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.jacoco)
}

android {
    namespace = "com.section11.mystock"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.section11.mystock"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            testCoverage {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    detekt {
        buildUponDefaultConfig = true // preconfigure defaults
        allRules = true // activate all available (even unstable) rules.
        config.setFrom(files("$projectDir/config/detekt/detekt.yml"))
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.compose.junit4)
//    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.orchestrator)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}

jacoco {
    toolVersion = "0.8.10"
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        // Required for Android projects
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.create("jacocoTestReport", JacocoReport::class.java) {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/androidX.*",
        "**/androidx/**",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/di/**/*.*",
        "**/models/**/*.*",
        "**/database/**/*.*",
        "**/composables/**", // exclude files in composable folders
        "**/*Composable*.*", // exclude files with "composable" in their name
        "**/*Composable*.*", // exclude files with "composable" in their name
        "**/ComposableSingletons*.*", // Exclude ComposableSingletons
        "**/*Application*.*", // Exclude Application classes
        "**/*Activity*.*", // Exclude all activities
        "**/ui/theme/*.*", // Exclude ui theme
        "**/*state*.*", // Exclude files with "state" in the name (case-insensitive)
        "**/*State*.*", // Covers capitalized "State"
        "**/*UiState*.*", // Exclude specific pattern for "UiState" files
        "**/META-INF/**/*.*" // Exclude META-INF
    )
    val debugTree = fileTree("/build/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = fileTree("$projectDir/src/main/java") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(
            mapOf(
                "dir" to "${layout.buildDirectory.get()}",
                "includes" to listOf("/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
            )
        )
    )
}

tasks.register("checkCoverage") {
    dependsOn("jacocoTestReport")
    doLast {
        val xmlFile = file("./build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        val threshold = 80.0

        if (!xmlFile.exists()) {
            throw GradleException("JaCoCo XML report not found at ${xmlFile.absolutePath}")
        }

        // Parse the XML file while ignoring DTD
        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = true
        factory.isValidating = false
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

        val builder = factory.newDocumentBuilder()

        // Set a custom EntityResolver to ignore the DTD
        builder.setEntityResolver { publicId, systemId ->
            println("Ignoring DTD: PublicId=$publicId, SystemId=$systemId")
            InputSource(StringReader(""))
        }

        val xml = builder.parse(xmlFile)

        val counters = xml.getElementsByTagName("counter")
        var totalCovered = 0
        var totalMissed = 0

        for (i in 0 until counters.length) {
            val node = counters.item(i)
            if (node.attributes.getNamedItem("type").nodeValue == "LINE") {
                totalCovered += node.attributes.getNamedItem("covered").nodeValue.toInt()
                totalMissed += node.attributes.getNamedItem("missed").nodeValue.toInt()
            }
        }

        // Calculate coverage
        val coverage = (totalCovered.toDouble() / (totalCovered + totalMissed)) * 100

        if (coverage < threshold) {
            throw GradleException(
                "Coverage is below the threshold of ${threshold}%" +
                        "\nCoverage: ${coverage.format(2)}%"
            )
        }

        println("Coverage is above the threshold of ${threshold}%")
        println("Coverage: ${coverage.format(2)}%")
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)