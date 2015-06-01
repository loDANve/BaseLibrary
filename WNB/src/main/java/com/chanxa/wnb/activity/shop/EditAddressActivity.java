package com.chanxa.wnb.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.wtm.library.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditAddressActivity extends WnbBaseActivity {

    @InjectView(R.id.search_back)
    ImageButton mSearchBack;
    @InjectView(R.id.edit_name)
    EditText mEditName;
    @InjectView(R.id.edit_phoneNumber)
    EditText mEditPhoneNumber;
    @InjectView(R.id.edit_address)
    EditText mEditAddress;
    @InjectView(R.id.btn_saveReceiveData)
    Button mBtnSaveReceiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        mEditName.setText(name);
        mEditPhoneNumber.setText(phone);
        mEditAddress.setText(address);
        mSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSaveReceiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", mEditName.getText().toString());
                intent.putExtra("phone", mEditPhoneNumber.getText().toString());
                intent.putExtra("address", mEditAddress.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        super.initView();
    }
}