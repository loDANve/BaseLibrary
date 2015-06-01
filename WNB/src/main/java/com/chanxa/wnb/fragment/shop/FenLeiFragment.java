package com.chanxa.wnb.fragment.shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.SearchActivity;
import com.chanxa.wnb.activity.shop.GoodsSearchActivity;
import com.chanxa.wnb.activity.shop.SearchResultActivity;
import com.chanxa.wnb.adapter.FenLeiTypeListViewAdapter;
import com.chanxa.wnb.adapter.ListHeaderAdapter;
import com.chanxa.wnb.adapter.ShopFenLeiHeaderGridAdapter;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;
import com.chanxa.wnb.bean.onLineShop.GoodsHeader;
import com.chanxa.wnb.bean.onLineShop.GoodsHeaderParent;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.stickygridheaders.StickyGridHeadersGridView;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class FenLeiFragment extends BaseFragment {
    private ListView list_shop_fenlei, listHeadersFenlei;
    private FenLeiTypeListViewAdapter typeAdapter;
    private ArrayList<GoodsHeaderParent> goodsHeaderParentArrayList = new ArrayList<>();
    private ListHeaderAdapter listHeaderAdapter;
    private ProgressDialog progressDialog;
    private TextView tv_fenleiStoreName;
    private LinearLayout container_listHeadersFenlei;
    private RelativeLayout relativeLayout_advert;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_fenlei, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        rootView.findViewById(R.id.container_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rootView.findViewById(R.id.edit_shop_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoodsSearchActivity.class));
            }
        });
        typeAdapter = new FenLeiTypeListViewAdapter(WnbApplication.getInstance().getFenleiArrayListArrayList(), getActivity());
        list_shop_fenlei = (ListView) rootView.findViewById(R.id.list_shop_fenlei);
        listHeadersFenlei = (ListView) rootView.findViewById(R.id.listHeadersFenlei);
        tv_fenleiStoreName = (TextView) rootView.findViewById(R.id.tv_fenleiStoreName);
        relativeLayout_advert = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_advert);
        relativeLayout_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FenLeiStore fenLeiStore = (FenLeiStore) v.getTag();
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("storeMark", fenLeiStore.getMark());
                startActivity(intent);
            }
        });

        /*tv_fenleiStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FenLeiStore fenLeiStore = (FenLeiStore) v.getTag();
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("storeMark", fenLeiStore.getMark());
                startActivity(intent);
            }
        });*/
        container_listHeadersFenlei = (LinearLayout) rootView.findViewById(R.id.container_listHeadersFenlei);
        list_shop_fenlei.setAdapter(typeAdapter);
        list_shop_fenlei.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setSelectPosition(position);
                list_shop_fenlei.setSelection(position);
                FenLeiStore fenLeiStore = WnbApplication.getInstance().getFenleiArrayListArrayList().get(position);
                tv_fenleiStoreName.setText(fenLeiStore.getName());
                //tv_fenleiStoreName.setTag(fenLeiStore);
                relativeLayout_advert.setTag(fenLeiStore);
                gridHeaderAdapterSetUp(position);
            }
        });
    }

    //防止gridview加载数据导致布局参数失效 设置适配器手动再次设置
    private void refreshHeaderGridParams() {
        if (container_listHeadersFenlei == null) {
            return;
        }
        int width = DeviceUtils.getScreenW(getActivity()) / 4 * 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, -1);
        layoutParams.setMargins(20, 0, 20, 0);
        container_listHeadersFenlei.setLayoutParams(layoutParams);
    }

    @Override
    public void initData() {
        super.initData();
        gridHeaderAdapterSetUp(0);
    }

    private void gridHeaderAdapterSetUp(final int index) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (WnbApplication.getInstance().getFenleiArrayListArrayList() == null || WnbApplication.getInstance().getFenleiArrayListArrayList().size() < 1) {
                    return;
                }
                FenLeiStore fenLeiStore = WnbApplication.getInstance().getFenleiArrayListArrayList().get(index);
                ArrayList<GoodsHeaderParent> goodsHeaderParentArrayList1 = new ArrayList<>();

                for (StaffStoreGoodsLV1 staffStoreGoodsLV1 : fenLeiStore.getStaffStoreGoodsLV1ArrayList()) {
                    GoodsHeaderParent goodsHeaderParent = new GoodsHeaderParent();
                    goodsHeaderParent.setHearId(Long.parseLong(staffStoreGoodsLV1.getMark()));
                    goodsHeaderParent.setName(staffStoreGoodsLV1.getName());
                    goodsHeaderParent.setMark(staffStoreGoodsLV1.getMark());
                    for (StaffStoreGoodsLV2 staffStoreGoodsLV2 : staffStoreGoodsLV1.getStaffStoreGoodsLV2ArrayList()) {
                        GoodsHeader goodsHeader = new GoodsHeader();
                        goodsHeader.setImgPath(staffStoreGoodsLV2.getImgPath());
                        goodsHeader.setName(staffStoreGoodsLV2.getName());
                        goodsHeader.setMark(staffStoreGoodsLV2.getMark());
                        goodsHeader.setGoodsHeaderParent(goodsHeaderParent);
                        goodsHeaderParent.addGoodsHeader(goodsHeader);
                    }
                    goodsHeaderParentArrayList1.add(goodsHeaderParent);
                }
                goodsHeaderParentArrayList.clear();
                goodsHeaderParentArrayList.addAll(goodsHeaderParentArrayList1);
                if (listHeaderAdapter == null) {
                    listHeaderAdapter = new ListHeaderAdapter(goodsHeaderParentArrayList, getActivity());
                    listHeaderAdapter.setOnItemClick(new ListHeaderAdapter.OnItemClick() {
                        @Override
                        public void toDo(String mark) {
                            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                            intent.putExtra("goodsType", mark);
                            intent.putExtra("goodsName", "");
                            startActivity(intent);
                        }
                    });
                    listHeadersFenlei.setAdapter(listHeaderAdapter);
                } else {
                    listHeaderAdapter.notifyDataSetChanged();
                }
                tv_fenleiStoreName.setText(WnbApplication.getInstance().getFenleiArrayListArrayList().get(index).getName());
                //tv_fenleiStoreName.setTag(fenLeiStore);
                relativeLayout_advert.setTag(fenLeiStore);
                refreshHeaderGridParams();
            }
        });
    }

}