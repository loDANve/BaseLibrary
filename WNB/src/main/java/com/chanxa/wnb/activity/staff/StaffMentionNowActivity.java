package com.chanxa.wnb.activity.staff;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

public class StaffMentionNowActivity extends DefaultTitleActivity {

    private EditText edit_memberMoney, edit_memberPwd;
    private ProgressDialog progressDialog;
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
        progressDialog = ProgressDialogBuilder.builderDialog(this);
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
        //CardToken cardToken = WnbApplication.getInstance().getCardToken();
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
        new StaffService().mentionNow(WnbApplication.getInstance().getStaff(), edit_memberPwd.getText().toString(), Integer.parseInt(edit_memberMoney.getText().toString()), new BaseCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("请求失败,请检查网络");
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
                    String newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    ViewInject.toast("提现成功,请等待工作人员确认");
                    progressDialog.dismiss();
                    finish();
                } catch (DocumentException e) {
                    onError(e,-1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    @Override
    public String initTitleText() {
        return "提现";
    }
}