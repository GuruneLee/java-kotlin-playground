package com.gurunelee.cubeCTMS

import com.gurunelee.fileUtils.FileUtils
import java.io.File
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * Created by chlee on 1/11/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */

class PropertiesFile(
    path: String,
) {
    val file = resourceFile(path)

    val properties: SortedEntryProperties = 
        file.inputStream().use {
            SortedEntryProperties().apply {
                load(it)
            }
        }

    private fun resourceFile(path: String): File {
        val file = FileUtils.resourceFile(path)
        if (file.extension != "properties") {
            throw IllegalArgumentException("Not a properties file")
        }

        return file
    }
}

fun Properties.writeTo(file: File, charset: Charset = StandardCharsets.UTF_8) =
    OutputStreamWriter(file.outputStream(), charset).use {
        this.store(it, null)
    }

class SortedEntryProperties: Properties() {
    private val orderedKeys: HashSet<Any> = LinkedHashSet()
    private val keyToNo: Map<Any, Int> 
        get() = orderedKeys.mapIndexed { index, s -> s to index }.toMap()

    override fun put(key: Any, value: Any?): Any? {
        orderedKeys.add(key)
        return super.put(key, value)
    }
    
    override val entries: MutableSet<MutableMap.MutableEntry<Any, Any>>
        get() {
            return super.entries
                .toSortedSet { o1, o2 ->
                    val no1 = keyToNo[o1.key] ?: 0
                    val no2 = keyToNo[o2.key] ?: 0
                    no1.compareTo(no2)
                }
        }

}
