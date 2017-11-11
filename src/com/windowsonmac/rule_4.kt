package com.windowsonmac

import java.io.File
import java.math.BigDecimal
import java.util.*

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
        while(sorted.isNotEmpty()) {
            container++
            weight = BigDecimal.ZERO
            volume = BigDecimal.ZERO
            for(i in 0 until sorted.size) {
                val p = sorted[i]
                if(p.weight > 15.0 || p.vol > 65340.0) continue
                oldVolume = volume
                oldWeight = weight
                volume += BigDecimal.valueOf(p.vol)
                weight += BigDecimal.valueOf(p.weight)
                if(weight > BigDecimal.valueOf(15.0) || volume > BigDecimal.valueOf(65340)) {
                    volume = oldVolume
                    weight = oldWeight
                    continue
                }
                print("${it.key} $volume $weight -> $container")
                Scanner(System.`in`).nextLine()
                //sb.append("${it.key},$container,${p.id}\n")
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
    File("$TEAM.rule_4.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
}