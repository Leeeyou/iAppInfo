# AppInfo中的技术说明

## 1 关于MD风格的几点说明

状态栏透明+Toolbar滚入滚出+TabLayout悬停效果     
![整体效果](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/mainpage.gif)

### 1.1 主体框架介绍
下面的截图就是主页面的布局，基本框架代码如下
```xml
<android.support.design.widget.CoordinatorLayout
        <android.support.design.widget.AppBarLayout
                <android.support.v7.widget.Toolbar        
                <android.support.design.widget.TabLayout
        <android.support.v4.view.ViewPager
```
![主体框架](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/CoordinatorLayout.png)
下面一一说明下各个控件的主要功能：   
CoordinatorLayout：翻译为协调布局，官方介绍第一句就是：CoordinatorLayout is a super-powered FrameLayout.  [官网指导说明](https://developer.android.google.cn/reference/android/support/design/widget/CoordinatorLayout.html) ，它最强大的地方是指定behavior来协调子view的交互，完成动画效果。

AppBarLayout：官方介绍第一句 AppBarLayout is a vertical LinearLayout which implements many of the features of material designs app bar concept, namely scrolling gestures. 就是说它是个垂直的线性布局，为了配合MD风格，实现了滚动手势监听。[官网指导说明](https://developer.android.google.cn/reference/android/support/design/widget/AppBarLayout.html)，它需要配合CoordinatorLayout 使用，同时在它里面的子view设置scrollFlag以达到滚动效果，否则就不会滚出屏幕，即悬停效果。

[Toolbar](https://developer.android.google.cn/reference/android/support/v7/widget/Toolbar.html)和[TabLayout](https://developer.android.google.cn/reference/android/support/design/widget/TabLayout.html)这两个控件并不是什么新鲜事物，故不多介绍。

想要成功引用上面这些控件，需要导入如下两个包
```java
compile 'com.android.support:appcompat-v7:26.0.0-alpha1'
compile 'com.android.support:design:26.0.0-alpha1'
```
个人认为重要的两点是scrollFlag以及behavior的理解   
第一：scrollFlag，总共是五种滚动效果，[官网指导说明](https://developer.android.google.cn/reference/android/support/design/widget/AppBarLayout.LayoutParams.html)，具体效果见如下gif   
![enterAlways](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/enterAlways.gif)    
![snap](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/snap.gif)  
![exitUntilCollapsed](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/exitUntilCollapsed.gif)  
![enterAlwaysCollapsed](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/enterAlwaysCollapsed.gif)  

第二：behavior   
CoordinatorLayout本身并没有做太多事情，和标准的framework视图一起使用时，它就跟一个普通的FrameLayout差不多。它的神奇之处来自于CoordinatorLayout.Behavior。通过为CoordinatorLayout的直接子view设置一个Behavior，就可以拦截touchEvents, window insets, measurement, layout, 和 nested scrolling等动作。

需要理清楚的是behavior可以拦截很多事件，比如拦截 Touch Events，拦截Window Insets，拦截Measurement 和 layout，同时可以通过设置依赖关系，让多个view之间实现联动效果，同时可以实现复杂的嵌套滚动效果。

详细使用可参考     
[拦截一切的CoordinatorLayout Behavior](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0224/3991.html)     
[关于CoordinatorLayout与Behavior的一点分析](http://www.jianshu.com/p/a506ee4afecb)       
### 1.2 状态栏透明+Activity转场动画效果
状态栏透明的实现主要是通过第三方库com.jaeger.statusbarutil:library:1.4.0实现的，结合了palette这个库实现取色效果，主要代码如下所示：    
![platte](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/platte.png)

Activity转场动画效果主要是通过ActivityCompat配合ActivityOptionsCompat完成的，具体使用步骤不再细说，需要注意两点

第一：清单文件中要转场的activity配置好特定的theme，也就是我们再style文件夹下定义好的ActivityTransitionTheme，其中有涉及到一些动画转换的配置文件，也需要一并引入。   
![ActivityTransitionTheme](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/ActivityTransitionTheme.png)

第二：在RecyclerView中，对item中的某一个view进行转场，需要通过findViewById找到真正的那个view，否则会出现错乱效果  
![activityCompat](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/activityCompat.png)

## 2 关于AppInfo中字段的说明
获取应用的各类信息，大致都在ActivityInfo和PackageInfo中，平时这两个类使用的频次可能很少，代码如下    
![appinfo](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/appinfo.png)

## 3 关于使用Kotlin的几点说明
首先获取手机中的所有的应用信息，本质上是交给PackageManager来处理，这里采用Rx的方式处理，逻辑上显得非常简洁，同时主子线程的切换也能很好的提高效率和优化交互，还利用了map函数，将输出的对象转成了另外一个自定义的集合数据。  
![observable](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/observable.png)

其次这次利用Kotlin扩展了Activity，在activity中使用这些方法，方便快捷，同时又不会造成同一代码多处出现的情况。   
![exactivity](https://raw.githubusercontent.com/LeeeYou/Img/master/appinfo/exactivity.png)

下面是参考链接：    
① [Design库-TabLayout属性详解](http://www.jianshu.com/p/2b2bb6be83a8)     
② [Material Design技术分享](https://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651231829&idx=1&sn=2418c741e7f0e41f6ac4fff4dc2de6f0&scene=1&srcid=0526kJT3uB1vIPjVXTVrSKjW&pass_ticket=muxDwCVjxMK%2Fz1ncol%2B4QyK94pZsvdn%2FSA4JigA6HWU1Hf%2Fr6BUURvZ002TBW6Oq#rd)       
③ [BRVAH官方使用指南（持续更新）](http://www.jianshu.com/p/b343fcff51b0)     
④ [使用CoordinatorLayout打造各种炫酷的效果](http://www.jianshu.com/p/f09723b7e887)      
⑤ [Kotlin官网指导](https://kotlinlang.org/docs/reference/idioms.html)    
⑥ [Android 详细分析AppBarLayout的五种ScrollFlags](http://www.jianshu.com/p/7caa5f4f49bd)    
⑦ [实现折叠式Toolbar：CollapsingToolbarLayout 使用完全解析](http://blog.csdn.net/a553181867/article/details/52871424)   
⑧ [https://developer.android.google.cn/guide/topics/ui/menus.html](https://developer.android.google.cn/guide/topics/ui/menus.html)        

下面是bug解决方法：     
① android.os.TransactionTooLargeException: data parcel size 1293724 bytes   
② https://stackoverflow.com/questions/43807056/rx-kotlin-map-function-cant-infer-return-type    
③ http://www.jianshu.com/p/255e2db67147 tablayout标题不见的问题    
④ http://blog.csdn.net/xiaochuanding/article/details/53727426  

