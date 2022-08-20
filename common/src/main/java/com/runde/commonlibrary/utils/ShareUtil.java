package com.runde.commonlibrary.utils;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.fragment.app.FragmentActivity;

import com.runde.commonlibrary.R;
import com.runde.commonlibrary.bean.ShareInfoBean;
import com.runde.commonlibrary.global.ApplicationContext;
import com.runde.commonlibrary.global.BaseUrl;
import com.runde.commonlibrary.wechat.WechatConstants;
import com.runde.commonlibrary.wechat.event.WXShareCancelEvent;
import com.runde.commonlibrary.wechat.event.WXShareEvent;
import com.runde.commonlibrary.dialog.ShareBoardDialog;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * desc:
 * author: shcx
 * date: 2021/8/12 10:04
 */
public class ShareUtil {

    public static String SHARE_SUCCESS_CALL_TO_WEB = "SHARE_SUCCESS_CALL_TO_WEB";

    /**
     * 分享微信  朋友圈
     *
     * @param activity
     * @param strShareUrl
     * @param strShareTitle
     * @param strShareSubtitle
     * @param strShareImg
     * @param transactionId
     */
    public static void shareWebWithWeChat(FragmentActivity activity, String strShareUrl, String strShareTitle,
                                          String strShareSubtitle, String strShareImg, String transactionId) {
        ShareBoardDialog myShareBoard = ShareBoardDialog.newInstance();
        setBoardCallback(myShareBoard, activity, strShareUrl, strShareTitle, strShareSubtitle, strShareImg, 0, transactionId);
        myShareBoard.show(activity.getSupportFragmentManager(), "");
    }

    /**
     * 分享微信  朋友圈
     *
     * @param activity
     * @param strShareUrl
     * @param strShareTitle
     * @param strShareSubtitle
     * @param shareImgId
     * @param transactionId
     */
    public static void shareWebWithWeChat(FragmentActivity activity, String strShareUrl, String strShareTitle,
                                          String strShareSubtitle, int shareImgId, String transactionId) {
        ShareBoardDialog myShareBoard = ShareBoardDialog.newInstance();
        setBoardCallback(myShareBoard, activity, strShareUrl, strShareTitle
                , strShareSubtitle, "", shareImgId, transactionId);
        myShareBoard.show(activity.getSupportFragmentManager(), "");
    }


    private static void setBoardCallback(ShareBoardDialog myShareBoard, final FragmentActivity activity, final String strShareUrl, final String strShareTitle,
                                         final String strShareSubtitle, final String strShareImg, final int drawableImg, final String transactionId) {
        myShareBoard.setOnShareClickListener(new ShareBoardDialog.OnShareClickListener() {
            @Override
            public void OnCancle() {
                WXShareCancelEvent.post(transactionId);
            }

            @Override
            public void OnClick(int type) {
                share(activity, strShareUrl, strShareTitle, strShareSubtitle, strShareImg, drawableImg, transactionId, type);
            }
        });
    }

