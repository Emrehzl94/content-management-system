package emrehzl.com.service

import emrehzl.com.models.Content
import emrehzl.com.models.ContentStatus
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

    //todo: Add parameters and use filtered repo method.
    override suspend fun list(name: String?, status: ContentStatus?): BaseResponse<Any> {
        return BaseResponse.SuccessResponse(data = contentRepository.list())
    }

    override suspend fun getById(id: String?): BaseResponse<Any> {
        if (id == null) {
            return BaseResponse.ErrorResponse("Id cannot be empty.")
        }
        val content = contentRepository.getById(id)
            ?: return BaseResponse.ErrorResponse(message = "Content can not be found with id: $id")

        val licenses = getContentLicenses(content.id)

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

        contentRepository.getById(contentId)
            ?: return BaseResponse.ErrorResponse(message = "Content can not be found with id: $contentId")

        val licenses = licenseRepository.getByIds(licenseIds)
        val contentLicenses = getContentLicenses(contentId)

        val addedLicenses = findLicensesToAdd(contentLicenses, licenses)

        if (addedLicenses.isEmpty()) {
            return BaseResponse.ErrorResponse(message = "Any of these licenses could not be added.")
        }

        contentLicenseRepository.add(contentId, addedLicenses.map { it.id })

        return BaseResponse.SuccessResponse(message = "Licenses were added to content successfully")
    }

    override suspend fun getLicenses(contentId: String?): BaseResponse<Any> {
        if (contentId.isNullOrEmpty()) {
            return BaseResponse.ErrorResponse(message = "Content id can not be empty")
        }

        contentRepository.getById(contentId)
            ?: return BaseResponse.ErrorResponse(message = "Content can not be found with id: $contentId")

        return BaseResponse.SuccessResponse(data = getContentLicenses(contentId))
    }

    private suspend fun getContentLicenses(contentId: String): List<License> {
        val licenseIds = contentLicenseRepository.getContentLicenses(contentId)
        return licenseRepository.getByIds(licenseIds)
    }

    private fun findLicensesToAdd(contentLicenses: List<License>, licensesToAdd: List<License>): List<License> {
        val contentLicensesMut = contentLicenses.toMutableList()
        val addedLicenses = mutableListOf<License>()

        for (license in licensesToAdd) {
            var error = false
            for (contentLicense in contentLicensesMut) {
                if (license.startTime >= contentLicense.startTime && license.startTime <= contentLicense.endTime) {
                    error = true
                    break
                }
                if (license.endTime >= contentLicense.startTime && license.endTime <= contentLicense.endTime) {
                    error = true
                    break
                }
                if (contentLicense.startTime >= license.startTime && contentLicense.startTime <= license.endTime) {
                    error = true
                    break
                }
                if (contentLicense.endTime >= license.startTime && contentLicense.endTime <= license.endTime) {
                    error = true
                    break
                }
            }

            if (!error) {
                contentLicensesMut.add(license)
                addedLicenses.add(license)
            }
        }

        return addedLicenses
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