package com.chanxa.wnb.activity.staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MakeDetailsActivity;
import com.chanxa.wnb.bean.ContactDetails;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.StoreDetails;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.utils.BitmapUtil;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.UploadImageView;
import com.chanxa.wnb.view.popwindow.ThreeWheelPopDelegate;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffStoreMaintenance extends DefaultTitleActivity {

    //全部
    @InjectView(R.id.allTitle)
    RelativeLayout alltitle;
    //联系人
    @InjectView(R.id.edit_contactPerson)
    EditText edit_contactPerson;
    String contactPerson;

    //联系电话
    @InjectView(R.id.edit_contactPhone)
    EditText edit_contactPhone;
    String contactPhone;

    //存放修改的详细信息
    ContactDetails contactDetails;

    //添加店铺详情图片
    @InjectView(R.id.img_upLoadHtml)
    UploadImageView mImgUpLoadHtml;

    //添加店铺详情图片的标语
    @InjectView(R.id.message)
    TextView message;

    //添加商品Logo图片
    @InjectView(R.id.shopLogo)
    UploadImageView shopLogo;

    //确认按钮
    @InjectView(R.id.btn_confirm)
    Button btn_Confirm;

    //用来保存图片的区域
    @InjectView(R.id.container_UploadImageView)
    LinearLayout mContainerUploadImageView;
    @InjectView(R.id.container_UploadImageView2)
    LinearLayout mContainerUploadImageView2;
    @InjectView(R.id.container_UploadImageView3)
    LinearLayout mContainerUploadImageView3;

    //存放logo的区域
    @InjectView(R.id.logoArea)
    LinearLayout logoUploadImageView;

    //存放plus图标的区域
    @InjectView(R.id.plusContainer)
    LinearLayout plusContainer;

    private LinearLayout.LayoutParams layoutParams,layoutParams2;

    private Bitmap bitmap;

    private WBitmap wBitmap;
    //图片方面的接口
    private StaffService staffService;
    private ProgressDialog progressDialog;
    private String goodsImgPath;

    private int deviceWidth, deviceHeight;

    private String goodsTypeMark;

    private String goodsType;
    //店铺详情
    private StoreDetails storeDetails;
    private ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList;
    private ArrayList<String> htmlImgPath = new ArrayList<>();
    private ThreeWheelPopDelegate threeWheelPopDelegate;

    private ArrayList<ImageView> imgList = new ArrayList<ImageView>();
    private UpLoadType upLoadType;
    //上传图片的工作状态
    private boolean inAsynUpLoadImg = false;
    private String localImgPath;
    //通过接口获得的店铺
    private Store curStore=null;
    private ArrayList<String> compressImagePath = new ArrayList<>();
    private PackageManager manager;
    private PackageInfo info = null;
    private String logo;//店铺logo
    private enum UpLoadType {//是商品的图片 还是对应网页的图片
        GOODSIMG, HTMLIMG
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = StaffStoreMaintenance.this.getPackageManager();
        setContentView(R.layout.activity_staff_store_maintenance);
        //取消软键盘显示
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == btn_Confirm){
            verificationInput();
            ViewInject.toast("点击");
            //subGoods();
            curStore=  (Store) getIntent().getSerializableExtra("store");
            verificationInput();
            contactDetails.setContactPerson(contactPerson);
            contactDetails.setContactPhone(contactPhone);
            new StaffService().editStoreDetails(contactDetails,logo,storeDetails,new BaseCallBack(){
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("店铺维护失败");
                    LogUtils.fff("Exception:   "  +e);
                    LogUtils.fff("stateCode:   "  +stateCode);
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                    LogUtils.fff("errroMsg   "  + errroMsg);
                }

                @Override
                public void onEXECSuccess(String result) {
                    String newProof = null;
                    try {
                        newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    WnbApplication.getInstance().getStaff().setProof(newProof);
                    ViewInject.toast("店铺维护成功");
                    LogUtils.fff("result   "  + result);
                    finish();
                }

                @Override
                public void onComplete(String result) {
                    LogUtils.fff("onComplete:"+result);
                }
            });
        }
    }
    @Override
    public void initView(){
        ButterKnife.inject(this);
        deviceWidth = DeviceUtils.getScreenW(this);
        deviceHeight = DeviceUtils.getScreenH(this);
        btn_Confirm.setOnClickListener(this);
        initGoodsTypewheelPopDelegate();
        curStore=new Store();
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        contactDetails = new ContactDetails();
        /*******************设置上传的商品图片html部分view************************/
        int width = deviceWidth - DeviceUtils.dip2px(this, 28);
        layoutParams = new LinearLayout.LayoutParams(width /4-10,
                width /4);
        layoutParams2=new LinearLayout.LayoutParams(width /4,
                width /4+25);
        layoutParams.setMargins(10, 10, 10, 10);
        mImgUpLoadHtml.setLayoutParams(layoutParams2);
        mImgUpLoadHtml.setCutting(false);
        mImgUpLoadHtml.setActivity(this);
        mImgUpLoadHtml.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success, Bitmap bitmap, final String path) {
                if (path == null) {
                    return;
                }
                final ImageView imageView = new ImageView(StaffStoreMaintenance.this);
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
        /*****************设置上传的商品Logo图片****************************/

        shopLogo.setActivity(this);
        shopLogo.setLayoutParams(layoutParams2);
        shopLogo.setUploadImageViewOnClick(new UploadImageView.UploadImageViewOnClick() {
            @Override
            public void onclick(View v) {
                upLoadType = UpLoadType.GOODSIMG;
            }
        });
        shopLogo.setCutting(true);
        shopLogo.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success, Bitmap bitmap, String path) {
                shopLogo.setImageBitmap(bitmap);
                final ImageView imageView = new ImageView(StaffStoreMaintenance.this);
                imageView.setTag(path);

                localImgPath = path;
                logo=path;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        asynUpLoadHtmlImg(imageView);
                    }
                });
            }
        });

        super.initView();
        obtainStoreDetails();

    }

    @Override
    public void initData() {
        super.initData();
        staffService = new StaffService();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        //curGoods = (Goods) getIntent().getSerializableExtra(Goods.class.getSimpleName());
    }
    private void refreshImgContainer() {
        plusContainer.removeAllViews();
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
            //plusContainer.addView(message);
        }

        else if (imgList.size() >= 4 && imgList.size() < 8) {
            mContainerUploadImageView2.addView(mImgUpLoadHtml);
            //mContainerUploadImageView2.addView(message);
        }

        else if (imgList.size() >= 8 && imgList.size() < 13) {
            alltitle.removeView(btn_Confirm);
            mContainerUploadImageView3.addView(mImgUpLoadHtml);
            mContainerUploadImageView3.addView(btn_Confirm);
            //mContainerUploadImageView3.addView(message);
        }
    }
    //获取店铺详情
    private void obtainStoreDetails() {
        new StaffService().obtainStoreDetails(new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast("获取店铺详情失败");
            }

            @Override
            public void onEXECSuccess(String result) {
                try {

                    storeDetails= XmlUtils.readXML2BeanList(result, StoreDetails.class, "DATA", Mapping.storeDetailsMapping).get(0);
                    logo=storeDetails.getImgPath();
                } catch (Exception e) {
                    onError(e, -1);
                }
                ViewInject.toast("获取店铺详情成功");
            }

            @Override
            public void onComplete(String result) {
                String newProof = null;
                try {
                    newProof = DATAxmlHelper.getAllAttribute(result).get("凭据");
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                WnbApplication.getInstance().getStaff().setProof(newProof);
            }
        });
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
        if (!BitmapUtil.compressImage(StaffStoreMaintenance.this, file.getPath(), newPath)) {
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
                        logo=path;
                        LogUtils.e("path:" + path);
                        compressImagePath.add(newPath);
                        htmlImgPath.add(AppConfig.PATH_PREFIX + path);
                        //长按删除
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(StaffStoreMaintenance.this);
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
                        //点击删除
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StaffStoreMaintenance.this);
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
                                        if (imgList.contains(imageView)) {
                                            imgList.remove(imageView);
                                            refreshImgContainer();
                                            htmlImgPath.remove(AppConfig.PATH_PREFIX + path);
                                            if (compressFile != null && compressFile.exists()) {
                                                compressFile.delete();
                                            }
                                        }
                                    }
                                });
                                builder.show();

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


    //调用接口上传图片
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