    public static void share(final Activity activity, String strShareUrl, String strShareTitle,
                              String strShareSubtitle, final String strShareImg, int drawableImg,
                              final String transactionId, final int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = strShareUrl;
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = strShareTitle;
        msg.description = strShareSubtitle;
        //缩略图
        if (drawableImg == 0 && TextUtils.isEmpty(strShareImg)) {
            Bitmap thumbBmp = BitmapFactory.decodeResource(ApplicationContext.getContext().getResources(),
                    R.drawable.no_banner);
            sendReq(activity, msg, transactionId, scene, thumbBmp);
        } else if (drawableImg != 0) {
            Bitmap thumbBmp = BitmapFactory.decodeResource(ApplicationContext.getContext().getResources(),
                    drawableImg);
            sendReq(activity, msg, transactionId, scene, thumbBmp);
        } else if (!TextUtils.isEmpty(strShareImg)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap thumbBmp = ImageLoaderUtil.GetLocalOrNetBitmap(BaseUrl.getPicUrl(strShareImg));
                    if (thumbBmp != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendReq(activity, msg, transactionId, scene, thumbBmp);
                            }
                        });
                    } else {
                        Bitmap thumbBmp1 = BitmapFactory.decodeResource(ApplicationContext.getContext().getResources(),
                                R.drawable.no_banner);
                        sendReq(activity, msg, transactionId, scene, thumbBmp1);
                    }
                }
            }).start();
        }
    }

    public static void shareOnlyWeChatCIRCLE(Activity activity, String strShareUrl, String strShareTitle,
                                             String strShareSubtitle, String strShareImg,
                                             String transactionId) {
        share(activity, strShareUrl, strShareTitle, strShareSubtitle, strShareImg, 0,
                transactionId, WXSceneTimeline);
    }

    public static void shareOnlyWeChat(Activity activity, String strShareUrl, String strShareTitle,
                                       String strShareSubtitle, String strShareImg,
                                       String transactionId) {
        share(activity, strShareUrl, strShareTitle, strShareSubtitle, strShareImg, 0,
                transactionId, WXSceneSession);
    }


    private static void sendReq(Activity activity, WXMediaMessage msg, final String transactionId,
                                int scene, Bitmap thumbBmp) {
        thumbBmp = BitmapUtils.imageZoom(thumbBmp, 32);
        msg.setThumbImage(thumbBmp);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transactionId;//事务id
        req.message = msg;
        req.scene = scene;//默认值
        req.userOpenId = "getOpenId";
        IWXAPI api = WXAPIFactory.createWXAPI(activity, WechatConstants.getWXAppId(), false);
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("请安装微信应用");
            return;
        }
        //当前微信版本是否支持分享到朋友圈
        if (scene == WXSceneTimeline && api.getWXAppSupportAPI() >= Build.TIMELINE_SUPPORTED_SDK_INT) {
            api.sendReq(req);
        } else if (scene == WXSceneTimeline && api.getWXAppSupportAPI() < Build.TIMELINE_SUPPORTED_SDK_INT) {
            ToastUtil.show("当前微信版本不支持朋友圈分享，请升级至4.2以上版本");
        } else {
            api.sendReq(req);
        }

        //由于微信分享回调存在的问题（分享之后如果不点击回到该应用，不会走回调，并且无法判断是否分享成功，所以在这里执行分享回调）
        ApplicationContext.getUiHandler().post(new Runnable() {  //这里不需要做延迟操作
            @Override
            public void run() {
                WXShareEvent.post(transactionId);
            }
        });

    }

    public static void shareWechat(String result, FragmentActivity activity) {
        try {
            ShareInfoBean shareInfoBean = GsonUtil.fromJson(result, ShareInfoBean.class);
            shareInfoBean.setTransactionId(ShareUtil.SHARE_SUCCESS_CALL_TO_WEB);
            if (shareInfoBean.getSharePlaformOption() != null) {
                shareToWechat(shareInfoBean, activity);
            } else {
                shareToWechat(shareInfoBean, false, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showDebug(e.getMessage());
        }
    }

    public static void shareToWechat(ShareInfoBean shareInfoBean, FragmentActivity activity) {
        if (shareInfoBean != null && !TextUtils.isEmpty(shareInfoBean.getUrl()) //url不可空
                && Patterns.WEB_URL.matcher(shareInfoBean.getUrl()).matches()   //url是否有效
        ) {
            if (shareInfoBean.getSharePlaformOption().equals("SharePlaformOptionsWechatTimeLine")) {//仅朋友圈
                shareToWechat(shareInfoBean, true, activity);
            } else if (shareInfoBean.getSharePlaformOption().equals("SharePlaformOptionsWechatSession")) {//仅微信
                ShareUtil.shareOnlyWeChat(activity, shareInfoBean.getUrl(), shareInfoBean.getTitle()
                        , shareInfoBean.getDesc(), shareInfoBean.getImgUrl()
                        , shareInfoBean.getTransactionId());
            } else if (shareInfoBean.getSharePlaformOption().equals("SharePlaformOptionsWechatAll")) {//微信+朋友圈
                shareToWechat(shareInfoBean, false, activity);
            }//页面是否加载完成
        }
    }

    public static void shareToWechat(ShareInfoBean shareInfoBean, boolean isOnlyWechatCircle, FragmentActivity activity) {
        if (shareInfoBean != null && !TextUtils.isEmpty(shareInfoBean.getUrl()) //url不可空
                && Patterns.WEB_URL.matcher(shareInfoBean.getUrl()).matches()   //url是否有效
        ) {                                              //页面是否加载完成
            if (isOnlyWechatCircle) {
                ShareUtil.shareOnlyWeChatCIRCLE(activity, shareInfoBean.getUrl(), shareInfoBean.getTitle()
                        , shareInfoBean.getDesc(), shareInfoBean.getImgUrl()
                        , shareInfoBean.getTransactionId());
            } else {
                ShareUtil.shareWebWithWeChat(activity, shareInfoBean.getUrl(), shareInfoBean.getTitle()
                        , shareInfoBean.getDesc(), shareInfoBean.getImgUrl()
                        , shareInfoBean.getTransactionId());
            }
        }
    }

}
