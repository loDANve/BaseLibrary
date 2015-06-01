package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.MemberService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackActivity extends DefaultTitleActivity {

    @InjectView(R.id.edit_sendMessage)
    EditText mEditSendMessage;
    @InjectView(R.id.btn_sendMessage)
    Button mBtnSendMessage;

    ProgressDialog progressDialog;

    @Override
    public String initTitleText() {
        return getString(R.string.helpFeedBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        mBtnSendMessage.setOnClickListener(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        super.initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String content = mEditSendMessage.getText().toString();
        if (StringUtils.isEmpty(content)) {//发空的不予理会
            return;
        }
        if (v.getId() == R.id.btn_sendMessage) {
            new MemberService().sendMessage(getCardToken(),mEditSendMessage.getText().toString(),new BaseCallBack() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("发送留言失败,请重试");
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                }

                @Override
                public void onEXECSuccess(String result) {
                    ViewInject.toast("发送留言成功");
                    finish();
                }

                @Override
                public void onComplete(String result) {
                    progressDialog.dismiss();
                }
            });
        }
    }
}
