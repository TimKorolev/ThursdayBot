package api.request

import api.RequestType
import api.RequestType.*
import api.request.entities.BaseEntity

enum class Request(val requestType: RequestType,
                   private val patternUrl: String,
                   headers: HashMap<String, String>?,
                   params: String?,
                   entity: BaseEntity?) {

    CREATE_NEW_ISSUE(POST, "api/issues", null, null, NewIssueEntity()),
    DELETE_ISSUE(DELETE, "api/issues/%s", _root_ide_package_.api.request.Request.Companion.getNewHeader("Cache-Control", "No-cache"), null, null),
    GET_ISSUE_CONTENT(GET, "api/issues/%s", _root_ide_package_.api.request.Request.Companion.getNewHeader("Cache-Control", "No-cache"), "fields=\$type,id,summary,description", null);

    private val BASE_URL = System.getProperty("baseUrl")
    var fullUrl: String
        private set
    val headers = _root_ide_package_.api.request.Request.Companion.defaultHeaders
    val params: String? = null
    val entity: BaseEntity? = null

    fun setParamFullUrl(paramFullUrl: String?) {
        fullUrl = BASE_URL + String.format(patternUrl, paramFullUrl)
    }

    companion object {
        private val defaultHeaders: HashMap<String?, String?>
            private get() {
                val headers: HashMap<String?, String?> = HashMap<Any?, Any?>()
                headers["Accept"] = "Application/json"
                headers["Content-type"] = "Application/json"
                headers["Authorization"] = "Bearer " + System.getProperty("token")
                return headers
            }

        private fun getNewHeader(key: String, value: String): HashMap<String, String> {
            val paramMap = HashMap<String, String>()
            paramMap[key] = value
            return paramMap
        }
    }

    init {
        fullUrl = BASE_URL + patternUrl
        headers?.forEach { key: String?, value: String? -> this.headers[key] = value }
        if (requestType === GET) {
            this.params = params
        }
        if (entity != null && requestType !== GET) {
            this.entity = entity
        }
    }
}
