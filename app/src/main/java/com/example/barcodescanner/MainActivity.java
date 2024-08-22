package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private SurfaceView mCameraPreview;
    private CameraSource mCameraSource;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCameraPreview = binding.cameraView;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button btnScan = binding.btnScan;
        btnScan.setOnClickListener (view -> {
            btnScan.setVisibility(View.GONE);
            mCameraPreview.setVisibility(View.VISIBLE);
            startBarcodeScanner();
            Toast.makeText(MainActivity.this, "Start Barcode Scanner", Toast.LENGTH_SHORT).show();
        });
    }

    void startBarcodeScanner() {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);
            return;
        }

        BarcodeDetector mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector).setFacing(
                        CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setAutoFocusEnabled(true)
                .build();

        mCameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

                try {
                    mCameraSource.start(mCameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    Toast.makeText(MainActivity.this,
                            barcodes.valueAt(0).displayValue,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}