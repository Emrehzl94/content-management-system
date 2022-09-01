package emrehzl.com.service

import emrehzl.com.repository.LicenseRepository
import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.utils.BaseResponse
import emrehzl.com.utils.Response
import io.ktor.http.*

class LicenseServiceImpl(private val licenseRepository: LicenseRepository) : LicenseService {
    override suspend fun create(params: LicenseCreateParams): Response<Any> {
        return if (params.startTime >= params.endTime) {
            Response.Error(statusCode = HttpStatusCode.BadRequest,
                message = "Start time can not be equal or bigger than end time.")
        } else {
            val license = licenseRepository.create(params)
            Response.Success(data = license)
        }
    }

    override suspend fun list(): Response<Any> {
        return Response.Success(data = licenseRepository.list())
    }

    override suspend fun getById(id: String?): Response<Any> {
        if (id == null) {
            return Response.Error(statusCode = HttpStatusCode.BadRequest,
                message = "Id cannot be empty.")
        }
        val license = licenseRepository.getById(id)
        return if (license == null) {
            Response.Error(statusCode = HttpStatusCode.NotFound,
                message = "License can not be found with id: $id")
        } else {
            Response.Success(data = license)
        }
    }

    override suspend fun update(params: LicenseUpdateParams): Response<Any> {
        val license = licenseRepository.update(params)
        return if (license == null) {
            Response.Error(statusCode = HttpStatusCode.InternalServerError,
                message = "License can not be updated")
        } else {
            Response.Success(data = license)
        }
    }

    override suspend fun delete(id: String?): Response<Any> {
        if (id == null) {
            return Response.Error(statusCode = HttpStatusCode.BadRequest,
                message = "Id can not be empty")
        }
        licenseRepository.getById(id)
            ?: return Response.Error(statusCode = HttpStatusCode.NotFound,
                message = "License can not be found with id: $id")
        licenseRepository.delete(id)
        return Response.Success(message = "License with id: $id was deleted successfully")
    }
}