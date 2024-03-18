package com.gurunelee.fileUtils

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Paths

/**
 * Created by chlee on 1/15/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
class FileUtilsTest {
    @Test
    fun `resources 폴더 하위의 파일을 로드한다`() {
        // when
        val file = FileUtils.resourceFile("$RESOURCE_ROOT/$FILE_NAME")
        println(file.toPath())

        // then
        Paths.get("$RESOURCE_ROOT/$FILE_NAME").let {
            assertTrue(file.exists())
            assertTrue(file.toPath() == it)
        }
    }

    companion object {
        private const val FILE_NAME = "test.txt"
        private const val RESOURCE_ROOT = "src/test/resources"

        @AfterAll
        @JvmStatic
        fun tearDown() {
            val file = FileUtils.resourceFile("$RESOURCE_ROOT/$FILE_NAME")
            if (file.exists()) {
                file.delete()
            }
        }
    }

}
