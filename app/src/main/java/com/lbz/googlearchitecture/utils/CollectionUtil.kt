package com.lbz.googlearchitecture.utils

/**
 * @author: laibinzhi
 * @date: 2020-07-16 15:34
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
object CollectionUtil {

    /**
     * 判断两个集合是否相同，忽略顺序
     */
    fun <T> compareTwoListIgnoreOrder(oneList: List<T>, twoList: List<T>): Boolean {
        if (oneList.size != twoList.size) {
            return false
        }
        oneList.forEach {
            if (!twoList.contains(it)) {
                return false
            }
        }
        return true
    }

    /**
     * 判断两个集合是否相同，不忽略顺序
     */
    fun <T> compareTwoList(oneList: List<T>, twoList: List<T>): Boolean {
        return oneList.toString() == twoList.toString()
    }

}