package com.habahabamall.ShoppingCartApp.models.authentication

data class GetFireBaseToken(
    val idToken: String = "",
    val refreshToken: String = "",
    val expiresIn: String = ""
)