package db

import property.Property.Companion.HEROKU_DATABASE
import property.Property.Companion.HEROKU_HOST
import property.Property.Companion.HEROKU_PASSWORD
import property.Property.Companion.HEROKU_PORT
import property.Property.Companion.HEROKU_USER

enum class Connections(val url: String) {
    HerokuDb(
        "jdbc:postgresql://${HEROKU_HOST}:${HEROKU_PORT}/${HEROKU_DATABASE}?user=${HEROKU_USER}&password=${HEROKU_PASSWORD}"
    )
}