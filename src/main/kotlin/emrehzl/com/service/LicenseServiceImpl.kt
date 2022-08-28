package emrehzl.com.service

import emrehzl.com.repository.LicenseRepository
import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams
import emrehzl.com.utils.BaseResponse

class LicenseServiceImpl(private val licenseRepository: LicenseRepository) : LicenseService {
    override suspend fun create(params: LicenseCreateParams): BaseResponse<Any> {
        return if (params.startTime >= params.endTime) {
            BaseResponse.ErrorResponse("Start time can not be equal or bigger than end time.")
        } else {
            val license = licenseRepository.create(params)
            BaseResponse.SuccessResponse(data = license)
        }
    }

    override suspend fun list(): BaseResponse<Any> {
        return BaseResponse.SuccessResponse(data = licenseRepository.list())
    }

    override suspend fun getById(id: String?): BaseResponse<Any> {
        if (id == null) {
            return BaseResponse.ErrorResponse("Id cannot be empty.")
        }
        val license = licenseRepository.getById(id)
        return if (license == null) {
            BaseResponse.ErrorResponse(message = "License can not be found with id: $id")
        } else {
            BaseResponse.SuccessResponse(data = license)
        }
    }

    override suspend fun update(params: LicenseUpdateParams): BaseResponse<Any> {
        val license = licenseRepository.update(params)
        return if (license == null) {
            BaseResponse.ErrorResponse(message = "License can not be updated")
        } else {
            BaseResponse.SuccessResponse(data = license)
        }
    }

    override suspend fun delete(id: String?): BaseResponse<Any> {
        if (id == null) {
            return BaseResponse.ErrorResponse(message = "Id can not be empty")
        }
        licenseRepository.getById(id)
            ?: return BaseResponse.ErrorResponse(message = "License can not be found with id: $id")
        licenseRepository.delete(id)
        return BaseResponse.SuccessResponse(message = "License with id: $id was deleted successfully")
    }
}