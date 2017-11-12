package com.windowsonmac

import java.io.File
import java.math.BigDecimal
import java.util.ArrayList
import java.util.HashMap

/**
 * Written for GreatUniHack 2017 by Adel Khial.
 */

fun main(args: Array<String>) {
    val sb = StringBuilder()
    val orders = getOrders()
    val products = getProducts()
    val usedProducts = arrayListOf<Product>()

    val orderMap = HashMap<Int, ArrayList<Product>>()
    var weight: BigDecimal
    var volume: BigDecimal
    var oldWeight: BigDecimal
    var oldVolume: BigDecimal
    var container = 0
    orders.forEach {
        if(orderMap[it.first] != null)
            orderMap[it.first]?.add(products[it.second]!!)
        else
            orderMap[it.first] = arrayListOf(products[it.second]!!)
    }
    orderMap.forEach {
        var sorted = it.value.sortedByDescending { it.weight/it.vol } // Same thing just sort by density
        // for a weird approx.
        val split = sorted.partition { it.category > 4 }
        val first = split.first
        val second = split.second
        val p1 = first.partition { it.category > 6 }
        val p2 = second.partition { it.category > 2 }
        for(k in 0..5) {
            val p11 = p1.second.partition { it.category == 5 }
            val p22 = p2.first.partition { it.category == 3 }
            sorted = when(k) {
                0 -> p1.first // 7,8
                1 -> p11.first // 5
                2 -> p11.second // 6
                3 -> p22.first // 3
                4 -> p22.second // 4
                5 -> p2.second // 1,2
                else -> throw Exception("WT actual F")
            }
            while(sorted.isNotEmpty()) {
                container++
                weight = BigDecimal.ZERO
                volume = BigDecimal.ZERO
                for(i in 0 until sorted.size) {
                    val p = sorted[i]
                    if(p.weight > 15.0 || p.vol > VOL) {
                        usedProducts.add(p)
                        continue
                    }
                    oldVolume = volume
                    oldWeight = weight
                    volume += BigDecimal.valueOf(p.vol)
                    weight += BigDecimal.valueOf(p.weight)
                    if(weight > BigDecimal.valueOf(15.0) || volume > BigDecimal.valueOf(VOL)) {
                        volume = oldVolume
                        weight = oldWeight
                        continue
                    }
                    sb.append("${it.key},$container,${p.id}\n")
                    usedProducts.add(p)
                }
                val remaining = arrayListOf<Product>()
                var f = false
                for(p in sorted) {
                    for(q in usedProducts) {
                        if(p == q) {
                            usedProducts.remove(p)
                            f = true
                            break
                        }
                    }
                    if(!f) remaining.add(p)
                    f = false
                }
                usedProducts.clear()
                sorted = remaining
            }
        }
    }
    File("$TEAM.rule_7.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
}