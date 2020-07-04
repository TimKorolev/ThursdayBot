package db

enum class Connections(val url: String, val user: String, val password: String) {
    HerokuDb(
        "jdbc:postgresql://ec2-54-75-244-161.eu-west-1.compute.amazonaws.com:5432/de41ogue1ei9sv?user=gwqjvpawrwbbqx&password=5426fb30e0cc666420b7ef11e20826954f3439e8d863a1ecdc28983132447db7",
        "gwqjvpawrwbbqx",
        "5426fb30e0cc666420b7ef11e20826954f3439e8d863a1ecdc28983132447db7"
    )
}