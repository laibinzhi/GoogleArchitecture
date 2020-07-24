package com.lbz.googlearchitecture.data.search

import java.lang.Exception

/**
 * @author: laibinzhi
 * @date: 2020-07-23 19:06
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */


/**
 * 在SearchPagingSource中告诉Ui层显示空白数据布局
 */
class EmptyDataException(message: String) : Exception(message) {
}