# Jetpack Compose 调用摄像头

要在 Jetpack Compose 中调用摄像头，您可以使用 Android Jetpack CameraX 库。以下是一个简单的 Kotlin 代码示例，用于在 Jetpack Compose 中调用摄像头并显示摄像头预览：

## 依赖

首先，请确保在 build.gradle 文件中添加以下依赖项：
```groovy
implementation "androidx.camera:camera-core:1.1.0-alpha04"
implementation "androidx.camera:camera-camera2:1.1.0-alpha04"
```

这个示例代码使用 AndroidView Composable 将 PreviewView 添加到 Jetpack Compose 布局中。请注意，在 setContent() 中调用 AndroidView() 会创建一个非常简单的 View 对象，并在 View 上运行 Jetpack Compose。

PreviewView 是一个自定义 View，用于显示摄像头预览。我们使用 Preview.SurfaceProvider 来获取摄像头预览表面，并将其与 Preview 绑定。最后，我们使用 PreviewView 和 SurfaceView 将预览视图添加到 Jetpack Compose 布局