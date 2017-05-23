# AdsView 分屏的广告播放器

#### 支持大屏幕多屏播放
#### 支持滚动图片和视频轮播两种播放形式
#### 支持后台动态配置播放内容

# 效果展示
<div align="center">
<img src="screenshots/1.png" width="35%" height="35%"/>
<img src="screenshots/2.png" width="35%" height="35%"/>
</div>

# 使用方式

## 在布局文件中使用AdsView

```xml
<com.xwj.adsview.view.AdsView
        android:id="@+id/ads_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
## 给AdsView设置FragmentManager

```java
adsView = (AdsView) findViewById(R.id.ads_view);
adsView.setFragmentManager(getSupportFragmentManager());
```

## 若想添加自定义功能，请直接fork项目