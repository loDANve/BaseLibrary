package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

public class CashRebateActivity extends DefaultTitleActivity {

    private EditText edit_memberMoney, edit_memberPwd;
    private String rebate = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_cash_rebate);
    }

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        rebate = (String)intent.getExtras().get("rebate");
        edit_memberMoney = (EditText) findViewById(R.id.edit_memberMoney);
        edit_memberPwd = (EditText) findViewById(R.id.edit_memberPwd);
        findViewById(R.id.btn_mentionNow).setOnClickListener(this);
        super.initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_mentionNow) {
            mentionNow();
        }
    }

    private void mentionNow() {
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken != null) {
            if (StringUtils.isEmpty(edit_memberMoney.getText().toString())
                    || StringUtils.isEmpty(edit_memberPwd.getText().toString())) {
                ViewInject.toast("所填信息不能够为空");
                return;
            }
            float money = Float.parseFloat(edit_memberMoney.getText().toString());
            if (money <= 0) {
                ViewInject.toast("提现金额不能少于0");
                return;
            }
            //检查积分/100 = 余额 够不够提取
            /*if (money < Integer.parseInt(rebate)) {
                ViewInject.toast("");
                return;
            }*/
            new CardService().mentionNow(cardToken,edit_memberMoney.getText().toString(),edit_memberPwd.getText().toString(),null,null,new BaseCallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("请求失败,请检查网络");
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                }

                @Override
                public void onEXECSuccess(String result) {
                    ViewInject.toast("提现成功,请等待工作人员确认");
                    finish();
                }

                @Override
                public void onComplete(String result) {

                }
            });
        }
    }

    @Override
    public String initTitleText() {
        return "返利提现";
    }
}
