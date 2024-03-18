package com.gurunelee.logger

fun String?.log(type: LogType = LogType.TEXT) {
    println("${type.preFix}${this}")
}

enum class LogType(
        val preFix: String,
) {
    TEXT(""),
    INFO("INFO: "),
}