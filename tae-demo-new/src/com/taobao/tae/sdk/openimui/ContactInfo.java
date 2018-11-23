package com.taobao.tae.sdk.openimui;

import com.alibaba.mobileim.contact.IYWContact;

public class ContactInfo implements IYWContact{
	
	public String getmAvatarPath() {
		return mAvatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.mAvatarPath = avatarPath;
	}

	public void setNickName(String mName) {
		this.mName = mName;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	private String mAvatarPath;
	private String mName;
	private String mId;
	private String mAppKey;

	@Override
	public String getAvatarPath() {
		// TODO Auto-generated method stub
		return mAvatarPath;
	}

	@Override
	public String getShowName() {
		// TODO Auto-generated method stub
		return mName;
	}

	@Override
	public String getAppKey() {
		// TODO Auto-generated method stub
		return mAppKey;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return mId;
	}

}
