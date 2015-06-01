package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.staff.StaffMainActivity;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.fragment.SlidingMenuFragment;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.slidingmenu.SlidingMenu;
import com.wtm.library.slidingmenu.activity.SlidingActivity;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserLoginActivity extends SlidingActivity implements View.OnClickListener {

    @InjectView(R.id.edit_loginName)
    EditText mEditLoginName;
    @InjectView(R.id.edit_loginPwd)
    EditText mEditLoginPwd;
    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    @InjectView(R.id.btn_reg)
    Button mBtnReg;
    @InjectView(R.id.tv_loginType)
    TextView mTvLoginType;
    ProgressDialog progressDialog;
    @InjectView(R.id.btn_clear_name)
    Button mBtnClearName;
    @InjectView(R.id.btn_clear_pwd)
    Button mBtnClearPwd;

    public enum LoginType {
        Member, Staff
    }

    private LoginType loginType = LoginType.Member;
    private Class nextActivity;

    private User memberUser = null;
    private User staffUser = null;
    private User curUser = null;
    private SlidingMenuFragment slidingMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            memberUser = UserDAO.read(UserDAO.TAG.MEMBERTAG,this);
            staffUser = UserDAO.read(UserDAO.TAG.STAFFTAG,this);
        } catch (Exception e) {
        }
        setContentView(R.layout.activity_user_login);
        slidingMenuFragment = new SlidingMenuFragment();
        slidingMenuFragment.setActivity(this);
        initSlidingMenu(savedInstanceState);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        mBtnLogin.setOnClickListener(this);
        mBtnReg.setOnClickListener(this);

        mBtnClearName.setOnClickListener(this);
        mBtnClearPwd.setOnClickListener(this);

        findViewById(R.id.tv_goBackPwd).setOnClickListener(this);
        refreshView();
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        initEditWatcher(mEditLoginPwd, mBtnClearPwd);
        initEditWatcher(mEditLoginName, mBtnClearName);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void initEditWatcher(final EditText editText, final Button btn) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString() != null && !editText.getText().toString().equals("")) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String cardNumber = mEditLoginName.getText().toString();
                String cardPwd = mEditLoginPwd.getText().toString();
                if (StringUtils.isEmpty(cardNumber) || StringUtils.isEmpty(cardPwd)) {
                    ViewInject.toast("用户名密码不能为空");
                    return;
                }
                login(cardNumber, cardPwd);
                break;
            case R.id.btn_reg:
                startActivity(new Intent(this, UserRegActivity.class));
                break;
            case R.id.tv_goBackPwd:
                startActivity(new Intent(this, ShortMessageVerificationActivity.class));
                break;
            case R.id.btn_clear_name:
                mEditLoginName.setText("");
            case R.id.btn_clear_pwd:
                mEditLoginPwd.setText("");
                break;
        }
    }

    /**
     * 登陆
     *
     * @param cardNumber
     * @param cardPwd
     */
    private void login(String cardNumber, String cardPwd) {
        if (loginType.equals(LoginType.Member)) {
            memberLogin(cardNumber, cardPwd);
            return;
        }
        staffLogin(cardNumber, cardPwd);
    }

    /**
     * 员工登陆
     *
     * @param cardNumber
     * @param cardPwd
     */
    private void staffLogin(final String cardNumber, final String cardPwd) {
        new StaffService().login(cardNumber, cardPwd, new ServiceCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                if (stateCode == 404) {
                    ViewInject.toast("网络无连接,请检查设置");
                } else {
                    ViewInject.toast(e+"");
//                    ViewInject.toast("登陆失败");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                final User user = new User();
                //save user local
                user.setCardNumber(cardNumber);
                user.setPwd(cardPwd);
                try {
                    UserDAO.save(user, UserDAO.TAG.STAFFTAG,WActivityManager.getInstance().topActivity());
                } catch (Exception e) {
                } finally {
                    //end
                    Staff staff = (Staff) resultObj;
                    WnbApplication.getInstance().setStaff(staff);
                    progressDialog.dismiss();
                    startActivity(new Intent(UserLoginActivity.this, nextActivity));
                    ViewInject.toast("登录成功,欢迎回来");
                    finish();
                }
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    /**
     * 会员登陆
     *
     * @param cardNumber
     * @param cardPwd
     */
    private void memberLogin(String cardNumber, String cardPwd) {
        new CardService().login(cardNumber, cardPwd, true, new ServiceCallBack() {

            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                if (stateCode == 404) {
                    ViewInject.toast("网络无连接,请检查设置");
                } else {
                    ViewInject.toast("登陆失败");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                LogUtils.d(errroMsg);
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                List<Goods> arrayList = new Select().from(Goods.class).where("cardNumber = " + WnbApplication.getInstance().getCard().getCardNumber()).execute();
                LogUtils.e("size:" + arrayList.size());
                WnbApplication.getInstance().getGoodsCart().setGoodsList((ArrayList<Goods>) arrayList);
                progressDialog.dismiss();
                startActivity(new Intent(UserLoginActivity.this, nextActivity));
                ViewInject.toast("登录成功,欢迎回来");
                finish();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });

    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, slidingMenuFragment).commit();
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setBehindOffset((int) (DeviceUtils.getScreenW(this) * 0.6));
        sm.setFadeDegree(0.35f);
        //sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        sm.setMode(SlidingMenu.LEFT);
    }

    public void switchLoginType(LoginType loginType) {
        this.loginType = loginType;
        refreshView();
    }

    private void refreshView() {
        switch (this.loginType) {
            case Member:
                if (!StringUtils.isEmpty(mEditLoginName.getText().toString())) {
                    staffUser.setCardNumber(mEditLoginName.getText().toString());
                    staffUser.setPwd(mEditLoginPwd.getText().toString());
                }
                mTvLoginType.setText("电子商务会员");
                mBtnReg.setVisibility(View.VISIBLE);
                nextActivity = MainActivity.class;
                mEditLoginName.setInputType(InputType.TYPE_CLASS_NUMBER);
                curUser = memberUser;
                break;
            case Staff:
                if (!StringUtils.isEmpty(mEditLoginName.getText().toString())) {
                    memberUser.setCardNumber(mEditLoginName.getText().toString());
                    memberUser.setPwd(mEditLoginPwd.getText().toString());
                }
                mTvLoginType.setText("员工登录");
                mEditLoginName.setInputType(InputType.TYPE_CLASS_TEXT);
                mBtnReg.setVisibility(View.INVISIBLE);
                nextActivity = StaffMainActivity.class;
                curUser = staffUser;
                break;
        }
        mEditLoginName.setText("");
        mEditLoginPwd.setText("");
        if (curUser != null) {
            if (!StringUtils.isEmpty(curUser.getCardNumber()) && !curUser.getCardNumber().toUpperCase().equals("NULL")) {
                mEditLoginName.setText(curUser.getCardNumber());
            }
            mEditLoginPwd.setText(curUser.getPwd());
        }
    }
}
