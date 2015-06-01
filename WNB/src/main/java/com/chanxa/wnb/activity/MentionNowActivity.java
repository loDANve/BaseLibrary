package com.chanxa.wnb.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

public class MentionNowActivity extends DefaultTitleActivity {

    private EditText edit_memberMoney, edit_memberPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_mention_now);
    }

    @Override
    public void initView() {
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
        return "账户提现";
    }
}
