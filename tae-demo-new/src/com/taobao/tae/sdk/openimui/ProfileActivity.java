package com.taobao.tae.sdk.openimui;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.taobao.tae.sdk.demo.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	private ByteArrayOutputStream mResult = new ByteArrayOutputStream();// 下载的数据
	protected final static String EXTRA_USERID = "extraUserId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		String userId = getIntent().getStringExtra(EXTRA_USERID);
		final IYWContact contact = ContactFactory.getContact(userId);
		if (contact == null) {
			finish();
			return;
		}
		TextView nameView = (TextView) findViewById(R.id.name);
		nameView.setText(contact.getShowName());
		TextView idView = (TextView) findViewById(R.id.id);
		idView.setText(contact.getUserId());
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				byte[] data = internalRequestResource(contact.getAvatarPath());
				if(data == null){
					return null;
				}
				return BitmapFactory.decodeByteArray(data, 0, data.length);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				ImageView imageView = (ImageView) findViewById(R.id.icon);
				imageView.setImageBitmap(result);
			}

		}.execute();
	}

	private byte[] internalRequestResource(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		long startTime = SystemClock.elapsedRealtime();

		int errcode = 0;

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);// 读取超时

		HttpResponse response = null;
		InputStream in = null;
		try {
			if (httpClient == null) {
				return null;
			}
			HttpGet request = new HttpGet(url.trim());
			request.setHeader("Accept-encoding", "gzip");

			response = httpClient.execute(request);
			final int rspStatusCode = response.getStatusLine().getStatusCode();

			if (rspStatusCode == HttpStatus.SC_OK
					|| rspStatusCode == HttpStatus.SC_PARTIAL_CONTENT) {
				float totalSize = 0;

				HttpEntity httpEntity = response.getEntity();
				in = httpEntity.getContent();

				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					in = new GZIPInputStream(new BufferedInputStream(in));
				}

				byte[] buffer = null;
				buffer = new byte[1024];
				int len = 0;
				// 量化提醒次数，分成20.0次提醒
				final int interval = (int) (totalSize / 20.0);
				int intervalLen = 0;
				while ((len = in.read(buffer)) != -1) {
					intervalLen += len;
					mResult.write(buffer, 0, len);
				}
				// 未获取到contentlength， 并且有回调监听
				mResult.close();
				return mResult.toByteArray();
			}
			errcode = rspStatusCode;
		} catch (ClientProtocolException e) {
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} catch (IOException e) {
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} catch (IllegalArgumentException e) {
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} catch (Exception e) {
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} catch (OutOfMemoryError e) {
			System.gc();
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} catch (java.lang.AssertionError e) {
			errcode = IWxCallback.ERROR_NETWORK_ERROR;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			try {
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();
					httpClient = null;
				}
			} catch (java.lang.AssertionError e) {
			}

			long duration = SystemClock.elapsedRealtime() - startTime;
			String urlForMonitor = url;
			int index = urlForMonitor.indexOf("?");
			if (index > 0) {
				urlForMonitor = urlForMonitor.substring(0, index);
			}
		}
		return null;
	}

}
