package emrehzl.com.utils

import io.ktor.http.*

sealed class Response<T>(
    open val statusCode: HttpStatusCode = HttpStatusCode.OK,
) {
    data class Success<T>(
        override val statusCode: HttpStatusCode = HttpStatusCode.OK,
        val data: T? = null,
        val message: String? = null,
    ) : Response<T>()

    data class Error<T>(
        override val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
        val exception: T? = null,
        val message: String? = null,
    ) : Response<T>()
}
