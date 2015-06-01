package com.chanxa.wnb.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.fragment.shop.CartFragment;
import com.chanxa.wnb.fragment.shop.FenLeiFragment;
import com.chanxa.wnb.fragment.shop.MemberCenterFragment;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.base.BaseTabActivity;
import com.wtm.library.base.UncaughtHandler;
import com.wtm.library.base.WActivityManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MallMainActivity extends BaseTabActivity {

    @InjectView(R.id.container_fenleiTab)
    RelativeLayout mContainerFenleiTab;
    @InjectView(R.id.container_cartTab)
    RelativeLayout mContainerCartTab;
    @InjectView(R.id.container_memberCenterTab)
    RelativeLayout mContainerMemberCenterTab;

    int curTabIndex = 0;
    private ViewGroup[] tabViewGroup;

    private ArrayList<BaseFragment> fragmentArrayList;
    private FenLeiFragment fenLeiFragment;
    private CartFragment cartFragment;
    private MemberCenterFragment memberCenterFragment;
    protected UncaughtHandler uncaughtHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uncaughtHandler = UncaughtHandler.getInstance(getApplicationContext());
        uncaughtHandler.setOnErrorUncaught(new UncaughtHandler.OnErrorUncaught() {
            @Override
            public void action(Throwable e) {
                WActivityManager.getInstance().finishAllActivity();
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_main);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        tabViewGroup = new ViewGroup[3];
        tabViewGroup[0] = mContainerFenleiTab;
        tabViewGroup[1] = mContainerCartTab;
        tabViewGroup[2] = mContainerMemberCenterTab;
        mContainerFenleiTab.setOnClickListener(this);
        mContainerCartTab.setOnClickListener(this);
        mContainerMemberCenterTab.setOnClickListener(this);
        mContainerFenleiTab.setSelected(true);
        int openTab = getIntent().getIntExtra("openTab",0);
        changeTab(openTab);
    }


    @Override
    protected void onTabChanged(int oldPosition, int newPosition) {
        super.onTabChanged(oldPosition, newPosition);
        if (newPosition > tabViewGroup.length) {
            return;
        }
        tabViewGroup[oldPosition].setSelected(false);
        tabViewGroup[newPosition].setSelected(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.container_fenleiTab:
                curTabIndex=0;
                break;
            case R.id.container_cartTab:
                curTabIndex=1;
                break;
            case R.id.container_memberCenterTab:
                curTabIndex=2;
                break;
        }
        changeTab(curTabIndex);
    }

    @Override
    protected int setFragmentContainer() {
        return R.id.container_mallTab;
    }

    @Override
    public ArrayList<BaseFragment> setFragmentList() {
        fenLeiFragment = new FenLeiFragment();
        cartFragment = new CartFragment();
        memberCenterFragment = new MemberCenterFragment();
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(fenLeiFragment);
        fragmentArrayList.add(cartFragment);
        fragmentArrayList.add(memberCenterFragment);
        return fragmentArrayList;
    }
}
