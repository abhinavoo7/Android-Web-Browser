package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Downloads extends AppCompatActivity {

    DatabaseHelper mydb;
    ListView listView;
    ListAdapter listViewAdapter;
    ArrayAdapter adapter;
    ArrayList<HashMap<String, String>> downlist;

    LinearLayout emptylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);

        mydb = new DatabaseHelper(this);
        listView = findViewById(R.id.downloadlistview);
        emptylist = findViewById(R.id.emptyList);
        emptylist.setVisibility(View.GONE);
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object ob = listView.getAdapter().getItem(position);
                if (ob instanceof Map){
                    Map map = (Map)ob;
                    try {
                        String filename = String.valueOf(map.get("Title"));
                        String extension = filename.substring(filename.lastIndexOf(".")+1);
                        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        File file = new File(Environment.getExternalStorageDirectory() + "/" +
                                Environment.DIRECTORY_DOWNLOADS + "/" + filename);
                        Uri filepath = Uri.parse(String.valueOf(file));
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        in.setDataAndType(filepath,type);
                        in.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(in,"Open With: "));
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(Downloads.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getData(){
        downlist = mydb.showDownloads();
        if (downlist.isEmpty()){
            emptylist.setVisibility(View.VISIBLE);
            return;
        }
        listViewAdapter = new SimpleAdapter(Downloads.this, downlist, R.layout.download_custom_list,
                new String[]{"Id_download","Title","Time","Path"},  new int[]{R.id.customiddownload,R.id.customtitledownlaod,
                R.id.customtimedownload,R.id.custompathdownload});
        listView.setAdapter(listViewAdapter);
    }
}