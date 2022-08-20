package com.runde.commonlibrary.interfaces

import com.runde.commonlibrary.net.BaseResponse

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-07-31 下午 3:25
 * 文件描述：
 */
interface DataListener {
    fun getSuccess(baseResponse: BaseResponse<Any>)
    fun getFailed(baseResponse: BaseResponse<Any>)
}