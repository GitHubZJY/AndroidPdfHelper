# AndroidPdfHelper [![](https://jitpack.io/v/GitHubZJY/AndroidPdfHelper.svg)](https://jitpack.io/#GitHubZJY/AndroidPdfHelper)
一个基于Android PdfRenderer实现的PDF预览组件，支持对PDF文件的分页切换、放大缩小、拖动定位等操作。<br/>
A PDF preview component based on Android pdfRenderer, which supports paging switching, zooming in and out, dragging and positioning of PDF files.

## 特性
1. 基于PdfRenderer实现，不同于其它第三方库，占用包体小 <br/>
2. 支持PDF文件的上下页切换 <br/>
3. 支持PDF单页的放大缩小查看 <br/>
4. 支持设置文件预览清晰度 <br/>
5. 支持AndroidX <br/>

## 效果预览
![](https://github.com/GitHubZJY/AndroidPdfHelper/blob/master/image/pdf_preview_1.png)

## 如何使用
在项目根目录的build.gradle添加：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

在项目的build.gradle添加如下依赖：
```
implementation 'com.github.GitHubZJY:AndroidPdfHelper:v1.0.0'
```

### 1.以View的方式调用

```xml
<com.zjy.pdfview.PdfView
        android:id="@+id/pdf_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >

</com.zjy.pdfview.PdfView>
```

在代码中初始化PdfView
```java
PdfView pdfView = findViewById(R.id.pdf_view);
```
&nbsp;
预览在线PDF文件:
```java
pdfView.loadPdf("http://.....xx.pdf");
```
&nbsp;
预览asset文件:
```java
pdfView.loadPdf("file:///android_asset/test.pdf");
```

### 2.以页面方式调起
以页面的形式，自带了默认的顶部标题栏，适配Android 5.0以下，会自动下载并调用浏览器打开
预览在线PDF文件:
```java
PdfPreviewUtils.previewPdf(context, "http://.....xx.pdf");
```
&nbsp;
预览asset文件:
```java
PdfPreviewUtils.previewPdf(context, "file:///android_asset/test.pdf");
```

&nbsp;
### 3.设置预览清晰度.
```java
<com.zjy.pdfview.PdfView
    android:id="@+id/pdf_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:quality="medium">

</com.zjy.pdfview.PdfView>
```
通过设置 `quality` 属性即可，目前一共有低、中、高三种清晰度，如下：
>高清晰度：high
中等清晰度：medium
低清晰度：low

&nbsp;
## 其他
本库基于PdfRenderer实现，目前支持在线或本地pdf文件预览，另外还支持侧边导航滑块，可定位到任意一页，双指拖拽或双击可对单页进行放大缩小控制。
由于PdfRenderer提供的支持有限，主要还是在于预览在线和本地PDF文件，但优点在于其体积小，后续会继续更新，提供更多PDF预览方面的功能，欢迎issue和star~
