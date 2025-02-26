package com.example.youtubeplayer.ui.theme

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.youtubeplayer.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var videoId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoId = intent.getStringExtra("video_id")
        if (videoId.isNullOrEmpty()) {
            Toast.makeText(this, "Error: No video ID provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        webView = findViewById(R.id.webView)
        setupWebView()
        loadYoutubeVideo()
    }

    private fun setupWebView() {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
    }

    private fun loadYoutubeVideo() {
        val htmlContent = """
    <html>
    <body style='margin:0;padding:0;'>
        <div style='position:relative;width:100%;padding-top:56.25%;'>
            <iframe style='position:absolute;top:0;left:0;width:100%;height:100%;' 
                src='https://www.youtube.com/embed/$videoId?autoplay=1' 
                frameborder='0' allowfullscreen>
            </iframe>
        </div>
    </body>
    </html>
""".trimIndent()

        webView.loadData(htmlContent, "text/html", "utf-8")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
