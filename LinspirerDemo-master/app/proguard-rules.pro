# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#指定外部模糊字典
-obfuscationdictionary ./proguard-rules.txt
#指定class模糊字典
-classobfuscationdictionary ./proguard-rules.txt
#指定package模糊字典
-packageobfuscationdictionary ./proguard-rules.txt

-overloadaggressively


-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
-keep class com.zackratos.ultimatebarx.ultimatebarx.** { *; }
-keep public class * extends androidx.fragment.app.Fragment { *; }
-keep class com.gyf.cactus.entity.* {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
-keep class com.ljlVink.xposed.**{*;}
-keep class com.ljlVink.Activity.BaseActivity{initview();}
#-keepclasseswithmembernames class com.ljlVink.core.core.HackMdm_oldif {*;}
#-keepclasseswithmembernames class com.ljlVink.core.core.Lenovomethod {*;}

