package api

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException

class ApiHandler {
    fun execute(request: HttpRequestBase?): CloseableHttpResponse? {
        httpClient = DefaultHttpClient()
        try {
            val response = httpClient!!.execute(request)
            httpClient!!.close()
            return response
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        var httpClient: DefaultHttpClient? = null
    }
}