package com.chanxa.wnb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.service.MemberService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/13.
 */
public class UserRegActivity extends DefaultTitleActivity {

    @InjectView(R.id.edit_shortMessage)
    EditText mEditShortMessage;
    @InjectView(R.id.btn_getShortMessage)
    Button mBtnGetShortMessage;
    @InjectView(R.id.edit_phoneCheckCode)
    EditText mEditPhoneCheckCode;
    @InjectView(R.id.edit_regPassWord)
    EditText mEditRegPassWord;
    @InjectView(R.id.btn_reg)
    Button mBtnReg;
    @InjectView(R.id.edit_recommendNumber)
    EditText mEditRecommendNumber;

    private MemberService memberService;

    private int seconds = 60;

    private String checkCodeMark = null;//验证码标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
    }

    @Override
    public String initTitleText() {
        return getResources().getString(R.string.reg);
    }

    @Override
    public void initData() {
        super.initData();
        memberService = new MemberService();
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this);
        mBtnGetShortMessage.setOnClickListener(this);
        mBtnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_getShortMessage:
                requestShortMessage();
                break;
            case R.id.btn_reg:
                doReg();
                break;
        }
    }

    /**
     * 用户注册
     */
    private void doReg() {
        String phone = mEditShortMessage.getText().toString();
        String phoneCheckCode = mEditPhoneCheckCode.getText().toString();
        String passWord = mEditRegPassWord.getText().toString();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(phoneCheckCode) || StringUtils.isEmpty(passWord) || StringUtils.isEmpty(checkCodeMark)) {
            ViewInject.toast("请填写必要信息!");
            return;
        }
        Pattern phonePattern = Pattern
                .compile("^1[3|4|5|7|8]\\d{9}$");
        if (StringUtils.isEmpty(phone) || !phonePattern.matcher(phone).matches()) {
            ViewInject.toast("请输入正确的手机号码");
            return;
        }
        memberService.regMember(phone, passWord, phoneCheckCode, checkCodeMark, phone, "1", mEditRecommendNumber.getText().toString(), new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + ":" + e);
                ViewInject.toast("注册失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                ViewInject.toast("注册成功");
                finish();
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e("onComplete:" + result);
            }
        });


    }

    public void requestShortMessage() {
        String phone = mEditShortMessage.getText().toString();

        Pattern phonePattern = Pattern
                .compile("^1[3|4|5|7|8]\\d{9}$");

        if (StringUtils.isEmpty(phone) || !phonePattern.matcher(phone).matches()) {
            ViewInject.toast("请输入正确的手机号码");
            return;
        }
        //请求验证码
        memberService.requestRegShortMessage("", phone, new BaseCallBack() {
            @Override
            public void onStart() {
                mBtnGetShortMessage.setClickable(false);
                mBtnGetShortMessage.setText(seconds + "s");
                mBtnGetShortMessage.setClickable(false);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (seconds <= 0) {
                            ShoreSecondsHandler.sendEmptyMessage(-1);
                            this.cancel();
                            return;
                        }
                        ShoreSecondsHandler.sendEmptyMessage(2);
                    }
                }, 0, 1000);
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + ":" + e);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                ViewInject.toast("验证码已经下发至手机");
                try {
                    checkCodeMark = DATAxmlHelper.getAllAttribute(result).get("验证码标识");
                } catch (DocumentException e) {
                    e.printStackTrace();
                    LogUtils.e("onEXECSuccess:" + e);
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.e("onComplete:" + result);
            }
        });
    }


    public Handler ShoreSecondsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seconds--;
            if (msg.what <= 0) {
                mBtnGetShortMessage.setText("验证");
                mBtnGetShortMessage.setClickable(true);
                seconds = 60;
                return;
            }
            mBtnGetShortMessage.setText(seconds + "s");
        }
    };
}