//        if (localImgPath.contains("File")) {
//            goodsImgPath = localImgPath;
//            //addNewGoods();
//            return;
//        }

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
                    try {
                        goodsImgPath = DATAxmlHelper.getAllAttribute(result).get("图片路径");
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

    public boolean verificationInput(){
        inputFromView();
        if(StringUtils.isEmpty(contactPerson)){
            ViewInject.toast("请输入联系人");
            return false;
        }
        if(StringUtils.isEmpty(contactPhone)){
            ViewInject.toast("请输入联系人电话");
            return false;
        }
        return true;
    }

    public void inputFromView(){
        contactPerson=edit_contactPerson.getText().toString();
        contactPhone=edit_contactPhone.getText().toString();
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
                //mTvGoodsType.setText(curName);
                threeWheelPopDelegate.disMiss();
            }

            @Override
            public void onNegativeClick(String curName, String curMark) {
                threeWheelPopDelegate.disMiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (upLoadType) {
            case GOODSIMG:
                shopLogo.setOnActivityResult(requestCode, resultCode, data);
                break;
            case HTMLIMG:
                mImgUpLoadHtml.setOnActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        shopLogo.recycle();
        mImgUpLoadHtml.recycle();
        for (String path : compressImagePath) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        super.onDestroy();
    }
    public String initTitleText() {
        return "店面维护";
    }

}
