package com.chanxa.wnb.activity.staff;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.StaffServiceCallBack;
import com.chanxa.wnb.utils.BitmapUtil;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.UploadImageView;
import com.chanxa.wnb.view.popwindow.ThreeWheelPopDelegate;
import com.chanxa.wnb.view.switchbutton.SwitchButton;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.GoodsTypeXmlParser;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffUpLoadGoodsActivity extends DefaultTitleActivity {

    @InjectView(R.id.container_SystemMessageScore)
    RelativeLayout mContainerSystemMessageScore;

    @InjectView(R.id.edit_goodsName)
    EditText mEditGoodsName;

    @InjectView(R.id.edit_goodsBarCode)
    EditText mEditGoodsBarCode;

    @InjectView(R.id.edit_goodsMoney)
    EditText mEditGoodsMoney;

    //商品详情的图片
    @InjectView(R.id.img_upLoadHtml)
    UploadImageView mImgUpLoadHtml;

    //商品头像的图片
    @InjectView(R.id.upImg_goods)
    UploadImageView mUpImgGoods;

    @InjectView(R.id.edit_goodsReMark)
    EditText mEditGoodsReMark;

    @InjectView(R.id.btn_subGoods)
    Button mBtnSubGoods;

    @InjectView(R.id.tv_goodsType)
    TextView mTvGoodsType;



    @InjectView(R.id.container_UploadImageView)
    LinearLayout mContainerUploadImageView;

    @InjectView(R.id.container_UploadImageView2)
    LinearLayout mContainerUploadImageView2;

    @InjectView(R.id.container_UploadImageView3)
    LinearLayout mContainerUploadImageView3;

    @InjectView(R.id.switch_isOnlineOrder)
    SwitchButton mSwitchIsOnlineOrder;

    @InjectView(R.id.tv_upGoodsPreview)
    TextView mTvUpGoodsPreview;//预览

    private StaffService staffService;
    private ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList;
    private String localImgPath;
    private ProgressDialog progressDialog;
    private ThreeWheelPopDelegate threeWheelPopDelegate;
    private String goodsTypeMark;
    private String goodsType;
    private String goodsName;
    private String goodsBarCode;
    private String goodsMoney;
    private String goodsRemark;
    private String goodsImgPath;
    private WBitmap wBitmap;
    private ArrayList<String> htmlImgPath = new ArrayList<>();
    private String htmlStart = "<img src=\"";
    private String htmlend = "\" alt=\"\" border=\"0\" style=\"width:100%\" />";
    private boolean inAsynUpLoadImg = false;
    private ArrayList<FenLeiStore> fenLei_list;

    private enum UpLoadType {//是商品的图片 还是对应网页的图片
        GOODSIMG, HTMLIMG
    }

    private UpLoadType upLoadType;
    private ArrayList<ImageView> imgList = new ArrayList<ImageView>();
    private ArrayList<String> compressImagePath = new ArrayList<>();
    private LinearLayout.LayoutParams layoutParams;
    private int deviceWidth, deviceHeight;

    //从上个界面是否传递goods过来就行编辑,如果不传则为提交新商品
    private Goods curGoods;

    @Override
    public String initTitleText() {
        return getString(R.string.upLoadGoods);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_up_load_goods);
        //取消软键盘显示
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        deviceWidth = DeviceUtils.getScreenW(this);
        deviceHeight = DeviceUtils.getScreenH(this);
        initGoodsTypewheelPopDelegate();
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        mContainerSystemMessageScore.setOnClickListener(this);
        mTvUpGoodsPreview.setOnClickListener(this);
        mBtnSubGoods.setOnClickListener(this);
        /*******************设置上传的商品图片view************************/
        mUpImgGoods.setActivity(this);
        mUpImgGoods.setUploadImageViewOnClick(new UploadImageView.UploadImageViewOnClick() {
            @Override
            public void onclick(View v) {
                upLoadType = UpLoadType.GOODSIMG;
            }
        });
        mUpImgGoods.setOutputX(320);
        mUpImgGoods.setOutputY(320);
        mUpImgGoods.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success, Bitmap bitmap, String path) {
                mUpImgGoods.setImageBitmap(bitmap);
                localImgPath = path;
            }
        });

        /*******************设置上传的商品图片html部分view************************/
        int width = deviceWidth - DeviceUtils.dip2px(this, 28);
        layoutParams = new LinearLayout.LayoutParams(width / 4 - 20,
                width / 4 - 20);
        layoutParams.setMargins(10, 10, 10, 10);
        mImgUpLoadHtml.setLayoutParams(layoutParams);
        mImgUpLoadHtml.setCutting(false);
        mImgUpLoadHtml.setActivity(this);
        mImgUpLoadHtml.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success, Bitmap bitmap, final String path) {
                if (path == null) {
                    return;
                }
                final ImageView imageView = new ImageView(StaffUpLoadGoodsActivity.this);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                wBitmap.display(imageView, path);
                imageView.setTag(path);
                imgList.add(imageView);
                refreshImgContainer();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        asynUpLoadHtmlImg(imageView);
                    }
                });
            }
        });
        mImgUpLoadHtml.setUploadImageViewOnClick(new UploadImageView.UploadImageViewOnClick() {
            @Override
            public void onclick(View v) {
                upLoadType = UpLoadType.HTMLIMG;
            }
        });
        if (curGoods != null) {//当前为编辑商品,将商品属性填入view
            obtainGoodsDetails(curGoods);
        }
        //mSwitchIsOnlineOrder.setChecked(true);
        mSwitchIsOnlineOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //ViewInject.toast(isChecked);
            }
        });
        super.initView();
    }

    //将商品属性填入view
    private void goods2View() {
        if (curGoods == null) {
        return;
    }
    disPlayImg(mUpImgGoods, curGoods.getImgPath());
    //mTvGoodsType.setText(curGoods.getGoodsTypeMark());
    mEditGoodsName.setText(curGoods.getName());
    mEditGoodsBarCode.setText(curGoods.getBarCode());
    mEditGoodsMoney.setText(curGoods.getMoney());
    mEditGoodsReMark.setText(curGoods.getRemarks());
        goodsTypeMark = curGoods.getGoodsTypeMark();
        localImgPath = curGoods.getImgPath();
        obtainGoodsType(curGoods.getGoodsTypeMark());

        if (!StringUtils.isEmpty(curGoods.getIsOnLineOrder())) {
            mSwitchIsOnlineOrder.setChecked(curGoods.getIsOnLineOrder().equals("1"));

        }
        //ViewInject.toast(curGoods.getIsOnLineOrder());
        String details = curGoods.getDetails();
        if (!StringUtils.isEmpty(details)) {
//            htmlImgPath
            Pattern srcPattern = Pattern.compile("(?:src=\"?)(.*?)\"?\\s");
            Matcher m_image = srcPattern.matcher(details);
            while (m_image.find()) {
                htmlImgPath.add(m_image.group(1));
            }
        }
        for (final String path : htmlImgPath) {
            final ImageView imageView = new ImageView(StaffUpLoadGoodsActivity.this);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            wBitmap.display(imageView, path);
            imageView.setTag(path);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (imgList.contains(v)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StaffUpLoadGoodsActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("确定要删除么？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (imgList.contains(v)) {
                                    imgList.remove(v);
                                    refreshImgContainer();
                                    htmlImgPath.remove(path);
                                }
                            }
                        });
                        builder.show();
                    }
                    return true;
                }
            });
            imgList.add(imageView);
        }
        refreshImgContainer();
    }

    private void disPlayImg(View v, String imgPath) {
        if (StringUtils.isEmpty(imgPath)) {
            return;
        }
        if (imgPath.startsWith(AppConfig.PATH_PREFIX)) {
            wBitmap.display(v, imgPath);
        } else {
            wBitmap.display(v, AppConfig.PATH_PREFIX + imgPath);
        }
    }

    private void refreshImgContainer() {
        mContainerUploadImageView.removeAllViews();
        mContainerUploadImageView2.removeAllViews();
        mContainerUploadImageView3.removeAllViews();
        for (int i = 0; i < imgList.size(); i++) {
            ImageView img = imgList.get(i);
            if (i < 4) {
                img.setLayoutParams(layoutParams);
                mContainerUploadImageView.addView(img);
            } else {
                if (i < 8) {
                    mContainerUploadImageView2.addView(img);
                } else {
                    mContainerUploadImageView3.addView(img);
                }
            }
        }
        if (imgList.size() < 4) {
            mContainerUploadImageView.addView(mImgUpLoadHtml);
        } else if (imgList.size() >= 4 && imgList.size() < 8) {
            mContainerUploadImageView2.addView(mImgUpLoadHtml);
        } else if (imgList.size() >= 8 && imgList.size() < 13) {
            mContainerUploadImageView3.addView(mImgUpLoadHtml);
        }
    }

    private void initGoodsTypewheelPopDelegate() {
        staffStoreGoodsLV1ArrayList = WnbApplication.getInstance().getStaffStoreGoodsLV1ArrayList();
        if (staffStoreGoodsLV1ArrayList == null) {
            return;
        }
        threeWheelPopDelegate = new ThreeWheelPopDelegate(this, staffStoreGoodsLV1ArrayList);
        threeWheelPopDelegate.setOnAreaBtnClick(new ThreeWheelPopDelegate.OnAreaBtnClick() {
            @Override
            public void onPositiveClick(String curName, String curMark) {
                goodsTypeMark = curMark;
                mTvGoodsType.setText(curName);
                threeWheelPopDelegate.disMiss();
            }

            @Override
            public void onNegativeClick(String curName, String curMark) {
                threeWheelPopDelegate.disMiss();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        staffService = new StaffService();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        curGoods = (Goods) getIntent().getSerializableExtra(Goods.class.getSimpleName());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mBtnSubGoods) {
            subGoods();
            return;
        }
        if (v == mContainerSystemMessageScore) {
            if (threeWheelPopDelegate != null) {
                threeWheelPopDelegate.show();
            }
            return;
        }
        if (v == mTvUpGoodsPreview) {
            goPreview();
            return;
        }
    }

    /**
     * 商品预览
     */
    private void goPreview() {
        if (!verificationInput()) {
            return;
        }
        Intent intent = new Intent(this, StaffGoodsPreviewActivity.class);
        Goods goods = new Goods();

        goods.setName(mEditGoodsName.getText().toString());
        goods.setMoney(mEditGoodsMoney.getText().toString());
        goods.setImgPath(localImgPath);
        goods.setDetails(getGoodsDetails());
        goods.setRemarks(mEditGoodsReMark.getText().toString());

        intent.putExtra(Goods.class.getSimpleName(),goods);
        startActivity(intent);
    }

    private void subGoods() {
        if (inAsynUpLoadImg) {
            ViewInject.toast("正在上传图片,请稍后");
            return;
        }
        if (!verificationInput()) {
            return;
        }
        if (StringUtils.isEmpty(localImgPath)) {
            ViewInject.toast("请选择一张图片");
            return;
        }

        if (localImgPath.contains("File")) {
            goodsImgPath = localImgPath;
            addNewGoods();
            return;
        }
        File file = new File(localImgPath);
        if (!file.exists()) {
            ViewInject.toast("读取图片失败,请重新选择图片");
            return;
        }
        progressDialog.show();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            staffService.upLoadImage(Base64Helper.encode(FileUtils.input2byte(inputStream)), new BaseCallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("图片上传失败,请尝试重试");
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECSuccess(String result) {
                    //去新增商品
                    try {
                        goodsImgPath = DATAxmlHelper.getAllAttribute(result).get("图片路径");
                        addNewGoods();
                    } catch (DocumentException e) {
                        onError(e, -1);
                    }
                }

                @Override
                public void onComplete(String result) {

                }
            });
        } catch (FileNotFoundException e) {
            progressDialog.dismiss();
            ViewInject.toast("读取图片失败,请重新选择图片");
        } finally {
            FileUtils.closeIO(inputStream);
        }
    }

    private String getGoodsDetails(){
        String details = "";
        for (int i = 0; i < htmlImgPath.size(); i++) {
            String tempHtml = htmlStart + htmlImgPath.get(i) + htmlend;
            details += tempHtml;
        }
        details += "<br/>";
        return details;
    }

    private void addNewGoods() {
        if (StringUtils.isEmpty(goodsImgPath)) {
            ViewInject.toast("图片未上传成功,请重试");
            return;
        }
        String details = getGoodsDetails();
        String goodsMark = "";
        if (curGoods != null) {
            goodsMark = curGoods.getMark();
        }
        staffService.action_AddGoods(WnbApplication.getInstance().getStaff(), goodsMark, goodsTypeMark, goodsName, goodsBarCode, goodsImgPath, goodsMoney, goodsRemark, details, mSwitchIsOnlineOrder.isChecked(), new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("上传商品失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    String newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    ViewInject.toast("商品上传成功");
                    progressDialog.dismiss();
                    finish();
                } catch (DocumentException e) {
                    onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {
            }
        });
    }

    //验证输入是否完整
    private boolean verificationInput() {
        inputFromView();
        if (StringUtils.isEmpty(goodsTypeMark)) {
            ViewInject.toast("请选择商品类型");
            return false;
        }
        if (StringUtils.isEmpty(goodsName)) {
            ViewInject.toast("请输入商品名称");
            return false;
        }
        if (StringUtils.isEmpty(goodsBarCode)) {
            ViewInject.toast("请输入商品条码");
            return false;
        }
        if (StringUtils.isEmpty(goodsMoney)) {
            ViewInject.toast("请选择商品价格");
            return false;
        }
        return true;
    }

    private void inputFromView() {
        goodsType = mTvGoodsType.getText().toString();
        goodsName = mEditGoodsName.getText().toString();
        goodsBarCode = mEditGoodsBarCode.getText().toString();
        goodsMoney = mEditGoodsMoney.getText().toString();
        goodsRemark = mEditGoodsReMark.getText().toString();
    }

    /**
     * 异步上传图片
     */
    private void asynUpLoadHtmlImg(final ImageView imageView) {
        if (imageView == null) {
            return;
        }
        String path = imageView.getTag().toString();
        if (StringUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        final String newPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/temp" + StringUtils.md5(UUID.randomUUID() + "") + ".jpg";
        if (!BitmapUtil.compressImage(StaffUpLoadGoodsActivity.this, file.getPath(), newPath)) {
            return;
        }
        ;
        final File compressFile = new File(newPath);
        if (!compressFile.exists()) {
            return;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(compressFile);
            staffService.upLoadImage(Base64Helper.encode(FileUtils.input2byte(inputStream)), new BaseCallBack() {
                @Override
                public void onStart() {
                    inAsynUpLoadImg = true;
                }

                @Override
                public void onError(Exception e, int stateCode) {
                    //异步上传失败
                    if (imgList.contains(imageView)) {
                        imgList.remove(imageView);
                        refreshImgContainer();
                    }
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    //异步上传失败
                    onError(new RuntimeException(""), -1);
                }

                @Override
                public void onEXECSuccess(String result) {
                    try {
                        final String path = DATAxmlHelper.getAllAttribute(result).get("图片路径");
                        LogUtils.e("path:" + path);
                        compressImagePath.add(newPath);
                        htmlImgPath.add(AppConfig.PATH_PREFIX + path);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(StaffUpLoadGoodsActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("确定要删除么？");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (imgList.contains(v)) {
                                            imgList.remove(v);
                                            refreshImgContainer();
                                            htmlImgPath.remove(AppConfig.PATH_PREFIX + path);
                                            if (compressFile != null && compressFile.exists()) {
                                                compressFile.delete();
                                            }
                                        }
                                    }
                                });
                                builder.show();
                                return true;
                            }
                        });
                    } catch (DocumentException e) {
                    }
                }

                @Override
                public void onComplete(String result) {
                    inAsynUpLoadImg = false;
                }
            });
        } catch (FileNotFoundException e) {
            return;
        } finally {
            FileUtils.closeIO(inputStream);
        }
    }

    //获取商品详情
    public void obtainGoodsDetails(Goods goods) {
        if (goods == null || StringUtils.isEmpty(goods.getMark())) {
            return;
        }
        staffService.obtainGoodsDetails(WnbApplication.getInstance().getStaff(), goods.getMark(), new StaffServiceCallBack<Goods>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(Goods resultObj, String proof) {
                if (!StringUtils.isEmpty(resultObj.getDetails())) {
                    curGoods.setDetails(resultObj.getDetails());
                }
                curGoods.setIsOnLineOrder(resultObj.getIsOnLineOrder());
                WnbApplication.getInstance().getStaff().setProof(proof);
                goods2View();
            }

            @Override
            public void onEXECSuccess(Goods resultObj, String proof, int pageSize, int dataSize) {

            }
        });

    }

    //获取商品分类
    public void obtainGoodsType(final String goodsTypeMark) {
        ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList = WnbApplication.getInstance().getStaffStoreGoodsLV1ArrayList();
        for (int i = 0; i < staffStoreGoodsLV1ArrayList.size(); i++) {
            if (staffStoreGoodsLV1ArrayList.get(i) == null){
                continue;
            }
            if (goodsTypeMark.equals(staffStoreGoodsLV1ArrayList.get(i).getMark())){
                mTvGoodsType.setText(staffStoreGoodsLV1ArrayList.get(i).getName());
            }
            ArrayList<StaffStoreGoodsLV2> staffStoreGoodsLV2ArrayList = staffStoreGoodsLV1ArrayList.get(i).getStaffStoreGoodsLV2ArrayList();
            for (int j = 0; j < staffStoreGoodsLV2ArrayList.size(); j++) {
                if (staffStoreGoodsLV2ArrayList.get(j) == null){
                    continue;
                }
                LogUtils.fff(staffStoreGoodsLV2ArrayList.get(j).getMark() + "---------------------");
                if (goodsTypeMark.equals(staffStoreGoodsLV2ArrayList.get(j).getMark())){
                    mTvGoodsType.setText(staffStoreGoodsLV2ArrayList.get(j).getName());
                }
            }
        }
        /*staffService.obtainGoodsType(WnbApplication.getInstance().getStaff(), new StaffServiceCallBack<ArrayList<FenLeiStore>>(){

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(ArrayList<FenLeiStore> resultObj, String proof) {
                fenLei_list = resultObj;
                LogUtils.fff("---------------------"+fenLei_list.get(0).getName()+"------------------------");
                for (int i = 0; i < fenLei_list.size(); i++) {
                    if (goodsTypeMark.equals(fenLei_list.get(i).getMark())){
                        mTvGoodsType.setText(fenLei_list.get(i).getName());
                    }
                }
                WnbApplication.getInstance().getStaff().setProof(proof);
            }

            @Override
            public void onEXECSuccess(ArrayList<FenLeiStore> resultObj, String proof, int pageSize, int dataSize) {

            }
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (upLoadType) {
            case GOODSIMG:
                mUpImgGoods.setOnActivityResult(requestCode, resultCode, data);
                break;
            case HTMLIMG:
                mImgUpLoadHtml.setOnActivityResult(requestCode, resultCode, data);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        mUpImgGoods.recycle();
        mImgUpLoadHtml.recycle();
        for (String path : compressImagePath) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        super.onDestroy();
    }
}