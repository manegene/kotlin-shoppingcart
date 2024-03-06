package com.habahabamall.ShoppingCartApp.models.authentication

data class OrderFireBaseToken(
    val email: String = "dev@habahabamall.com",
    val password: String = "test123",
    val returnSecureToken: Boolean = true
)