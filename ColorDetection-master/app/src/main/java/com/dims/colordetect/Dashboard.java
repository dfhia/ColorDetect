package com.dims.colordetect;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.dims.colordetect.adapter.DashboardPagerAdapter;
import com.dims.colordetect.databinding.ActivityDashboardBinding;



public class Dashboard extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private ActivityDashboardBinding mBinding;
    public static TextToSpeech tts;
    private Camera camera;
    private Display display;
    private CameraPreview preview;
    private static TextView textView;



    //permissions stuff
    public static final String[] PERMS = {"android.permission.CAMERA"};
    public static final int PERMS_REQUEST_CODE = 200;

    public static void showColorInTextView(int i, int i1, int i2) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        allocation();
        setEvent();

        checkPermissionsAndGetCameraInstance();
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            // attempt to get a Camera instance of first rear facing camera
            c = Camera.open();

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * check persmissions
     */
    private void checkPermissionsAndGetCameraInstance() {
        if (checkifDeviceHasCamera(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMS, PERMS_REQUEST_CODE);
            }
        } else {
            Toast.makeText(Dashboard.this, getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
            // close the app
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMS_REQUEST_CODE) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(Dashboard.this, getResources().getString(R.string.no_camera_permission), Toast.LENGTH_LONG).show();
                finish();
            } else {
                //only initialize camera and preview if they are not set yet
                if (camera == null && preview == null) {
                }
            }
        }
    }


    private boolean checkifDeviceHasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // release the camera immediately on pause event
        releaseCamera();
    }

    /**
     * releases the camera
     */
    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private void allocation() {
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
    }

    private void setEvent() {
        mBinding.viewPager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager(),getApplicationContext()));

        Intent ttsIntent=new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        //noinspection deprecation
        startActivityForResult(ttsIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, Dashboard.this);
            } else {
                Intent i = new Intent();
                i.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                //noinspection deprecation
                startActivityForResult(i,1);
            }
        }
    }


    public static void speechNow(String str) {
        //noinspection deprecation
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Dashboard.speechNow("starting color detection");

            //tts.speak("Speech Engine INSTALLED", TextToSpeech.QUEUE_FLUSH, null);
            // Toast.makeText(ctx, "engine installed", Toast.LENGTH_SHORT).show();
        } else if (status == TextToSpeech.ERROR) {
            //noinspection deprecation
            tts.speak("Wait for Speech Engine", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(this, "error in engine installed", Toast.LENGTH_SHORT).show();
        }
    }

}
