package com.chanxa.wnb.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
    private ObservableScrollViewCallbacks mCallbacks;
    private int mPrevScrollY;
    private int mScrollY;
    private ScrollState mScrollState;
    private boolean mFirstScroll;
    private boolean mDragging;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        mPrevScrollY = ss.prevScrollY;
        mScrollY = ss.scrollY;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.prevScrollY = mPrevScrollY;
        ss.scrollY = mScrollY;
        return ss;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mScrollY = t;

            mCallbacks.onScrollChanged(t, mFirstScroll, mDragging);
            if (mFirstScroll) {
                mFirstScroll = false;
            }

            if (mPrevScrollY < t) {
                //down
                mScrollState = ScrollState.UP;
            } else if (t < mPrevScrollY) {
                //up
                mScrollState = ScrollState.DOWN;
                //} else {
                // Keep previous state while dragging.
                // Never makes it STOP even if scrollY not changed.
                // Before Android 4.4, onTouchEvent calls onScrollChanged directly for ACTION_MOVE,
                // which makes mScrollState always STOP when onUpOrCancelMotionEvent is called.
                // STOP state is now meaningless for ScrollView.
            }
            mPrevScrollY = t;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstScroll = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        mCallbacks = listener;
    }

    public int getCurrentScrollY() {
        return mScrollY;
    }

    static class SavedState extends BaseSavedState {
        int prevScrollY;
        int scrollY;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            prevScrollY = in.readInt();
            scrollY = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(prevScrollY);
            out.writeInt(scrollY);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
