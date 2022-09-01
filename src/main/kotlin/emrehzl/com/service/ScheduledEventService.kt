package emrehzl.com.service

import emrehzl.com.models.ContentStatus
import emrehzl.com.repository.ContentRepository
import kotlinx.coroutines.delay

object ScheduledEventService {
    suspend fun contentStatusChange(contentRepository: ContentRepository) {
        while(true) {
            delay(20000) //todo: change the time
            for (content in contentRepository.list()) {
                val hasActiveLicense = contentRepository.hasActiveLicense(content.id)
                println("content.name: $content.name hasActiveLicense: $hasActiveLicense")
                if (content.status == ContentStatus.InProgress
                    && content.posterUrl != null
                    && content.videoUrl != null
                    && hasActiveLicense) {
                    contentRepository.updateStatus(content.id, ContentStatus.Published)
                }
                if (content.status == ContentStatus.Published
                    && !hasActiveLicense) {
                    contentRepository.updateStatus(content.id, ContentStatus.InProgress)
                }
            }
        }
    }
}