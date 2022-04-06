
# Linspirer Demo (huoVink_MDM_patch_for_Lenovo)

> 比领创更懂你的mdm
> 
> 革命尚未结束 加油勇士
> <p align="right">——ljlVink</p>




针对各种教育平台(联想和华为)领创平板的研究

目前支持接口：联想csdk 联想mia 

目前支持系统:API>=27(Android 8.1)

测试过的系统:联想 android8.1 android9 android10

**老系统(安卓8(包括)以下)将不会支持 因为我手里并没有那些设备和精力向下兼容**

华为:仅在emui10测试过


qq群:836337977

如果你想强制在正常csdk接口中启动mdm的话可参照网站填写信息

[Project Management Dashboard (lenovocust.cn)](https://onlinecn.lenovocust.cn/lenovo/OnlineCustomization/Screens/DashboardCN.html)


加密混淆规则

```
com.ljlVink.**
```



### 贡献
**控制流混淆**

[BlackObfuscator](https://github.com/CodingGay/BlackObfuscator-ASPlugin)

**加固程序**

[nmmp](https://github.com/maoabc/nmmp)


**悬浮窗**

[princekin-f/EasyFloat:  EasyFloat：浮窗从未如此简单](https://github.com/princekin-f/EasyFloat)

**bugly**

[bugly](https://bugly.qq.com/)

**zxing-lite**

[zxing-lite](https://github.com/jenly1314/ZXingLite/)

**easy-protector**

[easy-protector](https://github.com/lamster2018/EasyProtector)

**stringfog**

[stringfog](https://github.com/MegatronKing/StringFog)

#### 主要调用

[/app/libs/framework_new.jar ](https://gitee.com/ljlvink/huovink_-mdm_catch_for_-lenovo/blob/master/LinspirerDemo-master/app/libs/framework_new.jar)

[DevicePolicyManager](https://developer.android.com/reference/android/app/admin/DevicePolicyManager)

