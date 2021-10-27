package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    public static EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        et = findViewById(R.id.secondtext);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ValidateUrl(et.getText().toString());
                return true;
            }
        });
    }
    private void ValidateUrl(String url) {
        String prefix = "https://www.google.com/search?q=";

        if(!url.startsWith("http://") && !url.startsWith("https://")&&
                !url.endsWith(".com"))
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
        Intent in = new Intent(MainActivity2.this,MainActivity.class);
        in.putExtra("second", url);
        startActivity(in);
    }
}