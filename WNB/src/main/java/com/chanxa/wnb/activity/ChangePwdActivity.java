package com.chanxa.wnb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChangePwdActivity extends DefaultTitleActivity {

    @InjectView(R.id.btn_title_back)
    Button mBtnTitleBack;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.edit_oldPwd)
    EditText mEditOldPwd;
    @InjectView(R.id.edit_newPwd)
    EditText mEditNewPwd;
    @InjectView(R.id.edit_newPwd2)
    EditText mEditNewPwd2;
    @InjectView(R.id.btn_doChangepwd)
    Button mBtnDoChangepwd;

    @Override
    public String initTitleText() {
        return getString(R.string.changepwd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this);
        mBtnDoChangepwd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        String oldPwd = mEditOldPwd.getText().toString();
        String newPwd = mEditNewPwd.getText().toString();
        String newPwd2 = mEditNewPwd2.getText().toString();
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(newPwd2)) {
            ViewInject.toast("不能为空");
            return;
        }
        if (!newPwd.equals(newPwd2)) {
            ViewInject.toast("二次输入的密码不一致");
            return;
        }
        changePwd(oldPwd, newPwd);
    }


    private void changePwd(String oldPwd,String newPWd) {
        CardToken cardToken = WnbApplication.getInstance().getCardToken();
        if (cardToken == null) {
            return;
        }
        new CardService().changePwd(cardToken,oldPwd,
                newPWd, new BaseAsynCallBack(new BaseCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(Exception e, int stateCode) {
                        LogUtils.d(stateCode + ":" + e);
                        if (stateCode != 200) {
                            ViewInject.toast("网络连接失败,请重试");
                        }
                    }

                    @Override
                    public void onEXECisFalse(String errroMsg) {
                        LogUtils.d(errroMsg);
                        ViewInject.toast(errroMsg);
                    }

                    @Override
                    public void onEXECSuccess(String result) {
                        ViewInject.toast("密码修改成功");
                        finish();
                    }

                    @Override
                    public void onComplete(String result) {
                        LogUtils.d(result);
                    }
                }));
    }

}
