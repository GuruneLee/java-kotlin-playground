package com.gurunelee.cubeCTMS

import com.gurunelee.fileUtils.FileUtils
import java.nio.charset.StandardCharsets


/**
 * Created by chlee on 1/11/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
fun main() {
    val resourceRoot = "src/main/resources"
    
    val srcPropertiesFile = PropertiesFile("$resourceRoot/cube/ctms/i18n/20240111_message/message_ja.properties")
    val properties = srcPropertiesFile.properties

    // properties 수정
    val srcCSVFile = CSVFile("$resourceRoot/cube/ctms/i18n/20240111_CTMS_TSV.tsv")
    srcCSVFile.readI18n(StandardCharsets.ISO_8859_1).filterNot { it.jaNew.isEmpty() }.forEach {
        properties.setProperty(it.messageId, it.jaNew)
    }
    
    // write to file
    properties.writeTo(
        FileUtils.resourceFile("$resourceRoot/cube/ctms/i18n/new_messages_ja.properties"), 
        StandardCharsets.ISO_8859_1
    )
}

