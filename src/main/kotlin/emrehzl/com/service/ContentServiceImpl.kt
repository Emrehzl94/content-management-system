package emrehzl.com.service

import emrehzl.com.repository.ContentRepository
import emrehzl.com.reqresobjects.ContentCreateParams
import emrehzl.com.utils.BaseResponse

class ContentServiceImpl(private val contentRepository: ContentRepository): ContentService {
    override suspend fun create(params: ContentCreateParams): BaseResponse<Any> {
        val content = contentRepository.create(params)
        return BaseResponse.SuccessResponse(data = content)
    }

    override suspend fun list(): BaseResponse<Any> {
        return BaseResponse.SuccessResponse(data = contentRepository.list())
    }

    override suspend fun getById(id: String): BaseResponse<Any> {
        val content = contentRepository.getById(id)
        return if (content == null) {
            BaseResponse.ErrorResponse(message = "Content can not be found with id: $id")
        } else {
            BaseResponse.SuccessResponse(data = content)
        }
    }
}