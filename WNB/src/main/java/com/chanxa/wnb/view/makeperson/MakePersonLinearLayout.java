package com.chanxa.wnb.view.makeperson;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/27.
 */
public class MakePersonLinearLayout extends LinearLayout {

    private ArrayList<Goods> goodsArrayList = new ArrayList<>();

    private LinearLayout goodsLayout;
    private TextView goodsDescription;
    private boolean isInitView = false;
    private boolean isShowText = false;
    private int screenWidth;
    private WBitmap wBitmap;

    private onMakePersonClickListener onMakePersonClickListener;

    public MakePersonLinearLayout(Context context) {
        super(context);
    }

    public MakePersonLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MakePersonLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MakePersonLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    //只能添加四个
    public boolean addGoods(Goods goods) {
        if (!isInitView) {
            initView();
        }
        if (goodsArrayList.size() == 4) {
            return false;
        }
      /*  if (goodsArrayList.contains(goods)) {
            return false;
        }*/
        boolean b = goodsArrayList.add(goods);
        LogUtils.e(b+"");
        refreshView();
        return true;
    }

    private void initView() {
        this.setOrientation(VERTICAL);
        screenWidth = DeviceUtils.getScreenW(getContext());

        goodsLayout = new LinearLayout(getContext());
        goodsLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        goodsLayout.setOrientation(HORIZONTAL);

        goodsDescription = new TextView(getContext());
        goodsDescription.setGravity(Gravity.LEFT);
        goodsDescription.setPadding(10,20,10,20);
        goodsDescription.setBackgroundColor(0xfffeeeda);
        goodsDescription.setVisibility(GONE);
        isShowText = false;
        isInitView = true;
        wBitmap = WnbApplication.getInstance().getwBitmap();
    }

    public void refreshView() {
        if (goodsArrayList.size() < 1) {
            return;
        }
        LogUtils.e("size:"+goodsArrayList.size());
        goodsLayout.removeAllViews();
        for (int i = 0; i < goodsArrayList.size(); i++) {
            final Goods goods = goodsArrayList.get(i);
            final LinearLayout contenView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_makeperson_title, null, false);
            ImageView img_item_makeperson = (ImageView) contenView.findViewById(R.id.img_item_makeperson);
            TextView tv_item_makeperson_name = (TextView) contenView.findViewById(R.id.tv_item_makeperson_name);
            TextView tv_item_makeperson_Money = (TextView) contenView.findViewById(R.id.tv_item_makeperson_Money);

            tv_item_makeperson_name.setTextColor(Color.RED);
            tv_item_makeperson_name.setText(goods.getName());
            tv_item_makeperson_Money.setTextColor(Color.RED);
            tv_item_makeperson_Money.setText("¥" + StringUtils.toDoubleStr(goods.getMoney(), 2));

            String imgPath = goods.getImgPath();
            if (!StringUtils.isEmpty(imgPath)) {
                if (imgPath.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(img_item_makeperson, imgPath);
                } else {
                    wBitmap.display(img_item_makeperson, AppConfig.PATH_PREFIX + imgPath);
                }
            }

            int width = screenWidth / 4;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            contenView.setPadding(10, 0, 10, 0);
            contenView.setLayoutParams(layoutParams);
            final int finalI = i;
            contenView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int childViewCount = goodsLayout.getChildCount();
                    for (int j = 0; j < childViewCount; j++) {
                        View childView = goodsLayout.getChildAt(j);
                        childView.setBackgroundColor(Color.WHITE);
                    }
                    contenView.setBackgroundResource(R.drawable.makeperson_select);
                    String reMark = goods.getRemarks();
                    if (StringUtils.isEmpty(reMark)) {
                        reMark = "无";
                    }
                    goodsDescription.setText(reMark);
                    if (onMakePersonClickListener != null) {
                        onMakePersonClickListener.onClick(MakePersonLinearLayout.this, finalI, goods);
                    }
                    // textShowToggle();
                }
            });
            goodsLayout.addView(contenView);
        }
        /*goodsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textShowToggle();
            }
        });*/
        this.removeAllViews();
        this.addView(goodsLayout);
        this.addView(goodsDescription);
    }

    public void clearSelect() {
        int childViewCount = goodsLayout.getChildCount();
        for (int j = 0; j < childViewCount; j++) {
            View childView = goodsLayout.getChildAt(j);
            childView.setBackgroundColor(Color.WHITE);
        }
    }

    private void textShowToggle() {
        ValueAnimator valueAnimator = null;
        if (isShowText) {//isShow == true(显示)已经显示将其隐藏
            if (goodsDescription.getVisibility() == View.GONE) {
                return;
            }
            int origHeight = goodsDescription.getHeight();
            valueAnimator = createHeightAnimator(goodsDescription, origHeight, 0);
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    goodsDescription.setVisibility(View.GONE);
                }
            });
        } else {//否则显示它
            if (goodsDescription.getVisibility() == View.VISIBLE) {
                return;
            }
            goodsDescription.setVisibility(View.VISIBLE);
            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            goodsDescription.measure(widthSpec, heightSpec);
            valueAnimator = createHeightAnimator(goodsDescription, 0, goodsDescription.getMeasuredHeight());
        }
        isShowText = !isShowText;
        valueAnimator.start();
    }


    private ValueAnimator createHeightAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void showText() {
        isShowText = false;
        textShowToggle();
    }

    public void hiddenText() {
        isShowText = true;
        textShowToggle();
    }


    public interface onMakePersonClickListener {
        void onClick(MakePersonLinearLayout makePersonLinearLayout, int position, Goods goods);
    }


    public MakePersonLinearLayout.onMakePersonClickListener getOnMakePersonClickListener() {
        return onMakePersonClickListener;
    }

    public void setOnMakePersonClickListener(MakePersonLinearLayout.onMakePersonClickListener onMakePersonClickListener) {
        this.onMakePersonClickListener = onMakePersonClickListener;
    }
}
