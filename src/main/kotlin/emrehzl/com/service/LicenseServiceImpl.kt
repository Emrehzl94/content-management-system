package emrehzl.com.service

import emrehzl.com.repository.LicenseRepository
import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.utils.BaseResponse
import emrehzl.com.utils.Response

class LicenseServiceImpl(private val licenseRepository: LicenseRepository) : LicenseService {
    override suspend fun create(params: LicenseCreateParams): Response<Any> {
        return if (params.startTime >= params.endTime) {
            Response.Error(message = "Start time can not be equal or bigger than end time.")
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
            return Response.Error(message = "Id cannot be empty.")
        }
        val license = licenseRepository.getById(id)
        return if (license == null) {
            Response.Error(message = "License can not be found with id: $id")
        } else {
            Response.Success(data = license)
        }
    }

    override suspend fun update(params: LicenseUpdateParams): Response<Any> {
        val license = licenseRepository.update(params)
        return if (license == null) {
            Response.Error(message = "License can not be updated")
        } else {
            Response.Success(data = license)
        }
    }

    override suspend fun delete(id: String?): Response<Any> {
        if (id == null) {
            return Response.Error(message = "Id can not be empty")
        }
        licenseRepository.getById(id)
            ?: return Response.Error(message = "License can not be found with id: $id")
        licenseRepository.delete(id)
        return Response.Success(message = "License with id: $id was deleted successfully")
    }
}