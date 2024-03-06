package com.habahabamall.ShoppingCartApp.models.products

import com.habahabamall.ShoppingCartApp.constants.calculateDiscount

data class ProductsModel(
    val id: Int = 0,
    val pname: String = "",
    val shortDescription: String = "",
    val isNewProduct: Boolean = false,
    val price: Double = 0.0,
    val previewmage: String = "",
    val currency: String = "",
    val oldprice: Double = 0.0,
    val seoPname: String = "",
    val fullDescription: String = "",
    var discount: String = calculateDiscount(price, oldprice)

)