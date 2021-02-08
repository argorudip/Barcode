package com.argorudip.barcodetembakau;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ImageView ivBgContent;
    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivBgContent = findViewById(R.id.ivBgContent);
        scannerView = findViewById(R.id.scannerView);
        ivBgContent.bringToFront();

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String message = result.getText();
            showAlertDialog(message);
        }));

        checkCameraPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermission();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void checkCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "SCAN LAGI",
                (dialog, id) -> {
                    dialog.cancel();
                    mCodeScanner.startPreview();
                });

        builder.setNegativeButton(
                "CANCEL",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
//        AndroidNetworking.post(Config.host + "/smartbin/nasabah_scan_qr")
//                .addBodyParameter("kode_qr", message)
//                .addHeaders("Authorization", "Bearer " + token)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (response.optBoolean("status")) {
//                            Intent a = new Intent(MainBarcode.this, CekStatusQR.class);
//                            a.putExtra("transaksi_id", response.optString("transaksi_id"));
//                            startActivity(a);
//                            finish();
//                        } else {
//                            popupPeringatan("QRCode tidak terdaftar..");
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        Log.d("data1", "onError errorCode : " + error.getErrorCode());
//                        Log.d("data1", "onError errorBody : " + error.getErrorBody());
//                        Log.d("data1", "onError errorDetail : " + error.getErrorDetail());
//
//                        if (error.getErrorCode() == 400) {
//                            try {
//                                JSONObject body = new JSONObject(error.getErrorBody());
//                                popupPeringatan(body.optString("message"));
//                            } catch (JSONException ignored) {
//
//                            }
//
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            popupPeringatan("Qrcode yang anda Scan tidak terdaftar!! klik 'OK' untuk menutup pesan ini.");
//                        }
//
//                    }
//                });

    }



}