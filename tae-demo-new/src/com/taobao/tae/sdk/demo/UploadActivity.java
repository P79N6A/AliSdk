package com.taobao.tae.sdk.demo;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.media.MediaService;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;

/**
 * 
 * @Description 图片上传示例
 * @author yulong.jyl@alibaba-inc.com <br/>
 * @date 2015-3-18 下午8:06:51<br/>
 * 
 */
public class UploadActivity extends Activity implements OnClickListener {

	private String filePath;
	private String uploadTaskId;

	private UploadListener uploadListener;
	private UploadView uploadView;

	private static final String NAMESPACE = "wanhutest";
	private static final String TAG = "UploadActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic);
		uploadView = new UploadView();
		initUpload();

	}

	private final class UploadView {

		TextView path,url;

		UploadView() {
			setUpView();
		}

		void setUpView() {
			findViewById(R.id.select_image).setOnClickListener(
					UploadActivity.this);
			findViewById(R.id.uploadFileButtion).setOnClickListener(
					UploadActivity.this);
			path = (TextView) findViewById(R.id.image_path);

			url = (TextView) findViewById(R.id.resultUrl);
		}

		void reset() {
			path.setText(null);
			filePath = null;
			uploadTaskId = null;
			url.setText(null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_image:
			uploadView.reset();
			MediaUtils.selectPhotos(this);
			break;
		case R.id.uploadFileButtion:
			uploadTaskId = uploadFile(filePath);
			Log.e(TAG, "---uploadTaskId--:" + uploadTaskId);
			break;
		
		}

	}

	private void initUpload() {
		uploadListener = new UploadListener() {
			

			@Override
			public void onUploadFailed(UploadTask uploadTask,
					FailReason failReason) {
				uploadView.url.setText("上传失败:" + failReason.toString());

			}

		

			@Override
			public void onUploadCancelled(UploadTask uploadTask) {
				uploadView.url.setText("----取消上传------");

			}



			@Override
			public void onUploadComplete(UploadTask arg0) {
				// TODO Auto-generated method stub
				uploadView.url.setText("resultUri :  "
						+ arg0.getResult().url);
			}



			@Override
			public void onUploading(UploadTask arg0) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public String uploadFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		UploadOptions options = new UploadOptions.Builder()
				//.userKey(String.valueOf(System.currentTimeMillis()))
				.needRecorder(false).blockSize(4 * 1000 * 1024)
				.aliases("image_" + UUID.randomUUID().toString().replaceAll("-", "")).build();
		return AlibabaSDK.getService(MediaService.class).upload(new File(path),
				NAMESPACE, options, uploadListener);

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == MediaUtils.REQUEST_IMAGE_CODE) {
			uploadView.path.setText("文件路径："
					+ (filePath = MediaUtils.getFilePath(data, this)));
		}
	}

}
