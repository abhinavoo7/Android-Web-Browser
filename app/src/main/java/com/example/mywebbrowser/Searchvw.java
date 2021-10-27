package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Searchvw extends AppCompatActivity {

    SearchView searchview;
    ListView searchlist;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter adapter;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchvw);

        searchview = findViewById(R.id.searchView);
        searchlist = (ListView) findViewById(R.id.SearchListView);

        list.add("Android Studio");
        list.add("mobile");
        list.add("hit movies");
        list.add("latest songs");
        list.add("app development");
        list.add("mobile architecture");
        list.add("software engineer");
        list.add("learn to code");
        list.add("java");
        list.add("best actors");
        list.add("best singers");
        list.add("hollywood blockbusters");
        list.add("WWE");
        list.add("sports news");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        searchlist.setAdapter(adapter);
        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = searchlist.getAdapter().getItem(position);
                ValidateUrl(String.valueOf(item));
            }
        });

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                    ValidateUrl(query);
                }
                ValidateUrl(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(list.isEmpty())
                {   }
                else {
                    adapter.getFilter().filter(newText);
                }
                return false;
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("second", url);
        startActivity(intent);
    }
}