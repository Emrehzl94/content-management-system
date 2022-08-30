package emrehzl.com.service

import kotlinx.coroutines.delay

object ScheduledEventService {
    suspend fun contentStatusChange() {
        while(true) {
            delay(5000)

        }
    }
}