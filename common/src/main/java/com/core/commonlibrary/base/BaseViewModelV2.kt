package com.core.commonlibrary.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.commonlibrary.net.BaseResponse
import com.core.commonlibrary.interfaces.DataListener

/**
 *
 */
abstract class BaseViewModelV2 : ViewModel() {

    val successLiveData = MutableLiveData<BaseResponse<Any>>()
    val errorLiveData = MutableLiveData<BaseResponse<Any>>()

    protected fun subscribe(baseModel: BaseModel) {
        baseModel.setDataListener(object : DataListener {
            override fun getFailed(baseResponse: BaseResponse<Any>) {
                onFail(baseResponse)
            }

            override fun getSuccess(baseResponse: BaseResponse<Any>) {
                onSuccess(baseResponse)
            }
        })
    }


    protected open fun onSuccess(baseResponse: BaseResponse<Any>, isSetValue: Boolean = true) {
        if (isSetValue) {
            successLiveData.value = baseResponse
        } else {
            successLiveData.postValue(baseResponse)
        }
    }

    protected open fun onFail(baseResponse: BaseResponse<Any>, isSetValue: Boolean = true) {
        if (isSetValue) {
            errorLiveData.value = baseResponse
        } else {
            errorLiveData.postValue(baseResponse)
        }
    }


}