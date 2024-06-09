
fun Int.fromSecondsToTimeString(): String {
    val minutes = this / 60
    val seconds = this - (minutes * 60)
    val builder = StringBuilder()
    if(minutes < 10) {
        builder.append("0$minutes")
    } else {
        builder.append("$minutes")
    }
    builder.append(":")
    if(seconds < 10) {
        builder.append("0$seconds")
    } else {
        builder.append("$seconds")
    }
    return builder.toString()
}