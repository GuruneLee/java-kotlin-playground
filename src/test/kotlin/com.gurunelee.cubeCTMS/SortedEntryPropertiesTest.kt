package com.gurunelee.cubeCTMS

import com.gurunelee.fileUtils.FileUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Created by chlee on 1/15/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
class SortedEntryPropertiesTest {
    @Test
    fun `소스 properties 의 key 입력 순서 대로 entry 를 반환한다`() {
        // given
        val properties = SortedEntryProperties().apply {
            setProperty("key3", "value1")
            setProperty("key1", "value2")
            setProperty("key2", "value3")
        }
        
        // when
        val entries = properties.entries
        
        // then
        assertTrue(entries.elementAt(0).key == "key3")
        assertTrue(entries.elementAt(1).key == "key1")
        assertTrue(entries.elementAt(2).key == "key2")
    }
    
    @Test
    fun `소스 파일의 key 순서대로 entry 를 반환`() {
        // given
        val file = FileUtils.resourceFile("src/test/resources/keys.properties")
        val properties = SortedEntryProperties().apply {
            load(file.inputStream())
        }

        // when
        val entries = properties.entries
        
        // then
        assertTrue(entries.elementAt(0).key == "Z")
        assertTrue(entries.elementAt(1).key == "B")
        assertTrue(entries.elementAt(2).key == "C")
        assertTrue(entries.elementAt(3).key == "AA.B")
    }
    
    @Test
    fun `소스 properties 의 key 입력순서 대로 파일에 쓰여진다 (첫줄은 날짜 주석)`() {
        // given
        val properties = SortedEntryProperties().apply {
            setProperty("key3", "value1")
            setProperty("key1", "value2")
            setProperty("key2", "value3")
        }
        
        // when
        val file = FileUtils.resourceFile("src/test/resources/test.properties")
        properties.writeTo(file)
        
        // then
        val lines = file.readLines()
        assertTrue(lines[0].startsWith("#"))
        assertTrue(lines[1].startsWith("key3"))
        assertTrue(lines[2].startsWith("key1"))
        assertTrue(lines[3].startsWith("key2"))
        
        // after
        file.delete()
    }
}
