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
    var oldWeight: BigDecimal
    var container = 0
    orders.forEach {
        if(orderMap[it.first] != null)
            orderMap[it.first]?.add(products[it.second]!!)
        else
            orderMap[it.first] = arrayListOf(products[it.second]!!)
    }
    orderMap.forEach {
        var sorted = it.value.sortedByDescending { it.weight }
        while(sorted.isNotEmpty()) {
            container++
            weight = BigDecimal.ZERO
            for(i in 0 until sorted.size) {
                val p = sorted[i]
                if(p.weight > 15.0) continue
                oldWeight = weight
                weight += BigDecimal.valueOf(p.weight)
                if(weight > BigDecimal.valueOf(15.0)) {
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
    File("$TEAM.rule_3.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
}

/*val old = order
        order = it.first
        val weightToAdd = BigDecimal.valueOf(products[it.second]!!.weight)
        if(weightToAdd <= BigDecimal.valueOf(15.0)) {
            weight += weightToAdd
            if(order != old || weight > BigDecimal.valueOf(15.0)) {
                weight = weightToAdd
                container++
            }
            println("$order $weight ${products[it.second]!!.weight} ${container + (weight.toInt()/15)}")
            sb.append("$order,$container,${it.second}\n")
        }*/