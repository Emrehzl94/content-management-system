package emrehzl.com

import emrehzl.com.db.DatabaseFactory
import emrehzl.com.repository.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import emrehzl.com.routes.contentRoutes
import emrehzl.com.routes.licenseRoutes
import emrehzl.com.service.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import kotlinx.coroutines.launch

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()

        install(ContentNegotiation) {
            jackson()
        }

        val contentRepository: ContentRepository = ContentRepositoryImpl()
        val licenseRepository: LicenseRepository = LicenseRepositoryImpl()
        val contentLicenseRepository: ContentLicenseRepository = ContentLicenseRepositoryImpl()

        val contentService: ContentService = ContentServiceImpl(
            contentRepository, licenseRepository, contentLicenseRepository)
        val licenseService: LicenseService = LicenseServiceImpl(licenseRepository)

        contentRoutes(contentService)
        licenseRoutes(licenseService)

        launch {
            ScheduledEventService.contentStatusChange()
        }
    }.start(wait = true)
}
