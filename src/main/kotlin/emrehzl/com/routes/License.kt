package emrehzl.com.routes

import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.service.LicenseService
import emrehzl.com.utils.ExceptionUtils
import emrehzl.com.utils.Response
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.licenseRoutes(licenseService: LicenseService) {

    routing {
        route("/licenses") {
            post {
                try {
                    val params = call.receive<LicenseCreateParams>()
                    val result = licenseService.create(params)
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
                    val result = licenseService.list()
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
                    val result = licenseService.getById(id)
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
                    val params = call.receive<LicenseUpdateParams>()
                    val result = licenseService.update(params)
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
                    val result = licenseService.delete(id)
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