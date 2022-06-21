package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class NewsViewer extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewer);
        webView = findViewById(R.id.news_web_view);
        if (getIntent()!=null){
            String url = getIntent().getStringExtra("url");
            webView.loadUrl(url);
        }
    }
}