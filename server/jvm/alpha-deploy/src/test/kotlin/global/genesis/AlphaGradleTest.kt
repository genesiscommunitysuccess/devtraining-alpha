package global.genesis

import org.junit.Test
import java.nio.file.Paths
import junit.framework.TestCase.assertEquals
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class AlphaGradleTest {
    @Test
    fun testAssembleTask() {
        val rootPath = Paths.get("").toAbsolutePath().toString().removeSuffix("server\\jvm\\alpha-deploy")
        val projectDir = File(rootPath)

        // Execute the assemble task
        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("assemble")
            .build()

        // Assert that the task was successful
        assertEquals(TaskOutcome.SUCCESS, result.task(":assemble")?.outcome)
    }
}