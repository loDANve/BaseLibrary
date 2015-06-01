package com.chanxa.wnb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.service.CardService;
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

public class ShortMessageVerificationActivity extends DefaultTitleActivity {


    @InjectView(R.id.edit_verCheckCode)
    EditText mEditVerCheckCode;
    @InjectView(R.id.btn_doRequestGetbackPwdCheckCode)
    Button mBtnDoRequestGetbackPwdCheckCode;
    @InjectView(R.id.edit_setPwd1)
    EditText mEditSetPwd1;
    @InjectView(R.id.edit_setPwd2)
    EditText mEditSetPwd2;
    @InjectView(R.id.btn_doSetPwd)
    Button mBtnDoSetPwd;
    @InjectView(R.id.edit_pwdPhone)
    EditText mEditPwdPhone;

    private int seconds = 60;

    private MemberService memberService;

    private String checkCodeMark;//验证码标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_message_verification2);
        ButterKnife.inject(this);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.shortMessageVerification);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this);
        mBtnDoRequestGetbackPwdCheckCode.setOnClickListener(this);
        mBtnDoSetPwd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        memberService = new MemberService();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_doRequestGetbackPwdCheckCode:
                doRequestGetbackPwdCheckCode();
                break;
            case R.id.btn_doSetPwd:
                doSetPwd();
                break;
        }
    }

    //执行重置密码
    private void doSetPwd() {
        String pwd = mEditSetPwd1.getText().toString();
        String pwd2 = mEditSetPwd2.getText().toString();
        String checkCode = mEditVerCheckCode.getText().toString();

        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(pwd2)) {
            ViewInject.toast("密码和二次密码不能为空");
            return;
        }
        if (StringUtils.isEmpty(checkCode)) {
            ViewInject.toast("请输入验证码,从短信中获取");
            return;
        }
        if (!pwd.equals(pwd2)) {
            ViewInject.toast("密码和二次密码不相同");
            return;
        }
        String phone = verificationPhone();
        if (phone == null) {
            return;
        }
        //String cardNumber = WnbApplication.getInstance().getCard().getCardNumber();
        //所有条件都满足,去修改密码
        new CardService().changePwd4ShortMessage(phone, pwd, checkCode, checkCodeMark, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + ":" + e);
                ViewInject.toast("修改密码失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
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
        });
    }

    private String verificationPhone(){
        String phone = mEditPwdPhone.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            ViewInject.toast("手机号不能为空");
            return null;
        }
        Pattern phonePattern = Pattern
                .compile("^1[3|4|5|7|8]\\d{9}$");
        if (!phonePattern.matcher(phone).matches()) {
            ViewInject.toast("请输入正确的手机号码");
            return null;
        }
        return phone;
    }

    //请求发送找回密码的短信
    private void doRequestGetbackPwdCheckCode() {
        String phone = verificationPhone();
        if (phone == null) {
            return;
        }
       // String cardNumber = WnbApplication.getInstance().getCard().getCardNumber();
        memberService.requestChangePwdShortMessage(phone, new BaseCallBack() {
            @Override
            public void onStart() {
                mBtnDoRequestGetbackPwdCheckCode.setClickable(false);
                mBtnDoRequestGetbackPwdCheckCode.setText(seconds + "s");
                mBtnDoRequestGetbackPwdCheckCode.setClickable(false);
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
                ViewInject.toast("获取验证码失败,请重试");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    checkCodeMark = DATAxmlHelper.getAllAttribute(result).get("验证码标识");
                    ViewInject.toast("短信已经下发至手机");
                } catch (DocumentException e) {
                    e.printStackTrace();
                    ViewInject.toast("获取验证码失败,请重试");
                }
            }

            @Override
            public void onComplete(String result) {
                LogUtils.d(result);
            }
        });
    }

    //处理发送短信60秒间隔
    public Handler ShoreSecondsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seconds--;
            if (msg.what <= 0) {
                mBtnDoRequestGetbackPwdCheckCode.setText("获取验证码");
                mBtnDoRequestGetbackPwdCheckCode.setClickable(true);
                seconds = 60;
                return;
            }
            mBtnDoRequestGetbackPwdCheckCode.setText(seconds + "s");
        }
    };
}
