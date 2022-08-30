package emrehzl.com.routes

import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import emrehzl.com.service.ContentService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.contentRoutes(contentService: ContentService) {

    routing {
        route("/contents") {
            post {
                val params = call.receive<ContentCreateParams>()
                val result = contentService.create(params)
                call.respond(result.statusCode, result)
            }

            get {
                val result = contentService.list()
                call.respond(result.statusCode, result)
            }

            get("{id?}") {
                val id = call.parameters["id"]
                val result = contentService.getById(id)
                call.respond(result.statusCode, result)
            }

            put {
                val params = call.receive<ContentUpdateParams>()
                val result = contentService.update(params)
                call.respond(result.statusCode, result)
            }

            delete("{id?}") {
                val id = call.parameters["id"]
                val result = contentService.delete(id)
                call.respond(result.statusCode, result)
            }

            post("{id}/licenses/add") {
                val contentId = call.parameters["id"]
                val licenseIds = call.receive<List<String>>()
                val result = contentService.addLicenses(contentId, licenseIds)
                call.respond(result.statusCode, result)
            }

            get("{id}/licenses") {
                val contentId = call.parameters["id"]
                val result = contentService.getLicenses(contentId)
                call.respond(result.statusCode, result)
            }
        }
    }
}