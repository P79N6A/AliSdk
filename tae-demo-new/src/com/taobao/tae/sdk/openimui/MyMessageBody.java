package com.taobao.tae.sdk.openimui;

import com.alibaba.mobileim.conversation.YWMessageBody;

/**
 * 自定义消息，可以按照自己的格式定义
 * 其中content是约定的消息简介信息，用于push展现，请遵循该规则
 * @author jiangxiang.jx
 *
 */
public class MyMessageBody extends YWMessageBody {

	private String subContent;
	private String content;

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setSubContent(String content) {
		this.subContent = content;
	}

	public String getSubContent() {
		return subContent;
	}

}
