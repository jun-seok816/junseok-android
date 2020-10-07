package com.example.smsbroadcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


     WebView wv;
    static String tv_sender;
     String tv_date;
    static String tv_content;
     String a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wv=findViewById(R.id.webview);
        initializeWebView();

        WebSettings settings=wv.getSettings();
        settings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());
        wv.loadUrl("file:///android_asset/index.html");


        requirePerms();



    }



    private void sendSMS(String phoneNumber,String message){
        SmsManager smsManager= SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message,null,null);

    }
    public void ShowSMS(Intent intent) {

        if (intent != null) {
            String  sender = intent.getStringExtra("sender");
            String date = intent.getStringExtra("date");
            String content = intent.getStringExtra("content");


            tv_sender = sender;
            tv_date = date;
            tv_content = content;

            Toast.makeText(getApplicationContext(),tv_content,Toast.LENGTH_SHORT).show();

            SMS();



        }

    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ShowSMS(intent);
    }




    private void requirePerms(){
        String[] permissions={Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,permissions,1);

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

    }
    @SuppressLint("JavascriptInterface")
    public void initializeWebView() {
        WebSettings webSettings = wv.getSettings();

        wv.addJavascriptInterface(new MainActivity(), "AndroidDevice");
        wv.setClickable(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);


        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);


        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
    }

    @JavascriptInterface
    public void ShowSMS2 () {

        sendSMS("01039750810", "4567");




    }
    public void SMS(){

        a = "전화번호" +tv_sender + "인증번호" + tv_content;
        wv.post(new Runnable() {
            @Override
            public void run() {
                    wv.loadUrl("javascript:(function() { "
                            + "document.getElementById('aa').innerHTML = '"+a+"'; "
                            + "})()");
            }
        });
    }


}

