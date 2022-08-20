package com.runde.commonlibrary.dialog

import android.Manifest
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.permissionx.guolindev.dialog.RationaleDialog
import com.permissionx.guolindev.dialog.allSpecialPermissions
import com.runde.commonlibrary.R
import com.runde.commonlibrary.server.ServiceFactory
import kotlinx.android.synthetic.main.permissions_item_view.view.*
import java.util.*

/**
 * +----------------------------------------------------------------------
 * | com.runde
 * +----------------------------------------------------------------------
 * | 功能描述:
 * +----------------------------------------------------------------------
 * | 时　　间: 2021/10/8.
 * +----------------------------------------------------------------------
 * | 代码创建: chenmingdu
 * +----------------------------------------------------------------------
 */
class CustomPermissionDialog(
    context: Context,
    private val message: String,
    private val permissions: List<String>,
    private var positiveText: String,
    private var negativeText: String,
) :
    RationaleDialog(context, R.style.test_tip_dialog) {

    private val permissionMap = mapOf(
        Manifest.permission.READ_CALENDAR to Manifest.permission_group.CALENDAR,
        Manifest.permission.WRITE_CALENDAR to Manifest.permission_group.CALENDAR,
        Manifest.permission.READ_CALL_LOG to Manifest.permission_group.CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG to Manifest.permission_group.CALL_LOG,
        "android.permission.PROCESS_OUTGOING_CALLS" to Manifest.permission_group.CALL_LOG,
        Manifest.permission.CAMERA to Manifest.permission_group.CAMERA,
        Manifest.permission.READ_CONTACTS to Manifest.permission_group.CONTACTS,
        Manifest.permission.WRITE_CONTACTS to Manifest.permission_group.CONTACTS,
        Manifest.permission.GET_ACCOUNTS to Manifest.permission_group.CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION to Manifest.permission_group.LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION to Manifest.permission_group.LOCATION,
        "android.permission.ACCESS_BACKGROUND_LOCATION" to Manifest.permission_group.LOCATION,
        Manifest.permission.RECORD_AUDIO to Manifest.permission_group.MICROPHONE,
        Manifest.permission.READ_PHONE_STATE to Manifest.permission_group.PHONE,
        Manifest.permission.READ_PHONE_NUMBERS to Manifest.permission_group.PHONE,
        Manifest.permission.CALL_PHONE to Manifest.permission_group.PHONE,
        Manifest.permission.ANSWER_PHONE_CALLS to Manifest.permission_group.PHONE,
        Manifest.permission.ADD_VOICEMAIL to Manifest.permission_group.PHONE,
        Manifest.permission.USE_SIP to Manifest.permission_group.PHONE,
        Manifest.permission.ACCEPT_HANDOVER to Manifest.permission_group.PHONE,
        Manifest.permission.BODY_SENSORS to Manifest.permission_group.SENSORS,
        "android.permission.ACTIVITY_RECOGNITION" to "android.permission-group.ACTIVITY_RECOGNITION",
        Manifest.permission.SEND_SMS to Manifest.permission_group.SMS,
        Manifest.permission.RECEIVE_SMS to Manifest.permission_group.SMS,
        Manifest.permission.READ_SMS to Manifest.permission_group.SMS,
        Manifest.permission.RECEIVE_WAP_PUSH to Manifest.permission_group.SMS,
        Manifest.permission.RECEIVE_MMS to Manifest.permission_group.SMS,
        Manifest.permission.READ_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
        "android.permission.ACCESS_MEDIA_LOCATION" to Manifest.permission_group.STORAGE
    )

    private val groupSet = HashSet<String>()
    private var permissionsLayout: LinearLayout? = null
    private var mSubTitle: TextView? = null
    private var mTitle: TextView? = null
    private var mSure: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_permission)
        permissionsLayout = findViewById(R.id.permissionsLayout);
        mSubTitle = findViewById(R.id.tv_subTitle)
        mSubTitle?.text = message
        mTitle = findViewById(R.id.tv_tip_title_commodialog)
        mTitle?.setTextColor(ServiceFactory.getInstance().commonService.colorPrimary)
        mSure = findViewById(R.id.tv_sure_stolentip)
        mSure?.setTextColor(ServiceFactory.getInstance().commonService.colorPrimary)
        addView()
    }


    override fun getPositiveButton(): View {
        val view = findViewById<TextView>(R.id.tv_sure_stolentip)
        view.text = positiveText
        return view
    }

    override fun getNegativeButton(): View? {
        val view = findViewById<TextView>(R.id.tv_cancle_stolentip)
        view.text = negativeText
        return view
    }

    override fun getPermissionsToRequest(): List<String> {
        return permissions
    }


    private fun addView() {
        for (permission in permissions) {
            val permissionGroup = permissionMap[permission]
            if ((permission in allSpecialPermissions && !groupSet.contains(permission))
                || (permissionGroup != null && !groupSet.contains(permissionGroup))
            ) {
                val view: View = LayoutInflater.from(context).inflate(
                    R.layout.permissions_item_view,
                    null, false
                )
                when (permission) {
                    Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                        view.bodyItem.text =
                            context.getString(R.string.permissionx_system_alert_window)
                        view.permissions_icon.setImageResource(R.drawable.permissionx_ic_alert)
                    }
                    Manifest.permission.WRITE_SETTINGS -> {
                        view.bodyItem.text =
                            context.getString(R.string.permissionx_write_settings)
                        view.permissions_icon.setImageResource(R.drawable.permissionx_ic_setting)
                    }
                    "android.permission.MANAGE_EXTERNAL_STORAGE" -> {
                        view.bodyItem.text =
                            context.getString(R.string.permissionx_manage_external_storage)
                        view.permissions_icon.setImageResource(R.drawable.permissionx_ic_storage)
                    }

                    //内存
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        view.bodyItem.text =
                            "储存空间"
                        view.permissions_icon.setImageResource(R.drawable.permission_storage)
                    }

                    //地址
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION" -> {
                        view.bodyItem.text =
                            "位置信息"
                        view.permissions_icon.setImageResource(R.drawable.permission_location)
                    }

                    //录音
                    Manifest.permission.RECORD_AUDIO -> {
                        view.bodyItem.text =
                            "访问语音"
                        view.permissions_icon.setImageResource(R.drawable.permission_audio)
                    }


                    //电话
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    Manifest.permission.ADD_VOICEMAIL,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.ACCEPT_HANDOVER -> {
                        view.bodyItem.text =
                            "访问电话"
                        view.permissions_icon.setImageResource(R.drawable.permission_phone)
                    }

                    //短信
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS -> {
                        view.bodyItem.text =
                            "设备信息"
                        view.permissions_icon.setImageResource(R.drawable.permission_sms)
                    }

                    //相机
                    Manifest.permission.CAMERA -> {
                        view.bodyItem.text =
                            "访问相机"
                        view.permissions_icon.setImageResource(R.drawable.permission_camera)
                    }


                    else -> {

                        view.bodyItem.text =
                            context.getString(
                                context.packageManager.getPermissionGroupInfo(
                                    permissionGroup!!,
                                    0
                                ).labelRes
                            )

                        view.permissions_icon.setImageResource(
                            context.packageManager.getPermissionGroupInfo(
                                permissionGroup,
                                0
                            ).icon
                        )


                        view.permissions_icon.setColorFilter(
                            ServiceFactory.getInstance().commonService.colorPrimary,
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }

                }


                permissionsLayout?.addView(view)
                groupSet.add(permissionGroup!!)
            }

        }
    }
}