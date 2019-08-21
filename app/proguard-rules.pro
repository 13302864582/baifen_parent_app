# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidDev\studio_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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
#-libraryjars    libs/umeng-analytics-v5.2.4.jar
-keepclassmembers class * { *;}
-keep class umeng-analytics-v5.2.4.** { *; }

-dontwarn com.viewpagerindicator.**
-keep class com.viewpagerindicator.** {  *; }

-dontwarn de.tavendo.autobahn.**
-keep class de.tavendo.autobahn.** { *; }

-dontwarn com.edmodo.cropper.**
-keep class com.edmodo.cropper.** { *; }

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class com.google.gson.JsonObject { *; }
-keep class com.google.gson.Gson{*;}

#-libraryjars    ../library_websocketclient/libs/jackson-core-asl-1.9.7.jar
-keepclassmembers class * { *;}
-dontwarn jackson-core-asl-1.9.7.**
-keep class jackson-core-asl-1.9.7.** { *; }

#-libraryjars    ../library_websocketclient/libs/jackson-mapper-asl-1.9.7.jar
-keepclassmembers class * { *;}
-dontwarn jackson-mapper-asl-1.9.7.**
-keep class jackson-mapper-asl-1.9.7.** { *; }

-dontwarn org.codehaus.jackson.**
-keep class org.codehaus.jackson.** { *; }


-dontwarn
-keepattributes InnerClasses,LineNumberTable

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
-keepattributes Signature-dontski
-verbose
-ignorewarnings

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.app.AlarmManager
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.database.sqlite.SQLiteDatabase
-keep public class * extends android.database.sqlite.SQLiteOpenHelper
-keep public class * extends android.os.Handler
-keep public class * extends android.os.Message
-keep public class * extends android.os.Bundle
-keep public class * extends android.os.SystemClock
-keep public class * extends org.json.JSONObject
-keep public class * extends org.json.JSONArray
-keep public class * extends org.json.JSONException
-keep public class * extends com.google.gson.Gson
-keep public class * extends  de.tavendo.autobahn.WebSocketConnectionHandler
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-keep class * extends android.app.Dialog

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

 -keepclasseswithmembernames class * {

    native <methods>;

}

 -keepclasseswithmembers class * {

    public <init>(android.content.Context, android.util.AttributeSet);

}

 -keepclasseswithmembers class * {

    public <init>(android.content.Context, android.util.AttributeSet, int);
}

 -keepclassmembers class * extends android.app.Activity {

   public void *(android.view.View);

}

 -keepclassmembers class * extends android.database.sqlite.SQLiteDatabase {
    public void *(android.database.sqlite.SQLiteDatabase);
 }

 -keepclassmembers class * extends android.database.sqlite.SQLiteOpenHelper{
    public void *(android.database.sqlite.SQLiteOpenHelper);
 }

 -keepclassmembers class * extends org.json.JSONObject{
    public void *(org.json.JSONObject);
 }

 -keepclassmembers class * extends org.json.JSONArray{
    public void *(org.json.JSONArray);
 }

  -keepclassmembers class * extends org.json.JSONException{
    public void *(org.json.JSONException);
 }

 -keepclassmembers enum * {

    public static **[] values();

    public static ** valueOf(java.lang.String);

}

-keep class **.R$* {
    *;
}


-keep public class * {
    public protected *;
}

 -keep class * implements android.os.Parcelable {

    public static final android.os.Parcelable$Creator *;

}

-keep class com.badlogic.** { *; }
-keep class * implements com.badlogic.gdx.utils.Json*
-keep class com.google.** { *; }

##-libraryjars     libs/android-support-v4.jar
-keepclassmembers class * { *;}
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }

##-libraryjars    libs/gson-2.4.jar
-keepclassmembers class * { *;}
-dontwarn com.google.gson.**
-keep class com.google.gson.* { *;}
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
##---------------End: proguard configuration for Gson  ----------

-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#-keep class com.google.gson.stream.** { *; }

-keep public class com.smile.android.R$*{
    public static final int *;
}
-keepclassmembers class ** {
    public void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#-libraryjars   libs/mta-sdk-1.0.0.jar
-keep class com.tencent.stat.* { *;}

#-libraryjars   libs/open_sdk.jar
-keep class com.tencent.* { *;}

#-libraryjars   libs/alipay.jar
-keep class com.alipay.android.app.* { *;}
-keep class com.alipay.android.app.lib.* { *;}


-keep class com.android.volley.* { *;}
-keep class com.android.volley.toolbox.* { *;}
-dontwarn com.android.volley.**

#-libraryjars   libs/libammsdk.jar
-keep class com.tencent.mm.* { *;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}


#-libraryjars   libs/apache-mime4j-0.6.jar
-keep class org.apache.james.mime4j.* { *;}
-dontwarn org.apache.james.mime4j.**

#-libraryjars   libs/httpmime-4.1.1.jar
-keep class org.apache.http.entity.mime.* { *;}
-dontwarn org.apache.http.entity.mime.**

#-libraryjars   libs/nineoldandroids-2.4.0.jar
-keep class com.nineoldandroids.* { *;}
-dontwarn com.nineoldandroids.**

#-libraryjars   libs/core.jar
-keep class com.google.zxing.* { *;}
-dontwarn com.google.zxing.**

#-libraryjars   libs/fastjson-1.1.50.android.jar
-keep class com.alibaba.fastjson.* { *;}
-dontwarn com.alibaba.fastjson.**


#-libraryjars   libs/glide-3.7.0.jar
-keep class com.com.bumptech.glide.* { *;}
-dontwarn com.com.bumptech.glide.**

#-libraryjars   libs/locSDK_6.03.jar
-keep class com.baidu.location.* { *;}
-dontwarn  com.baidu.location.**

#-libraryjars   libs/okhttp-3.2.0.jar
-keep class com.squareup.okhttp3.* { *;}
-dontwarn com.squareup.okhttp3.**


#-libraryjars   libs/okio-1.6.0.jar
-keep class com.squareup.okio.* { *;}
-dontwarn com.squareup.okio.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings



