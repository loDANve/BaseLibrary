package com.chanxa.wnb.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MainActivity;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.fragment.base.MemberBaseFragment;
import com.chanxa.wnb.view.ViewPagerCompat;
import com.chanxa.wnb.zxing.encoding.EncodingHandler;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;

/**
 * Created by CHANXA on 2014/12/25.
 */
public class MemberCardFragment extends MemberBaseFragment implements View.OnClickListener{
    private ImageView img_QRCode;
    private ImageView img_bg_QRCode;
    private ImageView bg_img_QRCode;
    private Button btn_title_back;
    private int barCodeSize = 300;
    private int tryAgainNumber = 2;
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_member_card, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        img_QRCode = (ImageView) rootView.findViewById(R.id.img_QRCode);
        bg_img_QRCode = (ImageView) rootView.findViewById(R.id.bg_img_QRCode);
        img_bg_QRCode = (ImageView) rootView.findViewById(R.id.img_bg_QRCode);
        btn_title_back = (Button) rootView.findViewById(R.id.btn_title_back);
        btn_title_back.setOnClickListener(this);
        rootView.setOnClickListener(this);
        img_bg_QRCode.setOnClickListener(this);
        bg_img_QRCode.setOnClickListener(this);
        img_QRCode.setOnClickListener(this);
        int width = DeviceUtils.getScreenW(getActivity());
        int height = width * 1118 / 720;
        barCodeSize = width * 315 / 720;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        img_bg_QRCode.setLayoutParams(layoutParams);
        initMemberCard();
    }

    private void initMemberCard() {
        Bitmap qrCodeBitmap = null;
        try {
            Card card = getCard();
            if (card == null) {
                return;
            }
            String data = card.getCardNumber();
            qrCodeBitmap = EncodingHandler.createQRCode(data, barCodeSize);
            img_QRCode.setImageBitmap(qrCodeBitmap);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int margin = (int) (barCodeSize * 0.1);
            layoutParams.setMargins(margin,margin,margin,margin);
            bg_img_QRCode.setLayoutParams(layoutParams);
        } catch (WriterException e) {
            if (tryAgainNumber < 0) {
                return;
            }
            initMemberCard();
            tryAgainNumber--;
        }
    }

    @Override
    public void onClick(View v) {
        LogUtils.e("onClick");
        ((MainActivity) getActivity()).setPagerCanChangePager(true);
        ((MainActivity) getActivity()).pagerIndex(1);
    }
}
