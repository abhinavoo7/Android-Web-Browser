package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {

    DatabaseHelper mydb;
    ListView histlist;
    ListAdapter listviewAdapter;
    ArrayList<HashMap<String,String>> userlist;
    LinearLayout empty;
    int multicount = 0;
    ArrayAdapter adapter;
    ArrayList<HashMap<String,String>> multilist = new ArrayList<>();
    String getmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mydb = new DatabaseHelper(this);
        histlist = findViewById(R.id.histlistview);
        empty = findViewById(R.id.emptyviewhist);
        empty.setVisibility(View.GONE);
        getdata();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,userlist);
        histlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = histlist.getAdapter().getItem(i);
                if(o instanceof Map)
                {
                    Map map = (Map)o;
                    Intent intent = new Intent(History.this,MainActivity.class);
                    intent.putExtra("second",String.valueOf(map.get("Url")));
                    startActivity(intent);
                }
            }
        });
        histlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        histlist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                multicount = histlist.getCheckedItemCount();
                actionMode.setTitle(multicount + " Items Selected");
                if(histlist.isItemChecked(i))
                {
                    multilist.add(userlist.get(i));
                }
                else
                {
                    multilist.remove(userlist.get(i));
                }
            }
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.hist_context_menu,menu);
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.select_all_hist:
                        for(int i=0 ; i<histlist.getAdapter().getCount(); i++)
                        {
                            histlist.setItemChecked(i,true);
                        }
                        return true;
                    case R.id.Delete_hist:
                        for(HashMap msg : multilist)
                        {
                            HashMap hashmap = (HashMap)msg;
                            getmap = (String)hashmap.get("Id");
                            Integer delete = mydb.deleteHistory(getmap);
                            if(delete > 0)
                            {
                                Toast.makeText(History.this, "Deleted", Toast.LENGTH_SHORT).show();
                                mydb.alterHistory();
                                getdata();
                            }
                            else
                            {
                                Toast.makeText(History.this, "Error Deleting", Toast.LENGTH_SHORT).show();
                            }
                            adapter.remove(msg);
                        }
                        Toast.makeText(History.this, multicount+" items deleted", Toast.LENGTH_SHORT).show();
                        multicount = 0;
                        multilist.clear();
                        actionMode.finish();
                        return true;
                }
                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
    }
    public void getdata()
    {
        userlist = mydb.showHistory();
        if(userlist.isEmpty())
        {
            empty.setVisibility(View.VISIBLE);
            return;
        }
        listviewAdapter = new SimpleAdapter(History.this,userlist,R.layout.hist_custom_list,
                new String[]{"Id","Title","Url", "Time"},
                new int[]{R.id.customhistid,R.id.customhisttitle,R.id.customhisturl,R.id.customhisttime});
        histlist.setAdapter(listviewAdapter);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}