package com.runde.commonlibrary.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.runde.commonlibrary.R
import com.runde.commonlibrary.dialog.CustomPermissionDialog
import com.runde.commonlibrary.global.ApplicationContext

//示例：
/* PermissionXUtils.checkPermission(
      activity,
      "原因",
      object : PermissionXUtils.PermissionListener {
          override fun onGranted() {
              ToastUtil.showDebug("已允许")
          }

          override fun onDenied() {
              ToastUtil.showDebug("未允许权限")
          }
      },
      Manifest.permission.READ_CONTACTS,
      Manifest.permission.CAMERA,
      Manifest.permission.CALL_PHONE,
  )*/
/**
 * desc:
 * 示例：
 * date:2021/8/19
 * by:xiaoguoqing
 */
class PermissionXUtil {

    companion object {

        @JvmStatic
        fun checkPermission(
                activity: FragmentActivity?,
                reasonText: String,
                permissionListener: PermissionListener,
                vararg permissions: String
        ) {
            checkPermissionCustom(
                    activity,
                    reasonText,
                    permissionListener,
                    permissions,
                    ApplicationContext.getString(R.string.ok),
                    ApplicationContext.getString(R.string.cancel),
                    ApplicationContext.getString(R.string.forward_to_settings_text),
                    ApplicationContext.getString(R.string.no_permission_text)
            )
        }

        @JvmStatic
        fun checkPermission(
                fragment: Fragment,
                reasonText: String,
                permissionListener: PermissionListener,
                vararg permissions: String
        ) {
            checkPermission(
                    fragment,
                    reasonText,
                    permissionListener,
                    permissions,
                    ApplicationContext.getString(R.string.ok),
                    ApplicationContext.getString(R.string.cancel),
                    ApplicationContext.getString(R.string.forward_to_settings_text),
                    ApplicationContext.getString(R.string.no_permission_text)
            )
        }

        @JvmStatic
        fun checkPermissionCustom(
                activity: FragmentActivity?,
                reasonText: String,
                permissionListener: PermissionListener,
                permissions: Array<out String>,
                positiveText: String = ApplicationContext.getString(R.string.ok),
                negativeText: String = ApplicationContext.getString(R.string.cancel),
                forwardToSettingsText: String = ApplicationContext.getString(R.string.forward_to_settings_text),
                toastText: String = ApplicationContext.getString(R.string.no_permission_text)
        ) {
            var mShowCount = 0
            PermissionX.init(activity)
                    .permissions(listOf(*permissions))
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason { scope, deniedList ->
                        if (mShowCount == 0) {
                            mShowCount++
                            scope.showRequestReasonDialog(
                                    CustomPermissionDialog(
                                            activity!!,
                                            reasonText,
                                            listOf(*permissions),
                                            positiveText,
                                            negativeText
                                    )
                            )
                        } else {
                            ToastUtil.show(toastText)
                            permissionListener.onDenied()
                        }
                    }
                    .onForwardToSettings { scope, deniedList ->
                        scope.showForwardToSettingsDialog(
                                CustomPermissionDialog(
                                        activity!!,
                                        forwardToSettingsText,
                                        listOf(*permissions),
                                        positiveText,
                                        negativeText
                                )
                        )
                    }
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            permissionListener.onGranted()
                        } else {
                            ToastUtil.show(toastText)
                            permissionListener.onDenied()
                        }
                    }
        }


        @JvmStatic
        fun checkPermission(
                fragment: Fragment,
                reasonText: String,
                permissionListener: PermissionListener,
                permissions: Array<out String>,
                positiveText: String = ApplicationContext.getString(R.string.ok),
                negativeText: String = ApplicationContext.getString(R.string.cancel),
                forwardToSettingsText: String = ApplicationContext.getString(R.string.forward_to_settings_text),
                toastText: String = ApplicationContext.getString(R.string.no_permission_text)
        ) {
            var mShowCount = 0
            PermissionX.init(fragment)
                    .permissions(listOf(*permissions))
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason { scope, deniedList ->
                        if (mShowCount == 0) {
                            mShowCount++
                            scope.showRequestReasonDialog(
                                    CustomPermissionDialog(
                                            fragment.activity!!,
                                            reasonText,
                                            listOf(*permissions),
                                            positiveText,
                                            negativeText
                                    )
                            )
                        } else {
                            ToastUtil.show(toastText)
                            permissionListener.onDenied()
                        }
                    }
                    .onForwardToSettings { scope, deniedList ->
                        scope.showForwardToSettingsDialog(
                                CustomPermissionDialog(
                                        fragment.activity!!,
                                        forwardToSettingsText,
                                        listOf(*permissions),
                                        positiveText,
                                        negativeText
                                )
                        )
                    }
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            permissionListener.onGranted()
                        } else {
                            ToastUtil.show(toastText)
                            permissionListener.onDenied()
                        }
                    }
        }
    }


    interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }
}