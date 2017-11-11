package com.windowsonmac

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.HashMap

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun getProducts(): HashMap<Int, Product> {
    val products = HashMap<Int, Product>()
    Files.lines(Paths.get("product.csv")).forEach {
        val strings = it.split(",")
        if(strings[0] != "SKU_ID") {
            val id = strings[0].toInt()
            val h = strings[1].toDouble()
            val l = strings[2].toDouble()
            val w = strings[3].toDouble()
            val weight = strings[4].toDouble()
            val vol = strings[5].toDouble()
            val category = strings[6].toInt()
            products[id] = Product(h, l, w, weight, vol, category, id)
        }
    }
    return products
}
