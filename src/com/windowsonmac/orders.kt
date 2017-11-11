package com.windowsonmac

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.ArrayList

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun getOrders(): ArrayList<Pair<Int, Int>> {
    val orders = ArrayList<Pair<Int, Int>>()
    val lines = Files.lines(Paths.get("order.csv"))
    for(line in lines) {
        val strings = line.split(",")
        if(strings[0] == "ORDER_ID") continue
        orders.add(Pair(strings[0].toInt(), strings[1].toInt()))
    }
    return orders
}