plugins {
    id("global.genesis.deploy") version "6.3.0"
}

description = "alpha-deploy"

dependencies {
    implementation(
        group = "global.genesis",
        name = "genesis-distribution",
        version = "6.3.0",
        classifier = "bin",
        ext = "zip"
    )
    implementation(
        group = "global.genesis",
        name = "auth-distribution",
        version = "6.3.0",
        classifier = "bin",
        ext = "zip"
    )

    api(project(":alpha-distribution", "distribution"))
    api(project(":alpha-eventhandler"))
    api(project(":alpha-messages"))
    api(project(":alpha-site-specific", "distribution"))
    // Add additional dependencies on other external distributions here

    genesisServer(
        group = "global.genesis",
        name = "genesis-distribution",
        version = "6.3.0",
        classifier = "bin",
        ext = "zip"
    )
    genesisServer(
        group = "global.genesis",
        name = "auth-distribution",
        version = "6.3.0",
        classifier = "bin",
        ext = "zip"
    )
}

task("copyDistributions", Copy::class) {
    from(configurations.default.filter { it.name.contains("distribution") }).into("$buildDir/distributions")
}

