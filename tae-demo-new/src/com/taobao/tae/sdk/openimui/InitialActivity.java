package com.taobao.tae.sdk.openimui;

import com.taobao.tae.sdk.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InitialActivity extends Activity{
	
	public static final int TB_CLIENT = 1;
	public static final int OPEN_CLIENT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_initial);
		super.onCreate(savedInstanceState);
	}
	
	public void tbClient(View v){
		Intent intent = new Intent(InitialActivity.this, OpenImUiActivity.class);
		intent.putExtra("CLIENT", TB_CLIENT);
		startActivity(intent);
	}
	
	public void openClient(View v){
		Intent intent = new Intent(InitialActivity.this, OpenImUiActivity.class);
		intent.putExtra("CLIENT", OPEN_CLIENT);
		startActivity(intent);
	}
}
