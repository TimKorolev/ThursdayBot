package api.requests

import api.RequestType
import api.RequestType.POST

enum class Request(
    val requestType: RequestType,
    val headers: String,
    val params: String,
    val entity: String
) {

    GET_TRANSLATE(
        POST,
        "Content-Type: application/json",
        "apikey=xvA0hvpQ9BE1hnU4IRubfQuFYuUBJL6Et22f-esf4jN-",
        """{"text": [Hello, world!, How are you?], "model_id":"en-es"}"""
    );

}