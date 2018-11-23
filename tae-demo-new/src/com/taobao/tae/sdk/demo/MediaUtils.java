package com.taobao.tae.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 
 * @Description TODO
 * @author yulong.jyl@alibaba-inc.com <br/>
 * @date 2015-3-24 下午4:01:47<br/>
 * 
 */
public class MediaUtils {

	public static final int REQUEST_IMAGE_CODE = 1;// 选择照片
	public static final int REQUEST_CODE_TAKE_VIDEO = 2;// 摄像的照相的requestCode
	public static final int RESULT_CAPTURE_RECORDER_SOUND = 3;// 录音的requestCode

	/**
	 * 选择照片
	 */
	public static void selectPhotos(Activity activity) {
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType("image/*");
		activity.startActivityForResult(getAlbum, REQUEST_IMAGE_CODE);
	}

	/**
	 * 拍摄视频
	 */
	public static void videoMethod(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		activity.startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
	}

	/**
	 * 录音功能
	 */
	public void soundRecorderMethod(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/amr");
		activity.startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
	}

	public static void startPlayVideo(String url, Activity activity) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("video/*");
		intent.setDataAndType(Uri.parse(url), "video/*");
		activity.startActivity(intent);
	}

	public static String onActivityResult(int requestCode, Intent data,
			Activity activity) {
		switch (requestCode) {
		case MediaUtils.REQUEST_IMAGE_CODE:
		case MediaUtils.REQUEST_CODE_TAKE_VIDEO:
		case MediaUtils.RESULT_CAPTURE_RECORDER_SOUND:
			return MediaUtils.getFilePath(data, activity);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static String getFilePath(Intent data, Activity activity) {
		String filePath = null;
		if (data != null) {
			Uri originalUri = data.getData();
			Cursor cursor = null;
			try {
				cursor = activity.managedQuery(originalUri,
						new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				if (cursor != null && cursor.getCount() > 0
						&& cursor.moveToFirst()) {
					filePath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
}
