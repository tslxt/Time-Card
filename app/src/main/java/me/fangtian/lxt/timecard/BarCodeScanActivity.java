package me.fangtian.lxt.timecard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scan);

        action = getIntent().getStringExtra(ClassRoomActivity.ACTION);

        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(BarCodeScanActivity.this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(BarCodeScanActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},1);

            }
        }

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view

        if (Build.MODEL.equals("KNT-AL10")){
            mScannerView.setAspectTolerance(0.5f);
        }
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(final Result result) {
//        Snackbar.make(this.mScannerView, "学号 " + action + " 的学生 " + action, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        Toast.makeText(this, "学号  " + result.getText() +
                " 的学生 " + action, Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra(ClassRoomActivity.RETURN_BARCODE, result.getText());
                setResult(RESULT_OK, data);
                finish();
            }
        }, 1000);
    }
}
