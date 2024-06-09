import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.toLocalDateTime

fun getCurrentDayStr(): String {
    val now = Clock.System.now()
    val localTime = now.toLocalDateTime(TimeZone.of("UTC+8"))
    return localTime.format(LocalDateTime.Format {
        this.dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    })
}