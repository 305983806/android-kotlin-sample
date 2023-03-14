package com.example.miniprogram.jsbridge

import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient: WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        val uri: Uri?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uri = request?.url
        } else {
            uri = Uri.parse(request.toString())
        }

        if (uri?.scheme.equals("js")) {
            if (uri?.authority.equals("webview")) {
                val collection = uri?.queryParameterNames
                var result = "Android 回调给 JS 的数据为 userid=123456"
                //TODO
                view?.loadUrl("javascript:returnResult(\"$result\")");
            }
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

}