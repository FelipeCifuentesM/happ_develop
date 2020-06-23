package cl.jumpitt.happ.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import cl.jumpitt.happ.R
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity: ToolbarActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val url = intent.getStringExtra("urlWebView")
        val titleBar = intent.getStringExtra("titleBar")

        toolbarToLoad(toolbar as Toolbar, titleBar)
        enableHomeDisplay(true)

        webView.webChromeClient = object : WebChromeClient(){ }
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefreshWebView.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefreshWebView.isRefreshing = false
            }

            override fun onReceivedError(view: WebView?,request: WebResourceRequest?,error: WebResourceError?) {
                Log.e("Borrar", "error webview")
//                webView.stopLoading()
                super.onReceivedError(view, request, error)
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)


        //Refresh
        swipeRefreshWebView.setOnRefreshListener {
            webView.reload()
        }
        swipeRefreshWebView.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary), ContextCompat.getColor(this, R.color.colorAccent))

    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

}