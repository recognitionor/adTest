package com.jhlee.mraidtest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var mWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView = findViewById(R.id.webview)
        val webSettings = mWebView.getSettings()
        webSettings.javaScriptEnabled = true // JavaScript 활성화

        // MRAID 스크립트 로드
        mWebView.loadUrl("file:///android_asset/mraid.js")

        mWebView.loadDataWithBaseURL("file:///android_asset/", getAdContent(), "text/html", "UTF-8", null)
        // 광고 컨텐츠 로드

        // 광고 컨텐츠 로드
        mWebView.setWebChromeClient(object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("jhlee", "consoleMessage : " + consoleMessage.message())
                return super.onConsoleMessage(consoleMessage)
            }
        })

        class WebAppInterface(private val mContext: Context) {
            @JavascriptInterface
            fun open(url: String) {

                Intent(Intent.ACTION_VIEW, Uri.parse(url)).run {
                    this@MainActivity.startActivity(this)
                }
            }
        }

        mWebView.addJavascriptInterface(WebAppInterface(this), "Device")

        mWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.d("jhlee", "onPageFinished")
                val mraidEnvJson = createMraidEnv() // JSON 문자열을 생성하는 메소드 호출
                val script = "javascript:(function() { window.MRAID_ENV = $mraidEnvJson; })();"
                view.evaluateJavascript(script, null)
                val test = "javascript:window.onMraidReady()"
                view.evaluateJavascript(test, null)

            }

            override fun onLoadResource(view: WebView, url: String) {
                super.onLoadResource(view, url)
//                Log.d("jhlee", "onLoadResource : $url")
            }
        })


    }

    private fun getAdContent(): String {
        // MRAID 광고 마크업 및 스크립트를 포함한 HTML 문자열 반환
        val html = assets.open("mraid.html").bufferedReader().use { it.readText() }
        return html

//        return "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "\t<head>\n" +
//                "\t\t<meta charset=\"utf-8\" />\n" +
//                "\t\t<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\" />\n" +
//                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
//                "\n" +
//                "\t\t<title></title>\n" +
//                "\t\t<link rel=\"stylesheet\" href=\"css/main.css\" />\n" +
//                "\t\t<link rel=\"icon\" href=\"images/favicon.png\" />\n" +
//                "\n" +
//                "\t\t<script src=\"mraid.js\"></script>\n" +
//                "\t</head>\n" +
//                "\n" +
//                "\t<body>\n" +
//                "\t\t<div id=\"imageContainer\">\n" +
//                "\t\t\t<img src=\"https://nhnace.com/share/img/logo_main_ci.png\" width=\"300\" height=\"250\"/>\n" +
//                "\t\t</div>\t\n" +
//                "\t \n" +
//                "\t\t<script src=\"js/scripts.js\"></script>\n" +
//                "\t\t\n" +
//                "\t\t<script>\n" +
//                "\n" +
//                "\t\tfunction onMraidReady() {\n" +
//                "\t\t\tconsole.log(\"onMraidReady\")\n" +
//                "\t\t\tmraid.addEventListener(\"stateChange\", stateChangeHandler);\n" +
//                "\t\t\tdisplayAd();\n" +
//                "\t\t}\n" +
//                "\n" +
//                "\t\tfunction stateChangeHandler() {\n" +
//                "\t\t\tconsole.log(\"stateChangeHandler\")\n" +
//                "\t\t}\n" +
//                "\t\t\n" +
//                "\t\tfunction addEvent(evnt, elem, func) {\n" +
//                "\t\t\tif (elem.addEventListener) { // W3C DOM\n" +
//                "\t\t\t\telem.addEventListener(evnt, func, false);\n" +
//                "\t\t\t} else if (elem.attachEvent) { // IE DOM\n" +
//                "\t\t\t\telem.attachEvent(\"on\" + evnt, func);\n" +
//                "\t\t\t} else { // No much to do\n" +
//                "\t\t\t\telem[evnt] = func;\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t\t\n" +
//                "\t\tfunction displayAd()\n" +
//                "\t\t{\n" +
//                "\t\t\t//mraid.addEventListener(\"stateChange\", stateChangeHandler);\n" +
//                "\t\t\t//mraid.addEventListener(\"sizeChange\", sizeChangeHandler);\n" +
//                "\t\t\t//mraid.addEventListener(\"viewableChange\", viewableChangeHandler);\n" +
//                "\t\t\t//mraid.addEventListener(\"error\", errorHandler);\n" +
//                "\t\t\tconsole.log(\"displayAd\");\n" +
//                "\t\t\tvar adContainer = document.querySelector('#imageContainer');\n" +
//                "\t\t\taddEvent(\"click\", adContainer , function (e) {\n" +
//                "\t\t\t\tconsole.log(\"click\");\n" +
//                "\t\t\t\te.preventDefault();\n" +
//                "\t\t\t\tmraid.open('https://www.nhnace.com/bannerinfo');\n" +
//                "\t\t\t\t\n" +
//                "\t\t\t\treturn false;\n" +
//                "\t\t\t});\n" +
//                "\t\t}\t\n" +
//                "\t\t\n" +
//                "\t\tfunction readyAdPage() {\n" +
//                "\t\t\t mraid.removeEventListener(\"ready\", readyAdPage);\n" +
//                "\n" +
//                "\t\t\tdisplayAd();\n" +
//                "\t\t}\n" +
//                "\t\t\n" +
//                "\t\tfunction detectAdPageStatus() {\n" +
//                "\t\t\tconsole.log(\"detectAdPageStatus\");\n" +
//                "\t\t\tvar success = false;\n" +
//                "\n" +
//                "\t\t\tif (document.readyState === 'complete') {\n" +
//                "\t\t\t\n" +
//                "\t\t\t\tif (typeof (mraid) === \"undefined\") {\n" +
//                "\t\t\t\t\tsetTimeout(detectAdPageStatus, 250);\n" +
//                "\t\t\t\t} else {\n" +
//                "\t\t\t\t\tif (mraid.getState() === 'loading') {\n" +
//                "\t\t\t\t\t\tmraid.addEventListener('ready', readyAdPage);\n" +
//                "\t\t\t\t\t\t\n" +
//                "\t\t\t\t\t\tmraid.addEventListener(error,function(message,action){\n" +
//                "\t\t\t\t\t\t\talert('Error [' + action + ']' + message);\n" +
//                "\t\t\t\t\t\t})\n" +
//                "\t\t\t\t\t} else if (mraid.getState() === 'default') {\n" +
//                "\t\t\t\t\t\tdisplayAd();\n" +
//                "\t\t\t\t\t}\n" +
//                "\t\t\t\t\t\n" +
//                "\t\t\t\t\tsuccess = true;\n" +
//                "\t\t\t\t}\n" +
//                "\t\t\t}\n" +
//                "\t\t\t\n" +
//                "\t\t\treturn success;\t\n" +
//                "\t\t}\n" +
//                "\t\t\n" +
//                "\t\t// detectAdPageStatus();\n" +
//                "\t\t</script>\n" +
//                "\t</body>\n" +
//                "</html>";

//        return "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<head>\n" +
//                "    <title>MRAID Ad Example</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "    <h1>MRAID Ad Example</h1>\n" +
//                "    <div id=\"result\"></div>\n" + // 결과를 표시할 곳
//                "    <script src=\"mraid.js\"></script>\n" + // MRAID 스크립트 로드
//                "    <script>\n" +
//                "        console.log(111);\n" + // testPrint 함수 호출
////                "        var result = mraid.testPrint();\n" + // testPrint 함수 호출
////                "        document.getElementById('result').innerHTML = result;\n" + // 결과를 표시할 곳에 결과 삽입
//                "    </script>\n" +
//                "</body>\n" +
//                "</html>\n";
    }

    private fun createMraidEnv(): String {
        val obj = JSONObject()
        try {
            obj.put("version", "1.0.3")
            obj.put("sdk", "NHNACE_ADLIB")
            obj.put("sdkVersion", "1.0.3")
            obj.put("appId", packageName)
            obj.put("adid", "1111")
            obj.put("limitedAdTracking", true)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        return obj.toString()
    }
}