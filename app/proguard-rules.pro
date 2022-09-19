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
#---------------------------------基本指令区----------------------------------

# 指定代码的压缩级别 0 - 7(指定代码进行迭代优化的次数，在Android里面默认是5，这条指令也只有在可以优化时起作用。)
-optimizationpasses 5
# 混淆时不会产生形形色色的类名(混淆时不使用大小写混合类名)
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库类(不跳过library中的非public的类)
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共的的库类的成员
-dontskipnonpubliclibraryclassmembers
#不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify
# 混淆时记录日志(打印混淆的详细信息)
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
#指定混淆后映射关系输出文件
-printmapping proguardMapping.txt
#指定映射关系文件，可以配置上次输出的映射文件，这样就能保持映射关系不变
-applymapping a.txt
#指定字段、方法名的混淆字典，默认情况下使用abc等字母组合，比如根据自己的喜好指定中文、特殊字符进行混淆命名
-obfuscationdictionary test.txt
#指定类名混淆字典
-classobfuscationdictionary test.txt
#指定包名混淆字典
-packageobfuscationdictionary test.txt
# 把混淆类中的方法名也混淆了
-useuniqueclassmembernames

# apk 包内所有 class 的内部结构
-dump dump.txt

# 未混淆的类和成员
-printseeds seeds_txt

# 列出从apk中删除的代码
-printusage unused.txt

#输出整个混淆配置文件
-printconfiguration printconfiguration.txt
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#关闭压缩
-dontshrink
#关闭代码优化
-dontoptimize


#-keep class android.support.** {*;}

-keep class androidx.** {*;}
-keep class android.** {*;}
-keep class org.** {*;}
-keep class com.google.** {*;}
-keep class com.facebook.** {*;}
-keep class com.squareup.** {*;}
-keep class com.lhl.InitProvider
