# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class list to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file list.
#-renamesourcefileattribute SourceFile

#for stripe library
-keep class com.stripe.** { *; }
#for Firebase Database or to stop desofcuted class
-keepclasseswithmembers class com.veeritsolutions.uhelpme.**{ *; }
#for Glide library
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#for picasso library
-dontwarn com.squareup.okhttp.**
#for gif loading library
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int, java.lang.String);}