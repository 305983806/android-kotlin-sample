# Camera QRCode
演示如何使用 CameraX 的 MlKitAnalyzer 构建qr代码扫描仪。

## 相关知识点（踩坑记）

### 一、找不到 ActivityMainBinding 类
这是 ViewBinding 相关知识点。

#### ViewBinding 是什么
2020年的3月份 巨佬 JakeWharton 开源的 butterknife 被官宣 停止维护，关于 ViewBinding 的介绍在这里[官网](https://developer.android.google.cn/topic/libraries/view-binding?hl=zh-cn)

> 替换 findViewById 的原因大概归结为以下几个原因：
> 1. 过于冗余：findViewById 对应所有的 View 都需要书写 findViewById(R.id.xxx) 的方法
> 2. 不安全：所谓的不安全，分为两点，一是空类型的不安全，findViewById 有可能返回 null，导致程序异常；二是强转的不安全，findViewById，将对应的 id 需要强转成对应的 view 例如    
> ``Textview textView = findViewById(R.id.textview);``    
> 一旦我的类型给错了，就会出现异常，比如将 textview 错误地强转成 ImageView

#### 在使用 ViewBinding 前的注意事项：
1. 确保你的 AndroidStudio 是3.6或更高的版本
2. 在 build.gradle 中加入以下配置：
```groovy
buildFeatures {
    // 开启 viewBinding
    viewBinding true 
}
```

#### 使用 ViewBinding 的步骤：
1. 首先要调用 activity_main.xml 布局文件对应的 Binding 类，也就是 ActivityMainBinding 的 inflate() 函数去加载该布局，
   inflate() 函数接收一个 LayoutInflater 参数，在 Activity 中是可以直接获取到的
2. 接下来调用 Binding 类的 getRoot() 函数可以得到 activity_main.xml 中根元素的实例
3. 把根元素的实例传入到 setContentView() 函数当中，这样 Activity 就可以成功显示 activity_main.xml 这个布局的内容了。
