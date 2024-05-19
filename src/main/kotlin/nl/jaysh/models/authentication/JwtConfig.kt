package nl.jaysh.models.authentication

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
)
