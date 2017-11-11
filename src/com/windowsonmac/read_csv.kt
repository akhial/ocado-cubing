package com.windowsonmac

import java.util.*

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun main(args: Array<String>) {
    for(order in getOrders()) {
        print("order ${order.first}: ${getProducts()[order.second]}")
        Scanner(System.`in`).nextLine()
    }
}