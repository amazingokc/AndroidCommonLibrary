# 保留签名，解决泛型问题
-ignorewarnings
-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                        # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆
-keep public class * extends android.support.v4.**                              # 保持哪些类不被混淆
-keep public class * extends android.support.annotation.**                      # 保持哪些类不被混淆
-keep public class * extends android.support.v7.**                              # 保持哪些类不被混淆

 -keepattributes *Annotation*
 -keepattributes *JavascriptInterface*
 -keep class android.webkit.JavascriptInterface {*;}
#-keepclassmembers class com.seerkey.webview.SeerkeyMainProEnterActivity {
#  public *;
#}
#保留所有的原生Nativie方法
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持android视图构造属性值
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持android视图构造属性值
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#所有View的子类及其子类的get、set方法都不进行混淆
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
#在xml中使用onclick属性的，如kinbuff;--不混淆Activity中参数类型为View的所有方法
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#不混淆Enum类型的指定方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public <init>(org.json.JSONObject);
}
#不混淆Parcelable和它的子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn com.coremedia.iso.**
-keep class com.coremedia.iso.**{*;}
-dontwarn com.googlecode.mp4parser.**
-keep class com.googlecode.mp4parser.**{*;}
-dontwarn org.aspectj.internal.lang.annotation.**
-keep class org.aspectj.internal.lang.annotation.**{*;}
-dontwarn org.aspectj.internal.lang.reflect.**
-keep class org.aspectj.internal.lang.reflect.**{*;}
-dontwarn org.aspectj.lang.**
-keep class org.aspectj.lang.**{*;}
-dontwarn org.aspectj.runtime.**
-keep class org.aspectj.runtime.**{*;}
-dontwarn org.jdesktop.application.**
-keep class org.jdesktop.application.**{*;}

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

#不混淆第三方包
-keep class android.support.v4.**{*;}
-keep class org.apache.http.entity.mime.**{*;}
-keep class android.net.http.**{*;}
-keep class sina.sso.**{*;}
-keep class weibo.sdk.android.**{*;}

#不提示兼容库的错误警告
-dontwarn android.support.**

-keepclassmembers class * {
	public <init>(android.content.Context);
}
#使用注解需要添加
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

#不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class com.tencent.mm.opensdk.openapi.WXMediaMessage { *;}
-keep class com.tencent.mm.opensdk.openapi.** implements com.tencent.mm.opensdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.mm.**{*;}
-keep class com.bocommlife.healthywalk.wxapi.**{*;}
-keep class net.sourceforge.simcpux.**{*;}

#------------------------------Glide----------------------start
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#------------------------------Glide----------------------end

#------------------------Arouter------------------start
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# If single-type injection is used, that is, no interface is defined to implement IProvider,
# the following rules need to be added to protect the implementation
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
#------------------------Arouter------------------end

#-----------------retrofit2------------------start
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod
# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
#-----------------retrofit2------------------end

# -----------------okhttp3---------------start
#保持Logger包
-keep class com.orhanobut.logger.** { *; }
#忽略logger包报错的信息
#忽略logging-interceptor包报错的信息
-dontwarn okhttp3.logging.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.**{*;}
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
# -----------------okhttp3---------------start

#保持rxjava2包
-keep class io.reactivex.** { *; }
#忽略rxjava2包报错的信息
-dontwarn io.reactivex.**

#保持rxandroid包
-keep class io.reactivex.android.** { *; }
#忽略rxandroid包报错的信息
-dontwarn io.reactivex.android.**

#保持gson包
-keep class com.google.gson.** { *; }
#忽略gson包报错的信息
-dontwarn com.google.gson.**

#友盟统计防止混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

#alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

#不混淆导入的第三方jar包
-keep class com.alipay.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-keep class org.json.alipay.** {*;}
-keep class com.baidu.** {*;}
-keep class com.baidu.location.** {*;}
-keep class vi.com.gdi.bgl.android.java.** {*;}
-keep class com.baidu.navisdk.adapter.** {*;}
-keep class uk.co.chrisjenx.calligraphy.** {*;}
-keep class lecho.lib.hellocharts.** {*;}
-keep class org.apache.http.** {*;}
-keep class org.apache.http.entity.mime.** {*;}
-keep class com.tencent.** {*;}
-keep class com.nineoldandroids.** {*;}
-keep class com.squareup.picasso.** {*;}
-keep class com.umeng.analytics.** {*;}
-keep class com.nostra13.universalimageloader.** {*;}
-keep class com.unionpay.** {*;}
-keep class com.UCMobile.PayPlugin.** {*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** {*;}
-keep class com.google.zxing.** {*;}
-keep class com.liulishuo.filedownloader.** {*;}

#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，
#另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}

#umeng 分享 SDK  start
-dontusemixedcaseclassnames
    -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn com.tencent.weibo.sdk.**
    -dontwarn com.facebook.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**
    -keep enum com.facebook.**
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes *Annotation*
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.facebook.**
    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keep public class com.umeng.socialize.* {*;}

    -keep class com.facebook.**
    -keep class com.facebook.** { *; }
    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class im.yixin.sdk.api.YXMessage {*;}
    -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
    -keep class com.tencent.mm.sdk.** {
     *;
    }
    -keep class com.tencent.wxop.** {
       *;
    }
    -keep class com.tencent.mm.opensdk.** {
   *;
    }
    -dontwarn twitter4j.**
    -keep class twitter4j.** { *; }

    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }
    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keepattributes Signature
#umeng 分享 SDK  end

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留注解不被混淆
-keep public @interface * {
    ** default (*);
}
-keepclassmembers class * {
    void *(**On*Event);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
@org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
<init>(java.lang.Throwable);
}

-keep class com.baidu.bottom.** { *; }
-keep class com.baidu.kirin.** { *; }
-keep class com.baidu.mobstat.** { *; }
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------

#模块代码
-keep class com.runde.commonlibrary.bean.**{*;}
-keep class com.runde.commonlibrary.communication.** {*;}
-keep class com.runde.commonlibrary.customview.** {*;}
-keep class com.runde.commonlibrary.net.** {*;}

#------------GreenDAO -------------- start
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }
# If you DO use SQLCipher:
-keep class org.greenrobot.greendao.database.SqlCipherEncryptedHelper { *; }
# If you do NOT use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do NOT use RxJava:
#-dontwarn rx.**
#------------GreenDAO -------------- end

#-------------阿里OSS-----------------#
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**
#-------------阿里OSS-----------------#