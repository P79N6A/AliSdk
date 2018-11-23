package com.taobao.tae.sdk.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ResultCode;
import com.alibaba.sdk.android.trade.OrderService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.OrderItem;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.taobao.tae.sdk.demo.util.StringUtils;

public class OrderActivity extends Activity {

    int index;
	private RadioGroup type;
	boolean one =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        index = getIntent().getExtras().getInt("Index");
        type = (RadioGroup) findViewById(R.id.type);
		type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.one) {
					one=true;
				} else
					one=false;

			}
		});
    }

    public void showOrder(View view) {

    	String[] str=EnvData.ItemID[index].split(",");
    	
        TextView textView = (TextView) this.findViewById(R.id.orderInputData);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[2];
        String[] inputDatas2 = new String[2];
        if (StringUtils.isEmpty(inputData)) {
            inputDatas[0] =  str[0];        	
            inputDatas2[1]=(String) EnvData.skuid[index];
            inputDatas2[0]=str[2];
            inputDatas2[1]=(String) EnvData.skuid[index];
        }

        else {   	
        	
            inputDatas = inputData.split(",");
            Pattern p = Pattern.compile("[0-9]*"); 
			if (inputDatas .length<2||!p.matcher(inputDatas[0]).matches()||!p.matcher(inputDatas[1]).matches()) {
                Toast.makeText(OrderActivity.this, "input format err", Toast.LENGTH_LONG).show();
                return;
            }
        	
        }
        OrderItem orderItem = new OrderItem();
        orderItem.itemId = inputDatas[0];
        orderItem.quantity = 1;
        orderItem.skuId = inputDatas[1];
        
        OrderItem orderItem2 = new OrderItem();
        orderItem2.itemId = inputDatas2[0];
        orderItem2.quantity = 1;
        orderItem2.skuId = inputDatas2[1];
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(orderItem);
        if(!one)
        	orderItems.add(orderItem2);
        AlibabaSDK.getService(OrderService.class).showOrder(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(OrderActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "交易异常" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, orderItems);
    }

    public void showTaokeOrder(View view) {
    	String[] str=EnvData.ItemID[index].split(",");
        TextView textView = (TextView) this.findViewById(R.id.orderInputData);
        String inputData = textView.getText().toString();
        String[] inputDatas2 = new String[2];
        String[] inputDatas = new String[2];
        if (StringUtils.isEmpty(inputData)) {
        	inputDatas[0] =  str[0];
            inputDatas[1] = (String) EnvData.Pid[index];
            
        }

        else {
            inputDatas = inputData.split(",");
            Pattern p = Pattern.compile("[0-9]*"); 
			if (inputDatas .length<2||!p.matcher(inputDatas[0]).matches()||!p.matcher(inputDatas[1]).matches()) {
                Toast.makeText(OrderActivity.this, "input format err", Toast.LENGTH_LONG).show();
                return;
            }
        }
        OrderItem orderItem = new OrderItem();
        orderItem.itemId = inputDatas[0];
        orderItem.quantity = 1;
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(orderItem);
       
        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = inputDatas[1];
        AlibabaSDK.getService(OrderService.class).showTaokeOrder(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(OrderActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "交易异常" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, orderItem, taokeParams);
    }

    public void showOrderWithSKU(View view) {
    	String[] str=EnvData.ItemID[index].split(",");
        TextView textView = (TextView) this.findViewById(R.id.orderInputData);
        String inputData = textView.getText().toString();
        if (StringUtils.isEmpty(inputData)) {
            inputData = str[0];
        }
        else{
        	Pattern p = Pattern.compile("[0-9]*"); 
			if (!p.matcher(inputData).matches()) {
				return;
			}
        }

        AlibabaSDK.getService(OrderService.class).showOrderWithSKU(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(OrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(OrderActivity.this, "下单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "交易异常" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, null, inputData, null);
    }

    public void showKaoKeOrderWithSKU(View view) {
    	String[] str=EnvData.ItemID[index].split(",");
        TextView textView = (TextView) this.findViewById(R.id.orderInputData);
        String inputData = textView.getText().toString();
        String[] inputDatas = new String[2];
        if (StringUtils.isEmpty(inputData)) {
            inputDatas[0] = str[0];
            inputDatas[1] = (String) EnvData.Pid[index];

        }

        else {
            inputDatas = inputData.split(",");

            Pattern p = Pattern.compile("[0-9]*"); 
			if (inputDatas .length<2||!p.matcher(inputDatas[0]).matches()||!p.matcher(inputDatas[1]).matches()) {
                Toast.makeText(OrderActivity.this, "input format err", Toast.LENGTH_LONG).show();
                return;
            }
        }

        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = inputDatas[1];
        if (inputDatas.length > 2) {
            taokeParams.unionId = inputDatas[2];
        }
        if (inputDatas.length > 3) {
            taokeParams.subPid = inputDatas[3];
        }

        AlibabaSDK.getService(OrderService.class).showTaoKeOrderWithSKU(this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(OrderActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "交易异常" + code, Toast.LENGTH_SHORT).show();
                }
            }
        }, null, inputDatas[0], null, taokeParams);
    }
}
