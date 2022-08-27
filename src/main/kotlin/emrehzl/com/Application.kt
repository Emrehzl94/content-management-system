package emrehzl.com

import emrehzl.com.db.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import emrehzl.com.repository.ContentRepository
import emrehzl.com.repository.ContentRepositoryImpl
import emrehzl.com.routes.contentRoutes
import emrehzl.com.service.ContentService
import emrehzl.com.service.ContentServiceImpl
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()

        install(ContentNegotiation) {
            jackson()
        }

        val contentRepository: ContentRepository = ContentRepositoryImpl()
        val contentService: ContentService = ContentServiceImpl(contentRepository)
        contentRoutes(contentService)
    }.start(wait = true)
}
