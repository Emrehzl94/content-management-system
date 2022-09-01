package emrehzl.com.routes

import emrehzl.com.models.ContentStatus
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import emrehzl.com.service.ContentService
import emrehzl.com.utils.ExceptionUtils
import emrehzl.com.utils.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.contentRoutes(contentService: ContentService) {

    routing {
        route("/contents") {
            post {
                try {
                    val params = call.receive<ContentCreateParams>()
                    val result = contentService.create(params)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            get {
                try {
                    val name = call.request.queryParameters["name"]
                    val statusString = call.request.queryParameters["status"]
                    val status = if (statusString.isNullOrEmpty()) null else ContentStatus.valueOf(statusString)
                    val result = contentService.list(name, status)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            get("{id?}") {
                try {
                    val id = call.parameters["id"]
                    val result = contentService.getById(id)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            put {
                try {
                    val params = call.receive<ContentUpdateParams>()
                    val result = contentService.update(params)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            delete("{id?}") {
                try {
                    val id = call.parameters["id"]
                    val result = contentService.delete(id)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            post("{id}/licenses/add") {
                try {
                    val contentId = call.parameters["id"]
                    val licenseIds = call.receive<List<String>>()
                    val result = contentService.addLicenses(contentId, licenseIds)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }

            get("{id}/licenses") {
                try {
                    val contentId = call.parameters["id"]
                    val result = contentService.getLicenses(contentId)
                    call.respond(result.statusCode, result)
                } catch (e: Exception) {
                    val result = Response.Error(
                        statusCode = HttpStatusCode.InternalServerError,
                        message = e.message,
                        exception = ExceptionUtils.getExceptionNameString(e)
                    )
                    call.respond(result.statusCode, result)
                }
            }
        }
    }
}