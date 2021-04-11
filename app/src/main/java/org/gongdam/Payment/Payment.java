package org.gongdam.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.gongdam.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Payment extends AppCompatActivity {

    private WebView mainWebView;
    private static final String APP_SCHEME = "iamporttest://";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        intent = getIntent();
        String amount = intent.getStringExtra("amount");
        String payname = intent.getStringExtra("payname");


        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new InicisWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent2 = getIntent();
        Uri intentData = intent2.getData();


        if ( intentData == null ) {
            mainWebView.loadUrl("file:///android_asset/import.html?amount="+amount+"&name="+payname);
            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
            Log.e("CHECK_PAyMENT","0");
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            Log.e("CHECK_PAyMENT","2");
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                Log.e("CHECK_PAyMENT","1");
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
            }
        }

        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
        Log.e("CHECK_PAyMENT","3");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        if ( url.startsWith(APP_SCHEME) ) {
            String redirectURL = url.substring(APP_SCHEME.length()+3);
            mainWebView.loadUrl(redirectURL);

            Log.e("CHECK_PAyMENT","4");
        }
    }


}


