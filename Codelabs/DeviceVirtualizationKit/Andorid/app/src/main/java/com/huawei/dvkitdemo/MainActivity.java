
package com.huawei.dvkitdemo;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import com.huawei.dvkitdemoone.R;

public class MainActivity extends AppCompatActivity {

    static final int PERMISSIONS_REQUEST = 101;

    // 需要请求的权限
    String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isSystemSupport = true;
        try {
            Class.forName("com.huawei.dmsdp.devicevirtualization.DvKit");
            Class.forName("com.huawei.dmsdp.devicevirtualization.VibratorService");
        } catch (ClassNotFoundException e) {
            isSystemSupport = false;
            Toast.makeText(this,"this phone not support dvkit",Toast.LENGTH_SHORT).show();
        }

        //demo入口按钮
        Button dvKitDemoBtn = findViewById(R.id.dvkit_demo);
        Button wear = findViewById(R.id.wear_test);

        if (!isSystemSupport) {
            dvKitDemoBtn.setText("NOT SUPPORT");
            dvKitDemoBtn.setEnabled(false);
            wear.setText("NOT SUPPORT");
            wear.setEnabled(false);
        } else {
            dvKitDemoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否获取到需要的权限
                    mPermissionList.clear();
                    for (int i = 0; i < permissions.length; i++) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                            mPermissionList.add(permissions[i]);
                        }
                    }
                    if (mPermissionList.isEmpty()) {
                        // 未授予的权限为空，表示都授予了
                        Intent intent = new Intent(MainActivity.this, DvKitDemoActivity.class);
                        startActivity(intent);
                    } else {
                        // 请求权限方法
                        String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);// 将List转为数组
                        requestPermissions(permissions, PERMISSIONS_REQUEST);
                    }
                }
            });
            wear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, HiWearActivity.class));
                }
            });
        }
    }

    // 获取到权限后，打开DvKitDemo Activity
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.remove(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, DvKitDemoActivity.class);
                startActivity(intent);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
