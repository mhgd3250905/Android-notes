# 一个基本Android工程的搭建
## 1.网络框架的引用
>**Rxjava+Retrofit**

	/*
     这里是是Rxjava+Retrofit
     */
    compile 'io.reactivex:rxjava:1.1.3'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'com.squareup.retrofit2:retrofit:2.0.2'
    compile 'com.squareup.retrofit2:converter-gson:2.0.2'
    compile 'com.squareup.retrofit2:converter-scalars:2.0.2'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.2'
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'com.squareup.okhttp3:okhttp:3.2.0'
    compile 'com.squareup.okio:okio:1.8.0'

>**gson**

	compile files('libs/gson-2.6.2.jar')

## 2.页面UI风格的设置
>一个标准的Material Design样式

	<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- 溢出菜单图标颜色-->
        <item name="colorControlNormal">@color/colorWhite</item>
        <!-- 左边箭头 -->
        <!--<item name="drawerArrowStyle">@color/colorWhite</item>-->
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
        <!--<item name="android:statusBarColor">@android:color/transparent</item>-->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <!--<item name="android:textColor">@android:color/black</item>-->
        <item name="mainBackground">@android:color/white</item>
        <item name="styleTextColor">@android:color/black</item>
        <item name="styleTextColor55">#77000000</item>
        <item name="styleBackgroundIconColor">@color/colorPrimary</item>
        <item name="styleCardBackground">@android:color/white</item>
    </style>

###-V21

	<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">

        <!-- Customize your theme here. -->
        <!--透明状态栏-->
        <item name="android:windowTranslucentStatus">true</item>
        <!-- 溢出菜单图标颜色-->
        <item name="colorControlNormal">@color/colorWhite</item>
        <!-- 左边箭头 -->
        <!--<item name="drawerArrowStyle">@color/colorWhite</item>-->

        <!--&lt;!&ndash;透明导航栏&ndash;&gt;-->
        <item name="android:navigationBarColor">@color/colorPrimaryDark</item>

        <!--使状态栏，导航栏可绘制-->
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>

        <!--<item name="android:statusBarColor">@android:color/transparent</item>-->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <!--<item name="android:textColor">@android:color/black</item>-->
        <item name="mainBackground">@android:color/white</item>
        <item name="styleTextColor">@android:color/black</item>
        <item name="styleTextColor55">#77000000</item>
        <item name="styleBackgroundIconColor">@color/colorPrimary</item>
        <item name="styleCardBackground">@android:color/white</item>
    </style>
>**其中使用了自定义颜色，所以需要设置一下**

>values/attrs.xml

	<?xml version="1.0" encoding="utf-8"?>
	<resources>
	    <attr name="styleTextColor" format="color|reference"></attr>
	    <attr name="mainBackground" format="color|reference"></attr>
	    <attr name="styleTextColor55" format="color|reference"></attr>
	    <attr name="styleBackgroundIconColor" format="color|reference"></attr>
	    <attr name="styleCardBackground" format="color|reference"></attr>
	</resources>

