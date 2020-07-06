package db

enum class Connections(val url: String) {
    HerokuDb(
        "jdbc:postgresql://ec2-176-34-114-78.eu-west-1.compute.amazonaws.com:5432/d590qo8mgde1it?user=vekocwgflgqoth&password=8e768e723d48a5aba75eabb6ba11656e5fc916a62303c4a62a900df24390d0f3"
    )
}