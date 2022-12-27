createTasksForGithubActions()
createTasksForSamples()

fun createTasksForGithubActions() {
    tasks.register("clean") {
        dependsOn(gradle.includedBuild("kotlin-sdk").task(":kotlin:clean"))
        dependsOn(gradle.includedBuild("android-sdk").task(":android:clean"))
    }

    tasks.register("checkCodeStyle") {
        dependsOn(gradle.includedBuild("kotlin-sdk").task(":kotlin:detektMain"))
        dependsOn(gradle.includedBuild("android-sdk").task(":android:detektMain"))
    }

    tasks.register("lintAndroid") {
        dependsOn(gradle.includedBuild("android-sdk").task(":android:lint"))
    }

    tasks.register("test") {
        dependsOn(gradle.includedBuild("kotlin-sdk").task(":kotlin:test"))
        dependsOn(gradle.includedBuild("android-sdk").task(":android:testDebugUnitTest"))
    }

    tasks.register("testWithReport") {
        dependsOn(gradle.includedBuild("kotlin-sdk").task(":kotlin:koverReport"))
        dependsOn(gradle.includedBuild("android-sdk").task(":android:koverReport"))
    }

    tasks.register("publish") {
        dependsOn(gradle.includedBuild("kotlin-sdk").task(":kotlin:publish"))
        dependsOn(gradle.includedBuild("android-sdk").task(":android:publish"))
    }
}

fun createTasksForSamples() {
    val samples = listOf("android-sample-kotlin", "android-sample-java")
    samples.forEach {
        tasks.register("build-$it") {
            dependsOn(gradle.includedBuild(it).task(":app:build"))
        }
        tasks.register("clean-$it") {
            dependsOn(gradle.includedBuild(it).task(":app:clean"))
        }
    }
}
