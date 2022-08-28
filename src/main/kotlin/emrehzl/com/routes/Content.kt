package emrehzl.com.routes

import emrehzl.com.reqresobjects.ContentCreateParams
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
        }
    }
}