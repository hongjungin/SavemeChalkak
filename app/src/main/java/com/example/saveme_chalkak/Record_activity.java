package com.example.saveme_chalkak;

import android.Manifest;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.security.Permission;
import java.util.List;


public class Record_activity extends AppCompatActivity implements SurfaceHolder.Callback{

    private Camera cam;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);


        btn_record = (Button) findViewById(R.id.btn_record);

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recording){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    cam.lock();
                    recording = false;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Record_activity.this, "succeed", Toast.LENGTH_LONG).show();
                            try {
                                mediaRecorder = new MediaRecorder();
                                cam.unlock();
                                mediaRecorder.setCamera(cam);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mediaRecorder.setOrientationHint(90);
                                mediaRecorder.setOutputFile("/sdcard/boxfox.mp4");
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;
                                Toast.makeText(Record_activity.this, "start", Toast.LENGTH_LONG).show();

                            }catch (final  Exception e){
                                e.printStackTrace();
                                mediaRecorder.release();
                                return;
                            }
                        }
                    });
                }
            }
        });
        setting();


    }

    private void setting(){
        cam = Camera.open();
        cam.setDisplayOrientation(90);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void surfaceCreated(SurfaceHolder holder){
        try {
            if (cam == null){
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
        }catch (IOException e){

        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(cam);
    }
    public void refreshCamera(Camera camera){
        if(surfaceHolder.getSurface() == null) {
            return;
        }
        try{
            camera.stopPreview();
        }catch (Exception e){
        }
        setCamera(camera);
        try {
            cam.setPreviewDisplay(surfaceHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setCamera(Camera camera){
        cam = camera;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

}
