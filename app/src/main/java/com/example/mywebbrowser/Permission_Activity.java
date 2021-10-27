package com.example.mywebbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Permission_Activity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_);
        btn = findViewById(R.id.getpermission);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int permission_All = 1;
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                if(!hasPermission(Permission_Activity.this,permission))
                {
                    ActivityCompat.requestPermissions(Permission_Activity.this,permission,permission_All);
                }
            }
        });
    }
    public static boolean hasPermission(Context context, String... permissions)
    {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
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
}
