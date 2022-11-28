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

import java.security.Permission;
import java.util.List;


public class Record_activity extends AppCompatActivity implements  SurfaceHolder.Callback{

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();

        //녹화 시작하고 나서 멈출 때
        if (recording){
            mediaRecorder.stop();
            mediaRecorder.release();
            camera.lock();
            recording = false;
        }
        //처음으로 녹화 시작했을 때
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Record_activity.this, "녹화가 시작되었습니다.", Toast.LENGTH_SHORT).show();
                    try {
                        mediaRecorder = new MediaRecorder();
                        camera.unlock();
                        mediaRecorder.setCamera(camera);
                        //동영상 녹화 시작되는 소리 -> 뺄 예정
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                        mediaRecorder.setOrientationHint(90);
                        mediaRecorder.setOutputFile("/sdcard/test.mp4");
                        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        recording = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        mediaRecorder.release();
                    }
                }
            });
        }

        //btn_record = (Button)findViewById(R.id.btn_record);
        //btn_record.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //녹화 시작하고 나서 멈출 때
                //if (recording){
                    //mediaRecorder.stop();
                    //mediaRecorder.release();
                    //camera.lock();
                    //recording = false;
                //}
                //처음으로 녹화 시작했을 때
                //else {
                    //runOnUiThread(new Runnable() {
                        //@Override
                        //public void run() {
                            //Toast.makeText(Record_activity.this, "녹화가 시작되었습니다.", Toast.LENGTH_SHORT).show();
                            //try {
                                //mediaRecorder = new MediaRecorder();
                                //camera.unlock();
                                //mediaRecorder.setCamera(camera);
                                //동영상 녹화 시작되는 소리 -> 뺄 예정
                                //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                //mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                //mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                                //mediaRecorder.setOrientationHint(90);
                                //mediaRecorder.setOutputFile("/sdcard/test.mp4");
                                //mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                //mediaRecorder.prepare();
                                //mediaRecorder.start();
                                //recording = true;
                            //}catch (Exception e){
                                //e.printStackTrace();
                                //mediaRecorder.release();
                            //}
                        //}
                    //});
                //}
            //}
        //});


    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(Record_activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(Record_activity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(Record_activity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void refreshCamera(Camera camera) {
        if (surfaceHolder.getSurface() == null){
            return;
        }
        //카메라 초기화 해주는 작업
        try {
            camera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }

        setCamera(camera);
    }

    private void setCamera(Camera cam) {

        camera = cam;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(camera);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
