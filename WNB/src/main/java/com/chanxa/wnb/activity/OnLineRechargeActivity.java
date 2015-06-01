package com.chanxa.wnb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.dao.UserDAO;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

public class OnLineRechargeActivity extends DefaultTitleActivity implements View.OnClickListener {

    private ViewGroup container_unionPay, container_alipay;
    private EditText edit_onlineRechargeMoney;
    public static final int UNIONPAY = 1;
    public static final int ALIPAY = 2;
    private int selectType = UNIONPAY;
    //private boolean first = true;
    private int rechargeRequertCode = 888;
    private TextView tv_onlineCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_line_recharge);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.onLineRecharge);
    }

    @Override
    public void initView() {
        super.initView();
        edit_onlineRechargeMoney = (EditText) findViewById(R.id.edit_onlineRechargeMoney);
        container_unionPay = (ViewGroup) findViewById(R.id.container_unionPay);
        container_alipay = (ViewGroup) findViewById(R.id.container_alipay);
        container_unionPay.setOnClickListener(this);
        container_alipay.setOnClickListener(this);
        findViewById(R.id.onLineRootLayout).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        container_unionPay.setSelected(true);

        tv_onlineCardNumber = (TextView) findViewById(R.id.tv_onlineCardNumber);
        tv_onlineCardNumber.setText(getCard().getCardNumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_unionPay:
            case R.id.container_alipay:
                selectRecharge(v.getId());
                break;
            case R.id.btn_ok:
                doRecharge();
                break;
            case R.id.onLineRootLayout:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    private void doRecharge() {
        if (selectType == 0) {
            ViewInject.toast("请选择充值类型");
            return;
        }
        if (StringUtils.isEmpty(edit_onlineRechargeMoney.getText().toString())) {
            ViewInject.toast("请输入金额");
            return;
        }
        User user = null;
        try {
            user = UserDAO.read(UserDAO.TAG.MEMBERTAG,this);
            Intent intent = new Intent(this, OnLineRechargeWebActivity.class);
            intent.putExtra("number", getCard().getCardNumber());
            intent.putExtra("pwd",user.getPwd());
            intent.putExtra("money", edit_onlineRechargeMoney.getText().toString());
            intent.putExtra("type", selectType);
            startActivityForResult(intent, rechargeRequertCode);
        } catch (Exception e) {
           ViewInject.toast("获取密码失败");
        }
    }

    private void selectRecharge(int id) {
        if (id == R.id.container_unionPay) {
            container_unionPay.setSelected(true);
            container_alipay.setSelected(false);
            selectType = UNIONPAY;
        } else {
            container_unionPay.setSelected(false);
            container_alipay.setSelected(true);
            selectType = ALIPAY;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode == rechargeRequertCode) {
                finish();
            }
        }
    }
}