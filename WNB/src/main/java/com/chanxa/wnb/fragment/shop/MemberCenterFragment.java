package com.chanxa.wnb.fragment.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.shop.MyCollectionListActivity;
import com.chanxa.wnb.activity.shop.OrderListActivity;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.wtm.library.base.BaseFragment;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class MemberCenterFragment extends BaseFragment implements View.OnClickListener{

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_center,null,false);
    }

    @Override
    public void initView() {
        super.initView();
        rootView.findViewById(R.id.container_back).setOnClickListener(this);
        rootView.findViewById(R.id.container_myOrder).setOnClickListener(this);
        rootView.findViewById(R.id.container_myCollection).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_back:
                getActivity().finish();
                break;
            case R.id.container_myCollection:
                startActivity(new Intent(getActivity(), MyCollectionListActivity.class));
                break;
            case R.id.container_myOrder:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
        }
    }
}