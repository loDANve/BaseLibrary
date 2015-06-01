package com.chanxa.wnb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RadioButton;

import com.chanxa.wnb.R;

/**
 * Created by CHANXA on 2015/1/21.
 */
public class AskRadioButton extends RadioButton{

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public AskRadioButton(Context context) {
        super(context);
    }

    public AskRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AskRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AskRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void initView() {
        Button button = new Button(getContext());
        button.setBackgroundResource(R.drawable.select_bg_answer);
    }
}
