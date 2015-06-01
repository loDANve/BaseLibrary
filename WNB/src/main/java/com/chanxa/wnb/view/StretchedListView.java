package com.chanxa.wnb.view;


import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wtm.library.utils.DeviceUtils;

public class StretchedListView extends LinearLayout {

	private final DataSetObserver dataSetObserver;
	private ListAdapter adapter;
	private OnItemClickListener onItemClickListener;

	public StretchedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		this.dataSetObserver = new DataSetObserver() {
			@Override
			public void onChanged() {
				syncDataFromAdapter();
				super.onChanged();
			}

			@Override
			public void onInvalidated() {
				syncDataFromAdapter();
				super.onInvalidated();
			}
		};
	}

	public void setAdapter(ListAdapter adapter) {
		ensureDataSetObserverIsUnregistered();

		this.adapter = adapter;
		if (this.adapter != null) {
			this.adapter.registerDataSetObserver(dataSetObserver);
		}
		syncDataFromAdapter();
	}

	protected void ensureDataSetObserverIsUnregistered() {
		if (this.adapter != null) {
			this.adapter.unregisterDataSetObserver(dataSetObserver);
		}
	}

	public Object getItemAtPosition(int position) {
		return adapter != null ? adapter.getItem(position) : null;
	}

	public void setSelection(int i) {
		getChildAt(i).setSelected(true);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public ListAdapter getAdapter() {
		return adapter;
	}

	public int getCount() {
		return adapter != null ? adapter.getCount() : 0;
	}

	private void syncDataFromAdapter() {
		removeAllViews();
		if (adapter != null) {
			int count = adapter.getCount();
			for (int i = 0; i < count; i++) {
				View view = adapter.getView(i, null, this);
				boolean enabled = adapter.isEnabled(i);
				if (enabled) {
					final int position = i;
					final long id = adapter.getItemId(position);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (onItemClickListener != null) {
								onItemClickListener.onItemClick(null, v,
										position, id);
							}
						}
					});
				}
				addView(view);
			/*	TextView textViewLine = new TextView(getContext());
				textViewLine.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, DeviceUtils.dip2px(
                        getContext(), 1)));
				textViewLine.setBackgroundColor(0x00e3e3e3);
				addView(textViewLine);*/
			}
		}
	}
}