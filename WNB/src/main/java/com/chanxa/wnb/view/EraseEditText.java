package com.chanxa.wnb.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chanxa.wnb.R;

/**
 * Created by wtm on 2015/1/5.
 */
public class EraseEditText extends LinearLayout implements TextWatcher {

    private EditText editText;
    private Button clearBtn;

    public EraseEditText(Context context) {
        super(context);
        initContentView();
    }

    public EraseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContentView();
    }

    public EraseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContentView();
    }

    public EraseEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initContentView();
    }

    private void initContentView() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.erase_edit_text_layout, null, false);
        editText = (EditText) viewGroup.findViewById(R.id.erase_editText);
        clearBtn = (Button) viewGroup.findViewById(R.id.btn_clear);
        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        editText.addTextChangedListener(this);
        this.addView(viewGroup);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText.getText().toString() != null && !editText.getText().toString().equals("")) {
            clearBtn.setVisibility(View.VISIBLE);
        } else {
            clearBtn.setVisibility(View.INVISIBLE);
        }
    }

    public EditText getEditText() {
        return editText;
    }

    public Button getClearBtn() {
        return clearBtn;
    }

    public CharSequence getText(){
        return editText.getText();
    }
    public void setText(CharSequence text){
        editText.setText(text);
    }
}

