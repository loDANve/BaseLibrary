package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CCTransferActivity extends DefaultTitleActivity {

    @InjectView(R.id.edit_ccName)
    EditText mEditCcName;
    @InjectView(R.id.edit_ccNumber)
    EditText mEditCcNumber;
    @InjectView(R.id.edit_ccMoney)
    EditText mEditCcMoney;
    @InjectView(R.id.edit_ccPwd)
    EditText mEditCcPwd;
    @InjectView(R.id.btn_ok)
    Button mBtnOk;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctransfer);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this);
        mBtnOk.setOnClickListener(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        String name = mEditCcName.getText().toString();

        String ccNumber = mEditCcNumber.getText().toString();

        String money = mEditCcMoney.getText().toString();

        String pwd = mEditCcPwd.getText().toString();

        if (StringUtils.isEmpty(name)
                || StringUtils.isEmpty(ccNumber)
                || StringUtils.isEmpty(money)
                || StringUtils.isEmpty(pwd)
                ) {
            ViewInject.toast("请输入完整信息");
            return;
        }

        new CardService().ccTransfer(getCardToken(), name, ccNumber, money, "0", pwd, new BaseCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("转账失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    ViewInject.toast("转账成功");
                    //刷新 余额
                    refreshCard();
                } finally {
                    finish();
                }
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    private void refreshCard() {
        new CardService().readCard(getCardToken(), new ServiceCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                //设置全局参数
                WnbApplication.getInstance().setCard((Card) resultObj);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public String initTitleText() {
        return getString(R.string.ccTransfer);
    }
}
