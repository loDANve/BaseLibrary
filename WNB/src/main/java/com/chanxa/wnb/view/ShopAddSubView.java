package com.chanxa.wnb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chanxa.wnb.R;

/**
 * Created by CHANXA on 2014/12/25.
 */
public class ShopAddSubView extends LinearLayout implements View.OnClickListener {
    private EditText editText;
    private OnNumberChangeListener onNumberChangeListener;
    public ShopAddSubView(Context context) {
        super(context);
        initView();
    }

    public ShopAddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ShopAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ShopAddSubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.shop_add_sub, null, false);
        view.findViewById(R.id.img_sub).setOnClickListener(this);
        view.findViewById(R.id.img_add).setOnClickListener(this);
        editText = (EditText) view.findViewById(R.id.edit_number);
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_sub:
                if (getNumber() > 1) {
                    editText.setText(getNumber() - 1 + "");
                    if (onNumberChangeListener != null) {
                        onNumberChangeListener.onNumberChange(getNumber());
                    }
                }
                break;
            case R.id.img_add:
                if (getNumber() < 99) {
                    editText.setText(getNumber() + 1 + "");
                    if (onNumberChangeListener != null) {
                        onNumberChangeListener.onNumberChange(getNumber());
                    }
                }
                break;
        }
    }
    public interface OnNumberChangeListener {
        void onNumberChange(int count);
    }

    public void setNumber(int number) {
        if (number < 1 || number > 99) {
            return;
        }
        editText.setText(number+"");
    }
    public OnNumberChangeListener getOnNumberChangeListener() {
        return onNumberChangeListener;
    }

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

    public int getNumber() {
        return Integer.parseInt(editText.getText().toString());
    }
}
