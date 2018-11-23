package com.taobao.tae.sdk.demo;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ResultCode;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.PromotionService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.webview.UiSettings;
import com.taobao.tae.sdk.demo.util.StringUtils;

public class ItemActivity extends Activity {
	private static final String[] itemIds = { "2100502266765", "37196464781", "2100502166202", "2100502146518" };
	int index;
	TextView textView;
	AutoCompleteTextView actv;

	RadioGroup type;
	int col = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		index = getIntent().getExtras().getInt("Index");

		type = (RadioGroup) findViewById(R.id.type);
		type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.tao) {
					col = 0;
				} else
					col = 2;

			}
		});
		textView = (TextView) this.findViewById(R.id.itemInputData);

		actv = (AutoCompleteTextView) this.findViewById(R.id.itemInputData2);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_list, itemIds);
		// 启用监听器
		actv.setAdapter(adapter);
		actv.setText(itemIds[0]);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void showItemDetail(View view) {

		String[] str = EnvData.ItemID[index].split(",");
		String inputData = textView.getText().toString();
		String[] inputDatas = new String[2];
		if (StringUtils.isEmpty(inputData)) {
			inputDatas[0] = str[col];
			inputDatas[1] = str[col + 1];

		} else {

			inputDatas = inputData.split(",");
			Pattern p = Pattern.compile("[0-9]*");
			if (inputDatas.length < 2 || !p.matcher(inputDatas[0]).matches() || !p.matcher(inputDatas[1]).matches()) {
				Toast.makeText(ItemActivity.this, "input format err", Toast.LENGTH_LONG).show();
				return;
			}
		}

		UiSettings taeWebViewUiSettings = new UiSettings();
		AlibabaSDK.getService(ItemService.class).showItemDetailByOpenItemId(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(ItemActivity.this,
						"支付成功" + tradeResult.paySuccessOrders + " fail:" + tradeResult.payFailedOrders,
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(ItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
				}
			}
		}, taeWebViewUiSettings, inputDatas[0], Integer.parseInt(inputDatas[1]), null);
	}

	public void openTaokeDetail(View view) {
		// TaoOpen.startOauth(this, "23015524",
		// "4d0220bc382eb628b612956fb8edeb00");
		String[] str = EnvData.ItemID[index].split(",");
		String inputData = textView.getText().toString();
		String[] inputDatas = new String[3];
		if (StringUtils.isEmpty(inputData)) {
			inputDatas[0] = str[col];
			inputDatas[1] = str[col + 1];
			inputDatas[2] = (String) EnvData.Pid[index];

		} else {

			inputDatas = inputData.split(",");
			if (inputDatas == null) {
				Toast.makeText(ItemActivity.this, "input format err", Toast.LENGTH_LONG).show();
				return;
			}

		}

		TaokeParams taokeParams = new TaokeParams();
		taokeParams.pid = inputDatas[2];
		AlibabaSDK.getService(ItemService.class).showTaokeItemDetailByOpenItemId(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(ItemActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(ItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
				}
			}
		}, null, inputDatas[0], Integer.parseInt(inputDatas[1]), null, taokeParams);
	}

	public void showETicketDetail(View view) {
		AlibabaSDK.getService(PromotionService.class).showETicketDetail(this, 931159680463903l,
				new TradeProcessCallback() {

					@Override
					public void onPaySuccess(TradeResult tradeResult) {
						Toast.makeText(ItemActivity.this,
								"支付成功" + tradeResult.paySuccessOrders + " fail:" + tradeResult.payFailedOrders,
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onFailure(int code, String msg) {
						if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
							Toast.makeText(ItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void showPromotions(View view) {
		AlibabaSDK.getService(ItemService.class).showPage(this, null, null,
				"http://ff.win.taobao.com?des=promotions&cc=tae&sn=c测试账号0515");
		// TaeSDK.showPromotions(this, "shop", "c测试账号0515", new
		// TradeProcessCallback() {
		//
		// @Override
		// public void onPaySuccess(TradeResult tradeResult) {
		// Toast.makeText(ItemActivity.this,
		// "支付成功" + tradeResult.paySuccessOrders + " fail:" +
		// tradeResult.payFailedOrders,
		// Toast.LENGTH_SHORT).show();
		//
		// }
		//
		// @Override
		// public void onFailure(int code, String msg) {
		// if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
		// Toast.makeText(ItemActivity.this, "确认交易订单失败",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(ItemActivity.this, "交易取消" + code,
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// });
	}

	public void showItemDetailV2(View view) {
		String inputData = "41576306115";// ctv.getText().toString();
		AlibabaSDK.getService(ItemService.class).showItemDetailByItemId(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(ItemActivity.this,
						"支付成功" + tradeResult.paySuccessOrders + " fail:" + tradeResult.payFailedOrders,
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(ItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
				}
			}
		}, null, Long.valueOf(inputData), 1, null);
		// }, null, 37196464781l, 1, null);
	}

	public void showTaokeItemDetailV2(View view) {
		String inputData = "22429824161";// actv.getText().toString();
		TaokeParams taokeParams = new TaokeParams();
		taokeParams.pid = "mm_26632322_6858406_23810104";
		AlibabaSDK.getService(ItemService.class).showTaokeItemDetailByItemId(this, new TradeProcessCallback() {

			@Override
			public void onPaySuccess(TradeResult tradeResult) {
				Toast.makeText(ItemActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int code, String msg) {
				if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
					Toast.makeText(ItemActivity.this, "确认交易订单失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ItemActivity.this, "交易取消" + code, Toast.LENGTH_SHORT).show();
				}
			}
		}, null, Long.valueOf(inputData), 1, null, taokeParams);
	}

	public void showGlobalItem(View view) {
		Intent intent = new Intent(ItemActivity.this, GlobalItemActivity.class);
		intent.putExtra("Index", index);
		startActivity(intent);
	}

}
