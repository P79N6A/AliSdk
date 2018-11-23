package com.taobao.tae.sdk.openimui.refreshlist;

import java.util.HashSet;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.taobao.tae.sdk.openimui.refreshlist.internal.LoadingLayout;


public class LoadingLayoutProxy implements ILoadingLayout {

	private final HashSet<LoadingLayout> mLoadingLayouts;

	LoadingLayoutProxy() {
		mLoadingLayouts = new HashSet<LoadingLayout>();
	}

	/**
	 * This allows you to add extra LoadingLayout instances to this proxy. This
	 * is only necessary if you keep your own instances, and want to have them
	 * included in any
	 * {@link PullToRefreshBase#createLoadingLayoutProxy(boolean, boolean)
	 * createLoadingLayoutProxy(...)} calls.
	 * 
	 * @param mHeaderLayout - LoadingLayout to have included.
	 */
	public void addLayout(LoadingLayout mHeaderLayout) {
		if (null != mHeaderLayout) {
			mLoadingLayouts.add(mHeaderLayout);
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLastUpdatedLabel(label);
		}
	}

	@Override
	public void setLoadingDrawable(Drawable drawable) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLoadingDrawable(drawable);
		}
	}

	@Override
	public void setRefreshingLabel(CharSequence refreshingLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setRefreshingLabel(refreshingLabel);
		}
	}

	@Override
	public void setPullLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setPullLabel(label);
		}
	}

	@Override
	public void setReleaseLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setReleaseLabel(label);
		}
	}

	public void setTextTypeface(Typeface tf) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextTypeface(tf);
		}
	}
}
