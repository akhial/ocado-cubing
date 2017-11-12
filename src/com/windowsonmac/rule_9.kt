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
    var oldWeight: BigDecimal
    var container = 0
    orders.forEach {
        if(orderMap[it.first] != null)
            orderMap[it.first]?.add(products[it.second]!!)
        else
            orderMap[it.first] = arrayListOf(products[it.second]!!)
    }
    orderMap.forEach {
        var sorted = it.value.sortedByDescending { it.h*it.h*it.weight*it.vol } // Same thing just sort by density
        // for a weird approx.
        val split = sorted.partition { it.category > 4 }
        val first = split.first
        val second = split.second
        val p1 = first.partition { it.category > 6 }
        val p2 = second.partition { it.category > 2 }
        val p11 = p1.second.partition { it.category == 5 }
        val p22 = p2.first.partition { it.category == 3 }
        var p22s = p22.second

        var vol: BigDecimal
        for(k in 0..5) {
            sorted = when(k) {
                0 -> p1.first // 7,8
                1 -> p22s // 4
                2 -> p11.first // 5
                3 -> p11.second // 6
                4 -> p22.first // 3
                5 -> p2.second // 1,2
                else -> throw Exception()
            }
            while(sorted.isNotEmpty()) {
                vol = BigDecimal.ZERO
                var mX = BigDecimal.ZERO
                var mY = BigDecimal.ZERO
                var mZ = BigDecimal.ZERO
                var oY = BigDecimal.ZERO
                var oZ = BigDecimal.ZERO
                container++
                weight = BigDecimal.ZERO
                if(sorted.none { it.category == 8 } && k == 0) {
                    sorted += p22s
                    p22s = arrayListOf()
                }
                for(i in 0 until sorted.size) {
                    val p = sorted[i]
                    if(p.weight > 15.0 || p.vol > VOL) {
                        usedProducts.add(p)
                        continue
                    }
                    oldWeight = weight
                    weight += BigDecimal.valueOf(p.weight)
                    if(weight > BigDecimal.valueOf(15.0)) {
                        weight = oldWeight
                        continue
                    }
                    val w = BigDecimal.valueOf(p.w)
                    val l = BigDecimal.valueOf(p.l)
                    val h = BigDecimal.valueOf(p.h)
                    val ts = BigDecimal.valueOf(36.0)
                    val ff = BigDecimal.valueOf(55.0)
                    val tt = BigDecimal.valueOf(33.0)
                    if(w > ts || l > ff || h > tt) {
                        weight = oldWeight
                        usedProducts.add(p)
                        continue
                    }
                    val x = mX + w
                    var y = mY.max(l + oY)
                    var z = mZ.max(h + oZ)
                    if(x > ts || y > ff || z > tt) {
                        y = mY + l
                        if(y > ff || z > tt) {
                            z = mZ + h
                            if(z > tt) {
                                weight = oldWeight
                                continue
                            } else {
                                mX = w
                                mY = l
                                oZ = mZ
                                mZ = z
                            }
                        } else {
                            mX = w
                            oY = mY
                            mY = y
                            oZ = mZ
                            mZ = z
                        }
                    } else {
                        mX = x
                        mY = y
                        mZ = z
                    }
                    vol += BigDecimal.valueOf(p.vol)
                    if(vol > BigDecimal.valueOf(VOL)) throw Exception()
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
    File("$TEAM.rule_9.csv").printWriter().use {
        it.println("\"ORDER_ID\",\"CONTAINER_ID\",\"SKU_ID\"")
        it.println(sb)
    }
    println("# of totes: $container")
}