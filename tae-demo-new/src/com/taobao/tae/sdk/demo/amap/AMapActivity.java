package com.taobao.tae.sdk.demo.amap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.taobao.tae.sdk.demo.AbstractActivity;
import com.taobao.tae.sdk.demo.R;

public class AMapActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

        case R.id.openGaodeMap2d:
            intent = new Intent(this, AMap2DMapActivity.class);
            break;
        case R.id.openGaodeMap3d:
            intent = new Intent(this, AMap3DMapActivity.class);
            break;
        case R.id.openGaodeRoute:
            intent = new Intent(this, RouteActivity.class);
            break;
        case R.id.openGaodeGPSLocation:
            intent = new Intent(this, LocationGPSActivity.class);
            break;
        case R.id.openGaodePOIAroundSearch:
            intent = new Intent(this, PoiAroundSearchActivity.class);
            break;
        case R.id.openGaodePOIKeywordSearch:
            intent = new Intent(this, PoiKeywordSearchActivity.class);
            break;
        case R.id.openGaodeRouteSearch:
            intent = new Intent(this, RouteSearchActivity.class);
            break;
        default:
            Toast.makeText(AMapActivity.this, "Unrecongnized view id " + v.getId(), Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

}
