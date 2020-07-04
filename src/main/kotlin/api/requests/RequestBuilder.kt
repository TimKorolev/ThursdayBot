package api.requests


import api.RequestType.*
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase


object RequestBuilder {
    fun getHttpRequest(request: Request): HttpRequestBase? {
        return getRequestBase(request)
    }

    private fun getRequestBase(request: Request): HttpRequestBase? {
        lateinit var httpRequestBase: HttpRequestBase
        when (request.requestType) {
            GET -> HttpGet()
            DELETE -> HttpDelete()
            POST -> {
                var httpRequestPost = HttpPost("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/3e7dfd9e-ec39-42d1-a55f-98888ce2c6dc")
//                httpRequestPost.addHeader(request.headers)
//                httpRequestPost.setEntity(request.entity)
                httpRequestBase = httpRequestPost
            }
        }

        return httpRequestBase
    }
}