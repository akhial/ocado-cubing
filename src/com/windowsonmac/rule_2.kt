package com.windowsonmac

import java.io.File

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun main(args: Array<String>) {
    val sb = StringBuilder()
    val orders = getOrders()
    orders.forEach {
        sb.append("${it.first},${it.first},${it.second}\n")
    }
    File("$TEAM.rule_2.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
}