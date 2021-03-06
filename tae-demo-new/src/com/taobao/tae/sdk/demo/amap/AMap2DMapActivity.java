package com.taobao.tae.sdk.demo.amap;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.taobao.tae.sdk.demo.R;

/**
 * AMapV1地图中介绍如何显示一个基本地图
 */
public class AMap2DMapActivity extends Activity {
    private MapView mapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2dmap);
        mapView = (MapView) findViewById(R.id.map2d);
        mapView.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    System.out.println("zoom level is:" + cameraPosition.tilt);
                }

                @Override
                public void onCameraChange(CameraPosition arg0) {
                }
            });
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
