package com.chanxa.wnb.activity.staff;

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
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffChangePwdActivity extends DefaultTitleActivity {
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
        new StaffService().changePwd(WnbApplication.getInstance().getStaff(),oldPwd,newPWd,new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("修改失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    String newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    ViewInject.toast("修改成功");
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
}