package net.stckoverflw.hg.util

fun Int.formatTime(): String {
    val minutes = this / 60
    val seconds = this % 60

    if (minutes > 0 && seconds == 0) return "${minutes}m"
    if (minutes > 0) return "${minutes}m ${seconds}s"
    return "${seconds}s"
}