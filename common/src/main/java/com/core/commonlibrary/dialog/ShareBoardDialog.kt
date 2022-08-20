package com.core.commonlibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.core.commonlibrary.R
import com.core.commonlibrary.base.BaseCommonDialogFragment
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

/**
 *
 * desc:
 * author: shcx
 * date: 2021/8/12 10:38
 */
class ShareBoardDialog: BaseCommonDialogFragment {

    companion object{
        @JvmStatic
        fun newInstance(): ShareBoardDialog {
            return ShareBoardDialog()
        }
    }

    private var listener: OnShareClickListener? = null

    constructor() : super()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.layout_share_board_dialog, container)
        view.findViewById<TextView>(R.id.tv_weixin_firend_share_umeng).setOnClickListener {
            listener?.OnClick(SendMessageToWX.Req.WXSceneSession)
            dismiss()
        }

        view.findViewById<TextView>(R.id.tv_weixin_circle_share_umeng).setOnClickListener {
            listener?.OnClick(SendMessageToWX.Req.WXSceneTimeline)
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_cancle_share_umeng).setOnClickListener {
            listener?.OnCancle()
            dismiss()
        }
        return view
    }


    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    fun setOnShareClickListener(listener: OnShareClickListener){
        this.listener = listener
    }

    interface OnShareClickListener{
        fun OnClick(type: Int)
        fun OnCancle()
    }


}