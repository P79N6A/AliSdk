package com.taobao.tae.sdk.openimui;

import com.alibaba.tcms.env.YWEnvManager;
import com.alibaba.tcms.env.YWEnvType;
import com.taobao.tae.sdk.demo.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class OpenimInfoInitActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
	
	public static final String LOCAL_STORE_NAME="OPENIM_INFO";
	public static final String APPKEYS="appkeys";
	public static final String APPKEY="appkey";
	public static final String USERID="userid";
	public static final String PASSWORD="password";
	public static final String TRAGET_ID="targetid";
	public static final String TARGET_APPKEY="targetappkey";
	public static final String ENVIROMENT="environment";
	private EditText mAppKeys;
	private EditText mAppKey;
	private EditText mUserId;
	private EditText mPassWord;
	private EditText mTargetId;
	private EditText mTargetAppkey;
	private RadioGroup enviroments;
	private RadioButton test;
	private RadioButton pre;
	private RadioButton online;
	
	private Button save;
	
	private SharedPreferences preferences;
	private YWEnvType currentEnvironment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_openim_info_init);
		
		preferences=getApplication().getSharedPreferences(LOCAL_STORE_NAME, Context.MODE_PRIVATE);
		mAppKeys = (EditText) findViewById(R.id.appKeys);
		mAppKey = (EditText) findViewById(R.id.appKey);
		mUserId = (EditText) findViewById(R.id.username);
		mPassWord = (EditText) findViewById(R.id.password);
		mTargetId = (EditText) findViewById(R.id.target_username);
		mTargetAppkey = (EditText) findViewById(R.id.target_appkey);
		enviroments=(RadioGroup) findViewById(R.id.environment);
		enviroments.setOnCheckedChangeListener(this);
		
		test=(RadioButton) findViewById(R.id.test);
		pre=(RadioButton) findViewById(R.id.pre);
		online=(RadioButton) findViewById(R.id.online);
		save =(Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		initDefaultValues();
		initFromLocal();
	}
	
	/**
	 * 从本地读取
	 */
	private void initFromLocal() {
		mAppKeys.setText(preferences.getString(APPKEYS, "23015524 4272"));
		mAppKey.setText(preferences.getString(APPKEY, "23015524"));
		mUserId.setText(preferences.getString(USERID, "testpro1"));
		mPassWord.setText(preferences.getString(PASSWORD, "taobao1234"));
		mTargetId.setText(preferences.getString(TRAGET_ID, "yaochen002"));
		mTargetAppkey.setText(preferences.getString(TARGET_APPKEY, "23015524"));
		if(preferences.getString(ENVIROMENT, YWEnvManager.getEnv().name()).equalsIgnoreCase("test")){
			currentEnvironment=YWEnvType.TEST;
			test.setChecked(true);
		}else if(preferences.getString(ENVIROMENT, YWEnvManager.getEnv().name()).equalsIgnoreCase("pre")){
			currentEnvironment=YWEnvType.PRE;
			pre.setChecked(true);
		}else if(preferences.getString(ENVIROMENT, YWEnvManager.getEnv().name()).equalsIgnoreCase("online")){
			online.setChecked(true);
			currentEnvironment=YWEnvType.ONLINE;
		}else{
			currentEnvironment=YWEnvManager.getEnv();
		}
	}


	/**
	 * 默认值
	 */
	private void initDefaultValues() {
		mAppKeys.setText("23015524 4272");
		mAppKey.setText("23015524");
		mUserId.setText("testpro1");
		mPassWord.setText("taobao1234");
		mTargetId.setText("yaochen002");
		mTargetAppkey.setText("23015524");
	}

	/**
	 * 获取本地保存的环境、默认TEST
	 * @param string
	 * @return
	 */
	public static YWEnvType getYWEnvTypeFromLocal(String string){
		if(string.equalsIgnoreCase("test")){
			return YWEnvType.TEST;
		}else if(string.equalsIgnoreCase("pre")){
			return YWEnvType.PRE;
		}else if(string.equalsIgnoreCase("online")){
			return YWEnvType.ONLINE;
		}else{
			return YWEnvType.TEST;
		}
	}

	@Override
	public void onClick(View v) {
		SharedPreferences.Editor editor =preferences.edit();
		editor.putString(APPKEYS, mAppKeys.getText().toString());
		editor.putString(APPKEY, mAppKey.getText().toString());
		editor.putString(USERID, mUserId.getText().toString());
		editor.putString(PASSWORD, mPassWord.getText().toString());
		editor.putString(TRAGET_ID,mTargetId.getText().toString());
		editor.putString(TARGET_APPKEY, mTargetAppkey.getText().toString());
		editor.putString(ENVIROMENT, currentEnvironment.name());
		editor.apply();
		Notification.showToastMsg(getApplicationContext(), "保存成功,杀掉进程后生效");
		finish();
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group.getId()==R.id.environment) {
			switch (checkedId) {
			case R.id.online:
				currentEnvironment=YWEnvType.ONLINE;
				break;
			case R.id.pre:
				currentEnvironment=YWEnvType.PRE;
				break;
			case R.id.test:
				currentEnvironment=YWEnvType.TEST;
				break;

			default:
				break;
			}
			
		}
		
	}
}
