package com.taobao.tae.sdk.demo.ui.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.opentrade.OpenTradeService;
import com.alibaba.sdk.android.opentrade.callback.CreateOrderCallback;
import com.alibaba.sdk.android.opentrade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.opentrade.callback.TradeResult;
import com.alibaba.sdk.android.util.JSONUtils;
import com.taobao.tae.sdk.demo.R;
import com.taobao.tae.sdk.demo.amap.utils.ToastUtil;

/**
 * Created by jade on 15/3/31.
 */
public class OpenTradeActivity extends Activity {
    private static final String TAG = "AliSDKDemo";

    private EditText editIsvOrderId;
    private EditText editItemId;
    private EditText editItemTitle;
    private EditText editAmount;
    private EditText editData;

    private EditText editOrderId;

    private EditText editOutPayId;
    private EditText editPayAmount;
    private EditText editPayTimeout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_trade);
        editIsvOrderId = (EditText)findViewById(R.id.text_isvOrderId);
        editItemId = (EditText)findViewById(R.id.text_itemId);
        editItemTitle = (EditText)findViewById(R.id.text_itemTitle);
        editAmount = (EditText)findViewById(R.id.text_amount);
        editData = (EditText)findViewById(R.id.text_data);

        editOrderId = (EditText)findViewById(R.id.text_orderId);

        editOutPayId = (EditText)findViewById(R.id.text_outPayId);
        editPayAmount = (EditText)findViewById(R.id.text_pay_amount);
        editPayTimeout = (EditText)findViewById(R.id.text_pay_timeout);
    }

    public void createOrder(View view){
        OpenTradeService openTradeService = AlibabaSDK.getService(OpenTradeService.class);
        try {
            openTradeService.createOrder(this
                    , editIsvOrderId.getText().toString()
                    , TextUtils.isEmpty(editItemId.getText().toString())?null:Long.valueOf(editItemId.getText().toString())
                    , editItemTitle.getText().toString()
                    , TextUtils.isEmpty(editAmount.getText().toString())?null:Long.valueOf(editAmount.getText().toString())
                    , TextUtils.isEmpty(editData.getText().toString())?null:JSONUtils.toMap(new JSONObject(editData.getText().toString()))
                    , new CreateOrderCallback() {

                @Override
                public void onSuccess(Long orderId) {
                    editOrderId.setText(orderId.toString());
                    ToastUtil.show(getApplicationContext(), "success: " + orderId);
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtil.show(getApplicationContext(), "error: " + msg);
                }
            });
        }catch (Exception e){
            Log.e(TAG, "error", e);
        }
    }

    public void showPayOrder(View view){
        OpenTradeService openTradeService = AlibabaSDK.getService(OpenTradeService.class);
        openTradeService.showPayOrder(this
                , TextUtils.isEmpty(editOrderId.getText().toString())?null:Long.valueOf(editOrderId.getText().toString())
                , editIsvOrderId.getText().toString()
                , editOutPayId.getText().toString()
                , TextUtils.isEmpty(editPayAmount.getText().toString())?null:Long.valueOf(editPayAmount.getText().toString())
                , editPayTimeout.getText().toString()
                , new TradeProcessCallback() {
            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                ToastUtil.show(OpenTradeActivity.this, "success: " + tradeResult.paySuccessOrders);
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtil.show(OpenTradeActivity.this, "error: " + msg);
            }

        });
    }
}
