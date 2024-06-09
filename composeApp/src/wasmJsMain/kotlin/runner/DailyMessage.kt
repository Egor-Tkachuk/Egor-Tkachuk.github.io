data class DailyMessage(
    val userName: String,
    val firstAnswer: String,
    val secondAnswer: String
) {
    companion object {
        val NONE = DailyMessage("-", "-", "-")
    }
}