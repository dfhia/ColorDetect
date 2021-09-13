package com.dims.colordetect;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();

        TextView txtGuide = (TextView) findViewById(R.id.textguide);
        txtGuide.setText("Cara Menggunakan Aplikasi\n" +
                "1.Pilih \"Scan\" pada menu utama\n" +
                "2.Arahkan Titik scan pada objek yang akan di deteksi\n" +
                "3.Cukup tekan layar aplikasi pada handphone Anda\n" +
                "4.Aplikasi akan memberikan Anda informasi warna \n" +
                "Yaitu :\n" +
                "\ta.Code Hex\n" +
                "\tb.Bar Warna\n" +
                "\tc.Nama warna dalam bentuk teks dan suara");

        TextView txtCopy = (TextView) findViewById(R.id.textcopy);
        txtCopy.setText("© 2021 Dimas Lutfhi Amrullah - All Rights Reserved");




        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21)
        {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21)
        {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}