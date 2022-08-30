package emrehzl.com.repository

import emrehzl.com.models.License
import emrehzl.com.reqresobjects.LicenseCreateParams
import emrehzl.com.reqresobjects.LicenseUpdateParams

interface LicenseRepository {
    suspend fun create(params: LicenseCreateParams): License?
    suspend fun list(): List<License>
    suspend fun getById(id: String): License?
    suspend fun getByIds(ids: List<String>): List<License>
    suspend fun update(params: LicenseUpdateParams): License?
    suspend fun delete(id: String)
}