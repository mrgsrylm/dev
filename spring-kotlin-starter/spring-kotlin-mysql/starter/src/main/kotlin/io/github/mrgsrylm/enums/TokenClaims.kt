package io.github.mrgsrylm.enums

enum class TokenClaims(
        val value: String
){
    JWT_ID("jti"),
    TYPE("typ"),
    SUBJECT("sub"),
    ROLES("roles"),
    ID("id"),
    USERNAME("username"),
    EMAIL("email"),
    USER_FULL_NAME("userFullName"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp");
}
