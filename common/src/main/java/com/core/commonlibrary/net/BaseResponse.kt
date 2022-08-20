package com.core.commonlibrary.net

/**
 * 统一返回数据
 */
open class BaseResponse<T>(
    var code: Int = 0,
    var msg: String? = null,
    var data: T? = null,
    var repType: String? = null
)