class DailyRunner {
    private val messages = mutableListOf<MessageWrapper>()

    private var currentIndex: Int = -1

    private var currentMessage = DailyMessage.NONE

    private var storedTimes = mutableMapOf<String, Int>()

    fun submitRawText(rawTex: String) {
        val parsedMessages = DailyMessagesParser.parseMessages(rawTex)
        messages.clear()
        messages.addAll(parsedMessages.map { MessageWrapper(it) })
        messages.shuffle()
        storedTimes.clear()
        messages.forEach { message ->
            storedTimes[message.message.userName] = 0
        }
        currentIndex = -1
    }

    fun getTimes(): Map<String, Int> {
        return storedTimes
    }

    fun getSortedTimes() : List<Pair<String, Int>> {
        return storedTimes.toList().sortedBy{ it.second }
    }

    fun getMinTimes(): List<Pair<String, Int>> {
        val minValue = getSortedTimes().minBy { it.second }
        return getSortedTimes().filter { it.second == minValue.second }
    }

    fun getMaxTimes(): List<Pair<String, Int>> {
        val minValue = getSortedTimes().maxBy { it.second }
        return getSortedTimes().filter { it.second == minValue.second }
    }

    fun storeTimeForMessage(message: DailyMessage, time: Int) {
        if(message == DailyMessage.NONE) return
        storedTimes[message.userName] = time
    }

    fun getNext(skip: Boolean = false) : DailyMessage {
        if(currentMessage != DailyMessage.NONE) {
            messages.find { it.message == currentMessage }?.isSkipped = skip
        }
        currentIndex++
        return getNextInternal()
    }

    private fun getNextInternal() : DailyMessage {
        return if(currentIndex < messages.size) {
            currentMessage = messages[currentIndex].apply {
                isShown = true
            }.message
            currentMessage
        } else {
            val skippedMessages = messages.filter { it.isSkipped }
            if(skippedMessages.isEmpty()) {
                currentIndex = -1
                DailyMessage.NONE
            } else {
                currentMessage = skippedMessages.first().apply {
                    isShown = true
                }.message
                currentMessage
            }
        }
    }

    fun notAttend() : DailyMessage {
        storedTimes.remove(getCurrent().userName)
        messages.removeAt(currentIndex)
        return getNextInternal()
    }

    fun isAllRun(): Boolean {
        return messages.all { it.isShown && !it.isSkipped }
    }

    fun getCurrent(): DailyMessage {
        return currentMessage
    }

    class MessageWrapper(
        val message: DailyMessage
    ) {
        var isShown = false
        var isSkipped = false
    }
}