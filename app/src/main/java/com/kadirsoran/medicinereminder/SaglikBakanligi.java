package com.kadirsoran.medicinereminder;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
public class SaglikBakanligi extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_resources);

        webView = findViewById(R.id.webViewEducationalResources);
        webView.setWebViewClient(new WebViewClient());

        String url = "https://www.saglik.gov.tr";
        webView.loadUrl(url);
    }
}