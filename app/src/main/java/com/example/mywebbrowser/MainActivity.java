package com.example.mywebbrowser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    static WebView mywebview;
    static String title;
    static String url = "https://www.google.co.in/";

    static EditText tooltext;
    private java.util.Timer timer = new Timer();
    private double back = 0;
    private TimerTask timerTask;
    DatabaseHelper mydb;

    String DownloadImageUrl;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.progressbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mydb = new DatabaseHelper(this);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("second");
        }

        mywebview = findViewById(R.id.webview);
        mywebview.loadUrl(url);


        tooltext = findViewById(R.id.toolbartext);

        WebSettings webSettings = mywebview.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.supportZoom();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);

        mywebview.clearHistory();
        mywebview.clearCache(true);

        mywebview.setWebChromeClient(new WebChromeClient());
        registerForContextMenu(mywebview);
        mywebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                tooltext.setText(mywebview.getUrl());
                final String Urls = url;

                if (Urls.contains("mailto:") || Urls.contains("sms:") || Urls.contains("tel:")) {
                    mywebview.stopLoading();
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(Urls));
                    startActivity(i);
                }
                addhistory();

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
                tooltext.setText(mywebview.getUrl());
                super.onPageFinished(view, url);
                final String urls = url;
                if (urls.contains("mailto:") || urls.contains("sms:") || urls.contains("tel:")) {
                    mywebview.stopLoading();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls));
                    startActivity(intent);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        back = 1;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        back = 1;
                        timerTask.cancel();
                    }
                });
            }
        };
        timer.schedule(timerTask, (int)3000);
        back = 1;

        tooltext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ValidateUrl(tooltext.getText().toString());
                return true;
            }
        });
        tooltext.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();

                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tooltext.getRight() - tooltext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        startActivity(new Intent(MainActivity.this,Searchvw.class));

                        return true;
                    }
                }
                return false;
            }
        });
        mywebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String useragent, String contentdisposition, String mimetype, long contentlength) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            DownloadDialog(url,useragent,contentdisposition,mimetype);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }
                    }
                    else
                    {
                        DownloadDialog(url,useragent,contentdisposition,mimetype);
                    }
            }
        });
    }


    public void addhistory()
    {
        title = mywebview.getTitle();
        url = mywebview.getUrl();
        if (mywebview.getUrl() != null && mywebview.getTitle() != null && mywebview.getUrl() != " " && mywebview.getTitle() != " "){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY, hh:mm a", Locale.getDefault());
            String time = simpleDateFormat.format(new Date());
            boolean isInserted = mydb.insertHist(title,url,time);
            if(isInserted)
            {
                //Toast.makeText(this, "History Added", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Error adding History", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void DownloadDialog(final String url , final String UserAgent, String contentdisposition, String mimetype)
    {
        final String filename = URLUtil.guessFileName(url,contentdisposition,mimetype);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Downloading...")
                .setMessage("Do yo Want To Download"+ ' '+" "+filename+" "+' ')
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DownloadManager.Request request =new DownloadManager.Request(Uri.parse(url));
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("Cookie",cookie);
                        request.addRequestHeader("User-Agent",UserAgent);
                        request.allowScanningByMediaScanner();

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager manager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);

                        manager.enqueue(request);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY, hh:mm a", Locale.getDefault());
                        File path = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + filename);
                        addDownload(filename, simpleDateFormat.format(new Date()), String.valueOf(path));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                })
                .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final WebView.HitTestResult webviewHittestResult = mywebview.getHitTestResult();
        DownloadImageUrl = webviewHittestResult.getExtra();
        if(webviewHittestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webviewHittestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE)
        {
            if(URLUtil.isNetworkUrl(DownloadImageUrl))
            {
                menu.setHeaderTitle("Download Image from Below");
                menu.add(0,1,0,"Download Image")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int Permission_all = 1;
                                String Permission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                                if(!hasPermission(MainActivity.this,Permission))
                                {
                                    ActivityCompat.requestPermissions(MainActivity.this,Permission,Permission_all);
                                }
                                else
                                {
                                    String filename = "";
                                    String type = null;
                                    String Mimetype = MimeTypeMap.getFileExtensionFromUrl(DownloadImageUrl);
                                    filename = URLUtil.guessFileName(DownloadImageUrl,DownloadImageUrl,Mimetype);
                                    if(Mimetype!=null)
                                    {
                                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(Mimetype);

                                    }
                                    if(type==null)
                                    {
                                        filename = filename.replace(filename.substring(filename.lastIndexOf(".")),".png");
                                        type = "image/*";
                                    }
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageUrl));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                                    DownloadManager manager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                                    manager.enqueue(request);

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY, hh:mm a", Locale.getDefault());
                                    File path = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + filename);
                                    addDownload(filename, simpleDateFormat.format(new Date()), String.valueOf(path));

                                    Toast.makeText(MainActivity.this, "Check Notification", Toast.LENGTH_SHORT).show();
                                }
                                    return false;
                            }
                        });
                menu.add(0,2,0,"Copy Image Address").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String copyimageurl = webviewHittestResult.getExtra();
                        ClipboardManager manager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label",copyimageurl);
                        manager.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                menu.add(0,3,0,"Share").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Picasso.get().load(DownloadImageUrl).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("image/*");
                                i.putExtra(Intent.EXTRA_STREAM,getloacalBitmaUri(bitmap));
                                startActivity(Intent.createChooser(i,"Share Image"));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        return false;
                    }
                });
            }
        }
        else
        {
            Toast.makeText(this, "Error Downloading", Toast.LENGTH_SHORT).show();

        }
    }
    public Uri getloacalBitmaUri(Bitmap bmp)
    {
        Uri bmpuri = null;
        try{
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"shareimage"+ System.currentTimeMillis()+".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,90,out);
            out.close();
            bmpuri = FileProvider.getUriForFile(getApplicationContext(),"studio.amit.mybrowser.provider",file);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return bmpuri;
    }
    public static boolean hasPermission(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context!=null && permissions!=null)
        {
            for(String permission : permissions)
            {
                if(ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void addDownload(String Title,String Time,String Path)
    {
        String title = Title;
        String time = Time;
        String path = Path;
        boolean isInserted = mydb.insertDownload(title,time,path);
        if(isInserted)
        {
            Toast.makeText(this, "Download Added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Error adding Downloads", Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidateUrl(String url) {
        String prefix = "https://www.google.com/search?q";

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
        mywebview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mywebview.canGoBack()){
            mywebview.canGoBack();
        }
        else if (back==1){
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG);
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            back = 2;
                        }
                    });
                }
            };
            timer.schedule(timerTask, (int)0);
            back = 2;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        back = 1;
                    }
                });
            }
        };
        timer.schedule(timerTask, (int)3000);
        if (back==2)
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);


        if(mywebview.canGoForward())
        {
            menu.getItem( 1).setEnabled(true);
        }
        if(mywebview.canGoBack())
        {
            menu.getItem(1).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        invalidateOptionsMenu();

        if(id==R.id.reload)
        {
            mywebview.reload();
        }
        if(id==R.id.Previous)
        {
            if(mywebview.canGoBack())
            {
                mywebview.goBack();
            }
        }
        if(id==R.id.Forward)
        {
            if(mywebview.canGoForward())
            {
                mywebview.goForward();
            }
        }
        if(id==R.id.second)
        {
            startActivity(new Intent(MainActivity.this,MainActivity2.class));
            ValidateUrl(MainActivity2.et.getText().toString());
        }
        if(id==R.id.permission)
        {
            startActivity(new Intent(MainActivity.this,Permission_Activity.class));

        }
        if (id==R.id.Downloads){
            startActivity(new Intent(MainActivity.this, Downloads.class));
        }

        if(id == R.id.History)
        {
            startActivity(new Intent(MainActivity.this,History.class));
            if (getIntent().getExtras()!=null){
                url = getIntent().getStringExtra("urlkey");
            }
        }

        if(id==R.id.textrecognizer)
        {
            startActivity(new Intent(MainActivity.this,text_recognize.class));
        }
        if(id==R.id.translator)
        {
            startActivity(new Intent(MainActivity.this,translator.class));
        }
        if (id==R.id.categories){
            startActivity(new Intent(MainActivity.this, Categories.class));
            tooltext.setText(Categories.url);
            title = Categories.url;
            url = Categories.url;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnspeech(MenuItem item) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");
        try {
            startActivityForResult(intent, 1);
        }catch (ActivityNotFoundException e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tooltext.setText(result.get(0));
                }
                break;
        }
    }
}