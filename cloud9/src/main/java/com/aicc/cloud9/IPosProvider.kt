package com.aicc.cloud9

/**
 * Created by SaiZhang on 2019-06-28 21:41
 * E-Mail Address：1032863320@qq.com
 */
interface IPosProvider {
    fun start(): Int

    fun end(): Int

    fun canItemMove(fromPosition: Int, toPosition: Int): Boolean
}