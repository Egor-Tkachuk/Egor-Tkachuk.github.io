object DailyMessagesParser {
    fun parseMessages(rawText: String): List<DailyMessage> {
        val rawMessages = rawText.split(MESSAGE_SPLITTER).filter { it.isNotBlank() }
        val messages = rawMessages.map { parseSingleMessage(it) }
        return messages
    }

    private fun parseSingleMessage(rawMessage: String): DailyMessage {
        val name = rawMessage.substringAfter('[').substringBefore(']')
        val firstAnswer = rawMessage.substringAfter(FIRST_QUESTION).substringBefore(SECOND_QUESTION).trim()
        val secondAnswer = rawMessage.substringAfter(SECOND_QUESTION).trim()
        return DailyMessage(name, firstAnswer, secondAnswer)
    }

    private const val MESSAGE_SPLITTER = "StandupBuddy"
    const val FIRST_QUESTION = "[1/2] NEW: Which task did you successfully deliver yesterday? :clap:"
    const val SECOND_QUESTION = "[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:"
}

val messagesExample = """

StandupBuddy [isabelle]
APP  4:49 AM
@isabelle
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
• TOTD En Draft
• TOTD meeting
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• nolt management
• TOTD Fr Draft

StandupBuddy [lucio.pham]
APP  4:50 AM
@lucio.pham
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
na
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• [iOS] Relief scale to show up when adding reliefs after migraine was ended. - Jira

StandupBuddy [bo]
APP  4:55 AM
@Bo
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
• Abbvie meeting
• Amgen request
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• Review comments from Pfizer
• Pfizer SOW and SOP

StandupBuddy [thomas.gorissen]
APP  4:56 AM
@thomas.gorissen
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
n/a
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• hiring/review
• admin

StandupBuddy [syafiq]
APP  5:00 AM
@feek
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
• outcome screens is deployed to prod -- it's live! :fire:
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• Fade in/out of Taylor cards
• 2 hours of Taylor Admin exploration

StandupBuddy [debora.fournier]
APP  5:01 AM
@Deb
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
new ASO app store and playstore
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• Launch WHR month 6 campaign
• Courage 2 management (deal with delays due to card payment fail)
• Test Pfizer recruitment material

StandupBuddy [alexandre]
APP  9:24 AM
@alexandre
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
DMP update
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
Pfizer SOW
Pfizer study typeform
Abbvie invoice forecast update

StandupBuddy [egor.tkachuk]
APP  11:27 AM
@egor.tkachuk
 posted an update for The Daily:
[1/2] NEW: Which task did you successfully deliver yesterday? :clap:
-
[2/2] NEW: What are your top 3 tasks you will work on today, which one will you deliver? :pick:
• add additional relief screens into wizard flow MBI-15651
 """