package com.chanxa.wnb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.UserLoginActivity;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.utils.DeviceUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2014/12/22.
 */
public class SlidingMenuFragment extends BaseFragment implements View.OnClickListener {
    private UserLoginActivity activity;

    private Button btn_memberLogin, btn_staff_login;

    private int menuWidth;//菜单的真正宽度
    private int btn_wh;

    public SlidingMenuFragment() {
    }

    //public SlidingMenuFragment(UserLoginActivity activity) {
     //   this.activity = activity;
   // }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sliding_login, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        btn_memberLogin = (Button) rootView.findViewById(R.id.btn_memberLogin);
        btn_memberLogin.setOnClickListener(this);
        btn_staff_login = (Button) rootView.findViewById(R.id.btn_staff_login);
        btn_staff_login.setOnClickListener(this);
        menuWidth = (int) (DeviceUtils.getScreenW(getActivity()) * 0.4);
        btn_wh = (int) (menuWidth*0.9);

        btn_memberLogin.setWidth(btn_wh);
        btn_memberLogin.setHeight(btn_wh);

        btn_staff_login.setWidth(btn_wh);
        btn_staff_login.setHeight(btn_wh);

    }

    public void setActivity(UserLoginActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_memberLogin:
                activity.switchLoginType(UserLoginActivity.LoginType.Member);
                activity.toggle();
                break;
            case R.id.btn_staff_login:
                activity.switchLoginType(UserLoginActivity.LoginType.Staff);
                activity.toggle();
                break;
        }
    }
}
