package com.habahabamall.ShoppingCartApp.models.users

data class UsersModel(
    var emailId: String = "",
    var password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = ""
)
