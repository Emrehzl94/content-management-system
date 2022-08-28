package emrehzl.com.routes

import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.service.LicenseService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.licenseRoutes(licenseService: LicenseService) {

    routing {
        route("/licenses") {
            post {
                val params = call.receive<LicenseCreateParams>()
                val result = licenseService.create(params)
                call.respond(result.statusCode, result)
            }

            get {
                val result = licenseService.list()
                call.respond(result.statusCode, result)
            }

            get("{id?}") {
                val id = call.parameters["id"]
                val result = licenseService.getById(id)
                call.respond(result.statusCode, result)
            }

            put {
                val params = call.receive<LicenseUpdateParams>()
                val result = licenseService.update(params)
                call.respond(result.statusCode, result)
            }

            delete("{id?}") {
                val id = call.parameters["id"]
                val result = licenseService.delete(id)
                call.respond(result.statusCode, result)
            }
        }
    }
}