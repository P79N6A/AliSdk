package com.taobao.tae.sdk.openimui;

import com.alibaba.mobileim.YWAccount;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWSDK;
import com.alibaba.mobileim.YWUIAPI;
import com.alibaba.mobileim.conversation.YWConversationManager;
import com.alibaba.mobileim.conversation.YWFileManager;
import com.alibaba.mobileim.tribe.YWTribeManager;


/**
 * @author mayongge
 * 创建一个单例类，openim的对象都从该类获取，从而保证每个账号对应一个YWConversationManager。
 */
public class OpenWxApi {
	public static OpenWxApi mOpenWxApi;
	YWUIAPI mUIApi;
	private YWAccount mWangxinApi;  //openim API接口
	private String mCurrUserId;
	private String mCurrConversationId;
	private static int mClientType;
	
	private OpenWxApi(){
		mWangxinApi = getUIApi().getAccount();
	}
	
	public static OpenWxApi getInstance(){
		if (mOpenWxApi == null) {
			synchronized (OpenWxApi.class) {
				if (mOpenWxApi == null)
					mOpenWxApi = new OpenWxApi();
			}
		}
		return mOpenWxApi;
	}
	
	public static void setClientType(int type){
		mClientType = type;
	}
	
	public YWAccount getApi(){
		return mWangxinApi;
	}
	
	public YWUIAPI getUIApi(){
//		if (mClientType == InitialActivity.TB_CLIENT) {
//			mUIApi = YWSDK.getTBUIAPI();
//		}else {
//			mUIApi = YWSDK.getOpenUIAPI();
//		}
		mUIApi = YWSDK.getOpenUIAPI();
		return mUIApi;
	}
	
	public YWConversationManager getConversationManager(){
		return mWangxinApi.getConversationManager();
	}
	
	public YWFileManager getFileManager(){
		return mWangxinApi.getFileManager();
	}
	
	public void setCurrConversationId(String conversationId){
		mCurrConversationId = conversationId;
	}
	
	public String getCurrConversationId(){
		return mCurrConversationId;
	}
	
	public YWTribeManager getTribeManager() {
		return mWangxinApi.getTribeManager();
	}
	
	public void setCurrentUserId(String userId) {
		mCurrUserId = userId;
	}

	public String getCurrentId() {
		return mCurrUserId;
	}
}
