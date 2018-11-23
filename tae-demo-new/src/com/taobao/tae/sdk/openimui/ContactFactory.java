package com.taobao.tae.sdk.openimui;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.mobileim.contact.IYWContact;


public class ContactFactory {
	private static Map<String, ContactInfo> mContact = new HashMap<String, ContactInfo>();
	static{
		ContactInfo info1 = new ContactInfo();
		info1.setAvatarPath("http://images1.jiayuan.com/w4/global/cms/uploadfile/2012/1012/20121012023753480.jpg");
		info1.setNickName("药尘001");
		mContact.put("yaochen001", info1);

		ContactInfo info2 = new ContactInfo();
		info2.setAvatarPath("http://image.zcool.com.cn/2013/59/52/1389172852004.jpg");
		info2.setNickName("药尘002");
		mContact.put("yaochen002", info2);

		ContactInfo info3 = new ContactInfo();
		info3.setAvatarPath("http://image.zcool.com.cn/2009/06/10/73/1246331499328.jpg");
		info3.setNickName("药尘003");
		mContact.put("yaochen003", info3);

		ContactInfo info4 = new ContactInfo();
		info4.setAvatarPath("http://image.zcool.com.cn/2012/07/71/98/1342244401316.jpg");
		info4.setNickName("药尘004");
		mContact.put("yaochen004", info4);

		ContactInfo info5 = new ContactInfo();
		info5.setAvatarPath("http://image.zcool.com.cn/2013/43/47/1389440406843.jpg");
		info5.setNickName("药尘005");
		mContact.put("yaochen005", info5);

		ContactInfo info6 = new ContactInfo();
		info6.setAvatarPath("http://www.hua.com/Flower_Picture/baihehua/images/l01b.jpg");
		info6.setNickName("药尘006");
		mContact.put("yaochen006", info6);
	
	}
	public static Map<String, ContactInfo> getContacts() {
		return mContact;
	}

	public static IYWContact getContact(String userId) {
		final ContactInfo info = mContact.get(userId);
		if (info == null) {
			ContactInfo info6 = new ContactInfo();
			info6.setAvatarPath("http://www.hua.com/Flower_Picture/baihehua/images/l01b.jpg");
			info6.setNickName(userId);
			return info6;
		}
		return info;
	}
	
	
//	public static IWMContact getContact(String userId) {
//		final ContactInfo info = mContact.get(userId);
//		if (info == null) {
//			ContactInfo info6 = new ContactInfo();
//			info6.setAvatarPath("http://www.hua.com/Flower_Picture/baihehua/images/l01b.jpg");
//			info6.setNickName(userId);
//			return info6;
//		}
//		return info;
//	}

}
