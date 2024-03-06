package com.habahabamall.ShoppingCartApp.models.users

data class AddCartAndWishModel(
    val userid:Int,
    val productid:Int,
    val carttype:Int,
    val attributesXML:String,
    val isfavorite:Boolean
    )
