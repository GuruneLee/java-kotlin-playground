package com.gurunelee.fileUtils

import java.io.File

/**
 * Created by chlee on 1/11/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
class FileUtils {
    companion object {
        
        fun resourceFile(path: String): File =
            File(path).apply {
                // if not exists, create file, directory
                if (!exists()) {
                    // log
                    println("Create file: $this")
                    parentFile.mkdirs()
                    createNewFile()
                }
            }
    }
}
