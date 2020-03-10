# Utils [![](https://www.jitpack.io/v/FengChenSunshine/BaseUi.svg)](https://www.jitpack.io/#FengChenSunshine/BaseUi)

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
    }

### Step 2. Add the dependency
    dependencies {
	    implementation 'com.github.FengChenSunshine:BaseUi:1.0.2'
	}

## 历史版本

### 1.0.2
1.修复设置浮动按钮后滑动ViewPager时浮动按钮之后的按钮选择异常的问题。
2.升级版本库到AndroidX.
3.修复跳转时设置orientation后导致继承自RootFragment的视图initView调用多次问题.

### 1.0.1
1.BottomNavigationLayout支持设置浮动按钮，使用该功能可以实现中间按钮超出导航栏显示。

### 1.0.0
1.发布。
