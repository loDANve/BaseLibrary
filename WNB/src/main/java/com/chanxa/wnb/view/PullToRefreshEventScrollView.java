package com.chanxa.wnb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.R;
public class PullToRefreshEventScrollView extends PullToRefreshScrollView {

	public PullToRefreshEventScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshEventScrollView(
			Context context,
			Mode mode,
			AnimationStyle style) {
		super(context, mode, style);
	}

	public PullToRefreshEventScrollView(Context context,
			Mode mode) {
		super(context, mode);
	}

	public PullToRefreshEventScrollView(Context context) {
		super(context);
	}

	@Override
	protected ScrollView createRefreshableView(Context context,
			AttributeSet attrs) {
		ObservableScrollView scrollView;
		scrollView = new ObservableScrollView(context, attrs);
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

}
