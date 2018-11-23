package com.taobao.tae.sdk.demo;

import java.io.IOException;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ma.MaService;
import com.taobao.ma.analyze.api.MaAnalyzeAPI;
import com.taobao.ma.analyze.helper.MaAnalyzeHelper;
import com.taobao.ma.bar.parser.MaBarParSer;
import com.taobao.ma.common.result.MaResult;
import com.taobao.ma.qr.parser.MaQrParSer;
import com.taobao.tae.sdk.TaeSDK;

@SuppressWarnings("deprecation")
public class CameraActivity extends Activity implements PreviewCallback, AutoFocusCallback {

    private PreviewTask mPreviewTask;
    private SurfaceView mSurfaceView;
    private Camera camera;
    private boolean isPreview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreviewTask = new PreviewTask();
        initUI();
    };

    private void initUI() {

        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.camera);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(new SurfaceCallback());

    }

    private class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open();// 打开硬件摄像头，这里导包得时候一定要注意是android.hardware.Camera
                Camera.Parameters parameters = camera.getParameters();// 得到摄像头的参数
                Size size = parameters.getPreviewSize();
                parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的格式
                parameters.setJpegQuality(85);// 设置照片的质量
                parameters.setPictureSize(size.width, size.height);// 设置照片的大小，默认是和 屏幕一样大
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(mSurfaceView.getHolder());// 通过SurfaceView显示取景画面
                camera.startPreview();
                isPreview = true;// 设置是否预览参数为真
                camera.autoFocus(CameraActivity.this);

            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                if (isPreview) {// 如果正在预览
                    camera.stopPreview();
                    camera.release();
                }
            }
        }

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mPreviewTask.begin) {
            return;
        }

        mPreviewTask = new PreviewTask();
        mPreviewTask.mData = data;
        mPreviewTask.mCamera = camera;
        mPreviewTask.execute();
        camera.autoFocus(CameraActivity.this);
    }

    private class PreviewTask extends AsyncTask<Void, Void, MaResult> {
        public byte[] mData;
        public Camera mCamera;
        public boolean begin = false;

        @Override
        protected MaResult doInBackground(Void... params) {
            begin = true;
            Size size = mCamera.getParameters().getPreviewSize();
            MaAnalyzeAPI.registerResultParser(new MaBarParSer());
            MaAnalyzeAPI.registerResultParser(new MaQrParSer());
            YuvImage yuvImage = MaAnalyzeHelper.buildYuvImage(mData, mCamera);
            Rect region = buildDefaultDecodeRegion(size.width, size.height);
            return AlibabaSDK.getService(MaService.class).decode(yuvImage, region);
        }

        @Override
        protected void onPostExecute(MaResult result) {
            super.onPostExecute(result);
            if (result == null) {
                begin = false;
            } else {
                Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        camera.setOneShotPreviewCallback(CameraActivity.this);
    }

    private Rect buildDefaultDecodeRegion(int width, int height) {
        int x1 = Math.abs((width - height) / 2);
        int y1 = 0;
        int w1 = Math.min(width, height) / 8 * 8;

        final Rect rect = new Rect(x1, y1, x1 + w1, y1 + w1);
        return rect;
    }

}
