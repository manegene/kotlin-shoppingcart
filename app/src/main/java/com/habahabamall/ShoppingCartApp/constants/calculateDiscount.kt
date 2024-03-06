package com.habahabamall.ShoppingCartApp.constants

fun calculateDiscount(newPrice: Double, oldPrice: Double): String {
    var discount: String = if (newPrice.toInt() > 0 && oldPrice.toInt() > 0)
        ((oldPrice - newPrice) / 100f).toString()
    else
        (0 / 100f).toString()

    return "$discount% off!"
}