package com.gurunelee.cubeCTMS

import com.gurunelee.fileUtils.FileUtils
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Created by chlee on 1/11/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
class CSVFile(
    path: String,
) {
    val file = resourceFile(path)

    fun CSVFile.getContentLines(charset: Charset) =
        this.file.readLines(charset).subList(1, this.file.readLines().size)

    private fun resourceFile(path: String): File {
        val file = FileUtils.resourceFile(path)
        if (file.extension != "csv" && file.extension != "tsv") {
            throw IllegalArgumentException("Not a CSV or TSV file")
        }

        return file
    }
}

fun CSVFile.readI18n(charset: Charset = StandardCharsets.UTF_8): List<I18nRow> =
    getContentLines(charset).map {
        val row = it.split("\t")

        I18nRow(
            no = row[0].toInt(),
            messageId = row[1],
            en = row[2],
            enNew = row[3],
            ja = row[4],
            jaNew = row[5],
            zh = row[6],
            zhNew = row[7],
        )
    }

data class I18nRow(
    val no: Int,
    val messageId: String,
    val en: String,
    val enNew: String,
    val ja: String,
    val jaNew: String,
    val zh: String,
    val zhNew: String,
)
