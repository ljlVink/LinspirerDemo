# <center>Linspirer Demo</center>

## 简介

LinspirerDemo是一个致力于免刷机(或者在管控ROM里)，在管控系统下添加应用白名单,解开网络,具有一定防止实机检查的能力

## 支持情况

1.API level>=26 (Android 8.0),我们因无低版本安卓而不支持

2.深度支持的设备：联想(双mdm接口)

联想|android8-9|android 10+
---|:--:|---:
| |MiaMdmPolicyManager|CSDKManager

其他平板用领创控制

## 安装方法

### 联想

1.9008 写入官方userdata 然后进领创登录之后断网用密码登录管理员 白名单里包名adb安装即可

2.用二维码扫开了：
```shell
adb shell pm disable-user com.android.launcher3
adb install ***.apk
adb shell dpm set-device-owner 包名/com.huosoft.wisdomclass.linspirerdemo.AR
adb shell pm enable com.android.launcher3
```
### 华为

没研究过 但是曾经可以(管控已经更新策略:禁止外部程序安装 可参见hwmdmapi)

## 启动程序的方法

程序提供了多种启动方法

**注:请第一次保证通过正常方式启动**


### 首次启动的方法

1.首先请注册成语音助手(对于类似原生的系统管用，后续会说明)

2.然后请将它的输入法功能打开(对于能快速切换输入法管用，后续会说明)

3.**激活设备管理器**


### 平时启动程序的方法

1.输入法启动

切换输入法(前提设置成功)

你可选择输入法打开

![](https://github.com/YoungToday/youngtoday.github.io/blob/main/5.png?raw=true)

![](https://github.com/YoungToday/youngtoday.github.io/blob/main/6.png?raw=true)

即可以配置桌面隐藏 显示 打开程序

2.安卓原生语音助手启动

**暂不支持华为机 设置成功后 点击home三次即可进入程序**

3.开机五次音量下启动

前提启动过程序

## 进入程序的方法

**按音量下 按音量下 按音量下**

**按音量下 按音量下 按音量下**

说三遍能不能听明白 打开白屏的小伙伴们

## 验证

请联系你的上级代理获取验证二维码或者设备号云验证

激活全部功能

![](https://raw.githubusercontent.com/YoungToday/youngtoday.github.io/main/1.png)

长按唯一显示的按钮

会弹出二维码扫描 请扫描授权二维码完成程序的全部功能授权

如果二维码扫半天扫不出来 你可以将二维码手动识别下来将那串字符复制到程序下方的框内

点击确定即可

如果授权成功 再次点击音量下键可以激活全部功能

![](https://raw.githubusercontent.com/YoungToday/youngtoday.github.io/main/2.png)

不同机型执行的方法不一样 

![](https://raw.githubusercontent.com/YoungToday/youngtoday.github.io/main/3.png)

![](https://raw.githubusercontent.com/YoungToday/youngtoday.github.io/main/4.jpg)

![](https://raw.githubusercontent.com/YoungToday/youngtoday.github.io/main/7.png)


## 不同权限的能力

### 未激活

程序有可能被领创卸载

### deviceadmin

1.防止程序被mdm卸载

2.配置领创桌面|商店不显示应用 最多能在桌面消失(EMUI10**管控**下)

3.(dpm)的设置不起作用

### deviceowner

全部设备管理功能都可以使用

激活指南

准备好platformtools 按照程序一开头提示的指令输进去就行

**警告 程序在激活后会接管应用的卸载功能 也就是说 你只能在本程序控制其他程序的安装和卸载**

**包名都知道吧 不要无脑到不改包名**

```
adb shell dpm set-device-owner 包名/com.huosoft.wisdomclass.linspirerdemo.AR
```
如果你是华为解控机 还要加上这句
```
adb shell pm grant 包名 android.permission.WRITE_SECURE_SETTINGS
```

一些问题

1.华为**emui10 entc**解控后建议先恢复出厂后 立即到设置打开adb设置 否则打开应用商店或者登陆后会激活deviceowner

2.联想mia需要手动激活

3.联想csdk会移除其他owner程序并全自动激活

## 管控机回领创桌面

点击back即可返回领创桌面 并且将勾选的超级名单应用隐藏

如果没指定桌面 将打开默认桌面选择器 选择默认桌面

如果你要切换桌面的话 请长按返回桌面按钮 将配置默认桌面


## 关于程序隐藏的问题

至少要激活**语音助手或者输入法 才可以执行隐藏**

## 领创网络屏蔽

**从新版本开始 网络屏蔽不会自动开启 请手动开启完成屏蔽**



##  返回第三方||其他桌面


点击返回桌面 

唯一的缺点就是不能隐藏自身




## 解控的华为隐藏设置

根据华为mdm api文档 暂不支持低于emui10的解控机(能做到)

在华为专区里 选择设置隐藏 管控默认全勾选

注意 要adb给予权限 命令在上 不多说



## EMUI10应用安装FAQs

1.静默安装不成功:多按几次就好了

2.安装失败 xx策略禁止:静默安装

## 解除deviceowner|deviceadmin

长按左上角绿色按钮，点击解除设备管理器
