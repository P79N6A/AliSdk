package com.taobao.tae.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ResultCode;
import com.alibaba.sdk.android.globaltrade.GlobalItemService;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.webview.UiSettings;
import com.ta.utdid2.android.utils.StringUtils;

public class GlobalItemActivity extends Activity {

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_item);
        index = getIntent().getExtras().getInt("Index");

    }

    public void openItemDetailGlobal(View view) {
    	String[] str=EnvData.ItemID[index].split(",");
        TextView textView = (TextView) this.findViewById(R.id.itemInputDataGlobal);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[2];
        // Item Id
        inputDatas[0] = str[0];
        // Item Type
        inputDatas[1] = str[1];

        if (!StringUtils.isEmpty(inputData)) {
            String[] newInputDatas = inputData.split(",");

            for (int i = 0; i < newInputDatas.length; ++i) {
                inputDatas[i] = newInputDatas[i];
            }
        }

        UiSettings taeWebViewUiSettings = new UiSettings();
        AlibabaSDK.getService(GlobalItemService.class).showItemDetailByOpenItemId(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(GlobalItemActivity.this,
                        "支付成功" + tradeResult.paySuccessOrders + " fail:" + tradeResult.payFailedOrders,
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(GlobalItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GlobalItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, taeWebViewUiSettings, inputDatas[0], Integer.parseInt(inputDatas[1]), null);
    }


    public void openTaokeDetailGlobal(View view) {
        // TaoOpen.startOauth(this, "23015524",
        // "4d0220bc382eb628b612956fb8edeb00");

    	String[] str=EnvData.ItemID[index].split(",");
        TextView textView = (TextView) this.findViewById(R.id.itemInputDataGlobal);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[3];

        inputDatas[0] = str[0];
        inputDatas[1] = str[1];
        inputDatas[2] = EnvData.Pid[index];

        if (!StringUtils.isEmpty(inputData)) {

            String[] newInputDatas = inputData.split(",");

            for (int i = 0; i < newInputDatas.length; ++i) {
                inputDatas[i] = newInputDatas[i];
            }

        }

        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = inputDatas[2];
        AlibabaSDK.getService(GlobalItemService.class).showTaokeItemDetailByOpenItemId(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(GlobalItemActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(GlobalItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GlobalItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
                }
            }

        }, null, inputDatas[0], Integer.parseInt(inputDatas[1]), null, taokeParams);
    }


    public void showItemDetailGlobal(View view) {

        TextView textView = (TextView) this.findViewById(R.id.itemInputDataGlobalV2);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[1];
        // Item Id
        inputDatas[0] = "44206418777";

        if (!StringUtils.isEmpty(inputData)) {
            inputDatas[0] = inputData;
        }

        AlibabaSDK.getService(GlobalItemService.class).showItemDetailByItemId(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(GlobalItemActivity.this,
                        "支付成功" + tradeResult.paySuccessOrders + " fail:" + tradeResult.payFailedOrders,
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(GlobalItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GlobalItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, null, Long.parseLong(inputDatas[0]), 1, null);
    }



    public void showTaokeItemDetailGlobal(View view) {

        // TaoOpen.startOauth(this, "23015524",
        // "4d0220bc382eb628b612956fb8edeb00");

        TextView textView = (TextView) this.findViewById(R.id.itemInputDataGlobalV2);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[1];

        inputDatas[0] = "44235377131";


        if (!StringUtils.isEmpty(inputData)) {
            inputDatas[0] = inputData;
        }

        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = "mm_26632322_6858406_23810104";
        AlibabaSDK.getService(GlobalItemService.class).showTaokeItemDetailByItemId(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(GlobalItemActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(GlobalItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GlobalItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, null, Long.parseLong(inputDatas[0]), 1, null, taokeParams);
    }

}



