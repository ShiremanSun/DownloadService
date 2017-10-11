package com.example.droodsunny.downloadservice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Button pause;
    private Button cancel;
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=(Button)findViewById(R.id.start_download);
        pause=(Button)findViewById(R.id.pause_download);
        cancel=(Button)findViewById(R.id.cancel_download);

        myOnClickListener myOnClickListener=new myOnClickListener();
        start.setOnClickListener(myOnClickListener);
        pause.setOnClickListener(myOnClickListener);
        cancel.setOnClickListener(myOnClickListener);

        /*开启服务*/
        Intent intent = new Intent(MainActivity.this,DownloadService.class);
        startService(intent);//启动服务
        bindService(intent,connection,BIND_AUTO_CREATE);//绑定服务
        /*申请权限*/
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
    class myOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(downloadBinder==null){
                return;
            }
            switch (v.getId()){
                case R.id.start_download:
                    String url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507716340168&di=4ee9ea012ac2edce7d0f254494580e6b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb999a9014c086e062d68efaf08087bf40bd1cb16.jpg";
                    downloadBinder.startDownload(url);
                    break;
                case R.id.pause_download:
                    downloadBinder.pauseDownload();
                    break;
                case R.id.cancel_download:
                    downloadBinder.cancelDownload();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"拒绝权限无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
