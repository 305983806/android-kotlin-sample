package com.example.miniprogram.activity

import android.app.ActivityManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.miniprogram.jsbridge.MyWebViewClient
import com.example.miniprogram.R
import com.example.miniprogram.ui.theme.MiniProgramTheme
import kotlinx.coroutines.launch

class FirstActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiniProgramTheme {
                var rememberWebViewProgress: Int by remember {
                    mutableStateOf(-1)
                }
                // A surface container using the 'background' color from the theme
                Box {
                    CustomWebView(
                        modifier = Modifier.fillMaxSize(),
                        url = "file:///android_asset/index.html",
                        onBack = { webView ->
                            if (webView?.canGoBack() == true) {
                                webView.goBack()
                            } else {
                                finish()
                            }
                        },
                        onProgressChange = { progress ->
                            rememberWebViewProgress = progress
                        },
                        initSettings = { settings ->
                            settings?.apply {
                                // 支持js交互
                                javaScriptEnabled = true
                                // 将图片调整到适合 webView 的大小
                                useWideViewPort = true
                                // 缩放至屏幕的大小
                                loadWithOverviewMode = true
                                // 缩放操作
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = true
                                // 是否支持通过 JS 打开新窗口
                                javaScriptCanOpenWindowsAutomatically = true
                                // 不加载缓存内容
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }
                        },
                        onReceiveError = { error ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Log.d("AAAA", ">>>>>>${error?.description}")
                            }
                        }
                    )

                    LinearProgressIndicator(
                        progress = rememberWebViewProgress * 1.0F / 100F,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (rememberWebViewProgress == 100) 0.dp else 5.dp),
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        }

        setCustomTaskDescription()
    }

    private fun setCustomTaskDescription() {
        val taskDescription = ActivityManager.TaskDescription(
            "FirstActivity",
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
        )
        setTaskDescription(taskDescription)
    }
}


@Composable
fun CustomWebView(modifier: Modifier,
                  // 网络请求地址
                  url: String,
                  // 自己处理返回事件
                  onBack: (webView:WebView?) -> Unit,
                  // 自己选择是否接收，网页地址加载进度回调
                  onProgressChange: (progress:Int) -> Unit = {},
                  // 自己选择是否设置自己的WebSettings配置
                  initSettings: (webSettings:WebSettings?) -> Unit = {},
                  // 自己选择是否处理onReceivedError回调事件
                  onReceiveError: (error: WebResourceError?) -> Unit = {}
) {
    val webViewChromeClient = object:WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            // 回调网页内容加载进度
            onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    val webViewClient = object: WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            onProgressChange(-1)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onProgressChange(100)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (null == request?.url) return false
            val showOverrideUrl = request.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")) {
                    // 处理非 http 和 https 开头的链接地址
                    Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            } catch (e:Exception) {
                //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            // 接收到错误，交由调用者自行处理
            onReceiveError(error)
        }
    }

    var webView:WebView? = null
    val coroutineScope = rememberCoroutineScope()
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                this.webViewClient = webViewClient
                this.webChromeClient = webViewChromeClient
                // 供调用方设置 webSettings 的相关配置
                initSettings(this.settings)
                webView = this
                loadUrl(url)
//                addJavascriptInterface(JSKit(), "mjs")
                setWebViewClient(MyWebViewClient())
            }
        }
    )
    BackHandler {
        coroutineScope.launch {
            // 自行控制点击了返回按钮后，关闭页面还是返回上一级网页
            onBack(webView)
        }
    }
}