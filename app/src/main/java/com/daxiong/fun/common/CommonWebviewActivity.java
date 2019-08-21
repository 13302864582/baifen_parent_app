
package com.daxiong.fun.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daxiong.fun.R;
import com.daxiong.fun.base.BaseActivity;

/**
 * 此类的描述：公共的webview
 * 
 * @author: sky
 */
public class CommonWebviewActivity extends BaseActivity {
    private String title;

    private String url;

    private WebView webView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.common_webview_activity);
        getExtraData();
        initView();
        initListener();
        initSetting();
    }

    public void getExtraData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
    }

    @Override
    public void initView() {
        super.initView();
        webView=(WebView)findViewById(R.id.webView1);
    }
    
    private void initSetting() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
    }

    @Override
    public void initListener() {
        super.initListener();
        webView.setWebViewClient(new MyWebViewClient ());
    }
    
    
    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            if(Uri.parse(url).getHost().equals("www.example.com"))
            {
                // This is my web site, so do not override; let my WebView load
                // the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
    
    
    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            // 返回键退回
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

 

}
