# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {

    public <init>(android.content.Context);

    public <init>(android.content.Context, android.util.AttributeSet);

    public <init>(android.content.Context, android.util.AttributeSet, int);

    public void set*(...);

}

# 指定代码的压缩级别
-optimizationpasses 5

# 是否使用大小写混合
-dontusemixedcaseclassnames

# 是否混淆第三方jar
-dontskipnonpubliclibraryclasses

# 混淆时是否做预校验
-dontpreverify

# 混淆时是否记录日志
-verbose

-dontshrink
-dontoptimize

# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepclassmembers class cn.com.cimgroup.activity.ZSTWebViewActivity$JSBridge {
  public *;
}

-keepclassmembers class cn.com.cimgroup.activity.HtmlCommonActivity$*JSBridge {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepattributes Signature

#-libraryjars libs/async-http.jar
-libraryjars libs/arm64-v8a/libjpush205.so
-libraryjars libs/armeabi-v7a/libjpush205.so
-libraryjars libs/x86/libjpush205.so
-libraryjars libs/x86_64/libjpush205.so
-libraryjars libs/armeabi/libjpush205.so
-libraryjars libs/armeabi/liblocSDK4d.so
#-libraryjars libs/commons-io-2.0.1.jar
#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/libammsdk.jar
#-libraryjars libs/locSDK_4.2.jar
#-libraryjars libs/payecoplugin.jar
#-libraryjars libs/umpay_sdk.jar
#-libraryjars libs/universal-image-loader-1.9.3.jar
#-libraryjars libs/umeng-analytics-v6.0.0.jar

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService


-keep class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** { *; }

-keep class cn.com.cimgroup.frament.** { *; }
-keep class cn.com.cimgroup.protocol.** { *; }
-keep class cn.com.cimgroup.logic.** { *; }
-keep class cn.com.cimgroup.bean.** { *; }

-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }

-dontwarn android.support.v4.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {

    public static <fields>;

}

#-libraryjars libs/jpush-android-2.0.5.jar
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.google.**
-keep class com.google.** { *; }

#-libraryjars libs/alipaySDK-20151112.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep class com.payeco.android.plugin.** {*;}
-keep class org.apache.http.entity.mime.** {*;}
-dontwarn com.payeco.android.plugin.**
-keepclassmembers class com.payeco.android.plugin {
  public *;
}


-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}

-keep class com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#这句非常重要，主要是滤掉 com.bgb.scan.model包下的所有.class文件不进行混淆编译
-keep class com.bgb.scan.model.** {*;}

-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings



-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}


