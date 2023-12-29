package com.ym.geolocation;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;

public class PopWebViewActivity extends AppCompatActivity {

    private WebView subwebView;
    private WebSettings subWebSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);

        subwebView = findViewById(R.id.subwebview);
        // WebViewAssetLoader 생성
        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        // WebView에 AssetLoader 설정
        subwebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        // 로드할 HTML 파일의 경로
        String htmlPath = "file:///android_asset/www/04_test.html";

        //subwebView.loadUrl("file:///android_asset/www/04_test.html");
        //subwebView.loadUrl("file:///android_asset/www/test.html");
        //subwebView.loadUrl("https://m.naver.com");

        subwebView.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.

        subWebSetting = subwebView.getSettings(); // 웹뷰에서 webSettings를 사용할 수 있도록 함.
        subWebSetting.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
        subWebSetting.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
        subWebSetting.setAllowFileAccess(true); //파일 엑세스
        subWebSetting.setLoadWithOverviewMode(true); // 메타태그
        subWebSetting.setUseWideViewPort(true); //화면 사이즈 맞추기
        subWebSetting.setSupportZoom(true); // 화면 줌 사용 여부
        //subWebSetting.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        //subWebSetting.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        subWebSetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
        subWebSetting.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
        /*
        * Access to XMLHttpRequest at 'file:///android_asset/www/04_test.html"' from origin 'null' has been blocked by CORS policy:
        * Cross origin requests are only supported for protocol schemes: http, data, chrome, https.
        * */
        subWebSetting.setAllowFileAccessFromFileURLs(true);
        subWebSetting.setAllowUniversalAccessFromFileURLs(true);

        //subwebView.loadUrl("file:///android_asset/www/04_test.html");
        subwebView.loadUrl("https://threejs-bjchh.run.goorm.site/threejs/");
    }




}
