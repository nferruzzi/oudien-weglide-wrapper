package org.weglide.copilot;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private static final String URL = "https://copilot.weglide.org";
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private WebView webView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep screen on during flight
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Setup WebView
        webView = findViewById(R.id.webview);
        setupWebView();

        // Request location permissions
        requestLocationPermission();

        // Load the PWA
        webView.loadUrl(URL);
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();

        // Enable JavaScript (required for PWA)
        settings.setJavaScriptEnabled(true);

        // Enable DOM storage for PWA
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        // Enable geolocation
        settings.setGeolocationEnabled(true);

        // Caching
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Viewport
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Hide WebView marker
        String userAgent = settings.getUserAgentString();
        settings.setUserAgentString(userAgent.replace("; wv", ""));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Try to auto-click "No thanks" or similar dismiss buttons
                handler.postDelayed(() -> {
                    view.evaluateJavascript(
                        "(function() {" +
                        "  var buttons = document.querySelectorAll('button, a, [role=button]');" +
                        "  for (var i = 0; i < buttons.length; i++) {" +
                        "    var text = buttons[i].innerText.toLowerCase();" +
                        "    if (text.includes('no thanks') || text.includes('skip') || " +
                        "        text.includes('continue') || text.includes('dismiss') ||" +
                        "        text.includes('later') || text.includes('close')) {" +
                        "      buttons[i].click();" +
                        "      return 'clicked: ' + buttons[i].innerText;" +
                        "    }" +
                        "  }" +
                        "  return 'no button found';" +
                        "})()",
                        null);
                }, 1500); // Wait 1.5 seconds for page to render
            }
        });

        // Handle geolocation
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST
                );
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }
}
