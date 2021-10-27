package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Categories extends AppCompatActivity {

    static String url;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Search Engines
        Button google = (Button) findViewById(R.id.google);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateUrl("https://www.google.com");
            }
        });

        Button duck = (Button) findViewById(R.id.duckduckgo);
        duck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.duckduckgo.com";
                ValidateUrl(url);
            }
        });

        // Songs
        Button gaana = (Button) findViewById(R.id.gaana);
        gaana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.gaana.com/";
                ValidateUrl(url);
            }
        });

        Button wynk = (Button) findViewById(R.id.wynk);
        wynk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.wynk.in/music";
                ValidateUrl(url);
            }
        });

        // Entertainment
        Button netflix = (Button) findViewById(R.id.netflix);
        netflix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.netflix.com/in/";
                ValidateUrl(url);
            }
        });

        Button amazonprime = (Button) findViewById(R.id.amazonprime);
        amazonprime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.primevideo.com";
                ValidateUrl(url);
            }
        });

        // News
        Button times = (Button) findViewById(R.id.TimesOfIndia);
        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.timesofindia.indiatimes.com/";
                ValidateUrl(url);
            }
        });

        Button hindu = (Button) findViewById(R.id.thehindu);
        hindu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.thehindu.com/";
                ValidateUrl(url);
            }
        });

        Button india = (Button) findViewById(R.id.indiatv);
        india.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.indiatvnews.com/";
                ValidateUrl(url);
            }
        });

        // Online Shopping
        Button amazon = (Button) findViewById(R.id.amazon);
        amazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.amazon.in/";
                ValidateUrl(url);
            }
        });

        Button flipkart = (Button) findViewById(R.id.flipkart);
        flipkart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://www.flipkart.com/";
                ValidateUrl(url);
            }
        });
    }

    private void ValidateUrl(String url) {
        String prefix = "https://www.google.com/search?q=";

        if(!url.startsWith("http://") && !url.startsWith("https://") && !url.endsWith(".com"))
        {
            url=prefix+url;
        }
        if(url.endsWith(".com") || url.endsWith(".as") || url.endsWith(".uk") || url.endsWith(".biz"))
        {
            if(!url.startsWith("http://") && !url.startsWith("https://"))
            {
                url = "https://"+url;
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", url);
        MainActivity.mywebview.loadUrl(url);
        MainActivity.tooltext.setText(url);
        startActivity(intent);
    }
}