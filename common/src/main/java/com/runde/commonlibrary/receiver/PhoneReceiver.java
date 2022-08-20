package com.runde.commonlibrary.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-04-12 上午 11:29
 * 文件描述：
 */

public class PhoneReceiver extends BroadcastReceiver {
    DoTelePhonyWorkListener telePhonyWorkListener;

    public void setTelePhonyWorkListener(DoTelePhonyWorkListener listener) {
        telePhonyWorkListener = listener;
    }

    public void unRegister() {
        telePhonyWorkListener = null;
    }

    public interface DoTelePhonyWorkListener {
        void callStateIdle();

        void callStateRinging();

        void callStateOffHook();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
            // 如果是去电（拨出）
        } else {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            // 设置一个监听器
            if (tm != null) {
                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // state 当前状态 incomingNumber,貌似没有去电的API
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话响铃
                    if (telePhonyWorkListener != null) {
                        telePhonyWorkListener.callStateRinging();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:////电话挂断
                    if (telePhonyWorkListener != null) {
                        telePhonyWorkListener.callStateIdle();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://电话接通
                    if (telePhonyWorkListener != null) {
                        telePhonyWorkListener.callStateOffHook();
                    }
                    break;
            }
        }

    };

}
