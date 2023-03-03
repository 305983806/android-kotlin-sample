package com.example.miniprogram.jsbridge

import android.util.Log
import android.webkit.JavascriptInterface

class JSKit {

    @JavascriptInterface
    fun hello(msg: String): String {
        Log.d("==> JSKit，接收到msg：", msg)
        return "hello world."
    }
}