package emrehzl.com.service

import emrehzl.com.models.Content
import emrehzl.com.models.License
import emrehzl.com.repository.ContentLicenseRepository
import emrehzl.com.repository.ContentRepository
import emrehzl.com.repository.LicenseRepository
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.reqresobjects.ContentUpdateParams
import emrehzl.com.reqresobjects.SingleContentResponse
import emrehzl.com.utils.BaseResponse

class ContentServiceImpl(
    private val contentRepository: ContentRepository,
    private val licenseRepository: LicenseRepository,
    private val contentLicenseRepository: ContentLicenseRepository
) : ContentService {
    override suspend fun create(params: ContentCreateParams): BaseResponse<Any> {
        val content = contentRepository.create(params)
        return BaseResponse.SuccessResponse(data = content)
    }

    override suspend fun list(): BaseResponse<Any> {
        return BaseResponse.SuccessResponse(data = contentRepository.list())
    }

    override suspend fun getById(id: String?): BaseResponse<Any> {
        if (id == null) {
            return BaseResponse.ErrorResponse("Id cannot be empty.")
        }
        val content = contentRepository.getById(id)
            ?: return BaseResponse.ErrorResponse(message = "Content can not be found with id: $id")

        val licenseIds = contentLicenseRepository.getContentLicenses(content.id)
        val licenses = licenseRepository.getByIds(licenseIds)

        return BaseResponse.SuccessResponse(data = contentToSingleResponse(content, licenses))
    }

    override suspend fun update(params: ContentUpdateParams): BaseResponse<Any> {
        val content = contentRepository.update(params)
        return if (content == null) {
            BaseResponse.ErrorResponse(message = "Content can not be updated")
        } else {
            BaseResponse.SuccessResponse(data = content)
        }
    }

    override suspend fun delete(id: String?): BaseResponse<Any> {
        if (id == null) {
            return BaseResponse.ErrorResponse(message = "Id can not be empty")
        }
        contentRepository.getById(id)
            ?: return BaseResponse.ErrorResponse(message = "Content can not be found with id: $id")
        contentRepository.delete(id)
        return BaseResponse.SuccessResponse(message = "Content with id: $id was deleted successfully")
    }

    override suspend fun addLicenses(contentId: String?, licenseIds: List<String>): BaseResponse<Any> {
        if (contentId.isNullOrEmpty()) {
            return BaseResponse.ErrorResponse(message = "Content id can not be empty")
        }

        if (licenseIds.isEmpty()) {
            return BaseResponse.ErrorResponse(message = "LicenseId list can not be empty")
        }

        if (licenseIds.size > 5) {
            return BaseResponse.ErrorResponse(message = "License amount is too much to process")
        }

        val licenses = licenseRepository.getByIds(licenseIds)
        //todo:: Add license controls.

        licenses.filterNotNull().forEach {
            contentLicenseRepository.add(contentId, it.id)
        }

        return BaseResponse.SuccessResponse(message = "Licenses were added to content successfully")
    }

    private fun contentToSingleResponse(content: Content, licenses: List<License>): SingleContentResponse {
        return SingleContentResponse(
            id = content.id,
            name = content.name,
            year = content.year,
            summary = content.summary,
            genre = content.genre,
            status = content.status,
            posterUrl = content.posterUrl,
            videoUrl = content.videoUrl,
            createdAt = content.createdAt,
            licenses = licenses
        )
    }
}