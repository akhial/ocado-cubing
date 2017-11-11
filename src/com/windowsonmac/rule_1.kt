package com.windowsonmac

import java.io.File

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun main(args: Array<String>) {
    val sb = StringBuilder()
    val orders = getOrders()
    orders.forEach {
        sb.append("${it.first},1,${it.second}\n")
    }
    File("$TEAM.rule_1.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
}