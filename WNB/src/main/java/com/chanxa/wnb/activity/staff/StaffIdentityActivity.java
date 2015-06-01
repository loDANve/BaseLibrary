package com.chanxa.wnb.activity.staff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StaffService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.utils.BitmapUtil;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.UploadImageView;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffIdentityActivity extends DefaultTitleActivity {
    ProgressDialog progressDialog;
    @InjectView(R.id.img_upLoadSfz_Positive)
    UploadImageView mImgUpLoadSfzPositive;
    @InjectView(R.id.tv_lable_sfz_Positive)
    TextView mTvLableSfzPositive;
    @InjectView(R.id.img_upLoadSfz_Back)
    UploadImageView mImgUpLoadSfzBack;
    @InjectView(R.id.tv_lable_sfz_Back)
    TextView mTvLableSfzBack;
    @InjectView(R.id.btn_uploadsfz)
    Button mBtnUploadsfz;

    private StaffService staffService;
    private File compressPositiveFile, compressBackFile;

    public enum SfzStyle {
        POSITIVE, BACK
    }

    private SfzStyle sfzStyle;

    private String positiveImgUrl, backImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_identity);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        initUpLoadImageView(mImgUpLoadSfzPositive, mTvLableSfzPositive);
        initUpLoadImageView(mImgUpLoadSfzBack, mTvLableSfzBack);
        mImgUpLoadSfzPositive.setUploadImageViewOnClick(new UploadImageView.UploadImageViewOnClick() {
            @Override
            public void onclick(View v) {
                sfzStyle = SfzStyle.POSITIVE;
            }
        });
        mImgUpLoadSfzBack.setUploadImageViewOnClick(new UploadImageView.UploadImageViewOnClick() {
            @Override
            public void onclick(View v) {
                sfzStyle = SfzStyle.BACK;
            }
        });
        mBtnUploadsfz.setOnClickListener(this);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
    }

    private void initUpLoadImageView(final UploadImageView uploadImageView, final TextView lable) {
        uploadImageView.setActivity(this);
        uploadImageView.setCutting(false);
        uploadImageView.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success, Bitmap bitmap, String path) {
                lable.setText("");
                WnbApplication.getInstance().getwBitmap().display(uploadImageView, path);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        staffService = new StaffService();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        upLoadSfzImg();
    }

    //上传正面后上传反面,在提交修改
    private void upLoadSfzImg() {
        if (mImgUpLoadSfzPositive.getPathArrayList().size() < 1) {
            ViewInject.toast("请选择身份证的正面照片");
            return;
        }
        if (mImgUpLoadSfzBack.getPathArrayList().size() < 1) {
            ViewInject.toast("请选择身份证的反面照片");
            return;
        }
        String positivePath = mImgUpLoadSfzPositive.getPathArrayList().get(mImgUpLoadSfzPositive.getPathArrayList().size() - 1);
        String backPath = mImgUpLoadSfzBack.getPathArrayList().get(mImgUpLoadSfzBack.getPathArrayList().size() - 1);
        final File positiveFile = new File(positivePath);
        final File backFile = new File(backPath);
        if (!positiveFile.exists() || !backFile.exists()) {
            ViewInject.toast("上传的图片不存在内存介质中,请检查设备状态后重新上传");
            return;
        }
        final String newPositivePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/temp" + StringUtils.md5(UUID.randomUUID() + "") + ".jpg";
        if (!BitmapUtil.compressImage(StaffIdentityActivity.this, positiveFile.getPath(), newPositivePath)) {
            ViewInject.toast("上传的图片不存在内存介质中,请检查设备状态后重新上传");
            return;
        }
        final String newBackPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/temp" + StringUtils.md5(UUID.randomUUID() + "") + ".jpg";
        if (!BitmapUtil.compressImage(StaffIdentityActivity.this, positiveFile.getPath(), newBackPath)) {
            ViewInject.toast("上传的图片不存在内存介质中,请检查设备状态后重新上传");
            return;
        }
        compressPositiveFile = new File(newPositivePath);
        compressBackFile = new File(newBackPath);
        if (!compressPositiveFile.exists() || !compressBackFile.exists()) {
            ViewInject.toast("上传的图片不存在内存介质中,请检查设备状态后重新上传");
            return;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(compressPositiveFile);
            staffService.upLoadImage(Base64Helper.encode(FileUtils.input2byte(inputStream)), new BaseCallBack() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onError(Exception e, int stateCode) {
                    ViewInject.toast("图片上传失败");
                    LogUtils.e(e + "");
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECisFalse(String errroMsg) {
                    ViewInject.toast(errroMsg);
                    LogUtils.e(errroMsg);
                    progressDialog.dismiss();
                }

                @Override
                public void onEXECSuccess(String result) {
                    ViewInject.toast(result);
                    InputStream inputStream2 = null;
                    try {
                        inputStream2 = new FileInputStream(compressBackFile);
                        try {
                            positiveImgUrl = DATAxmlHelper.getAllAttribute(result).get("图片路径");
                            staffService.upLoadImage(Base64Helper.encode(FileUtils.input2byte(inputStream2)), new BaseCallBack() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onError(Exception e, int stateCode) {
                                    ViewInject.toast("图片上传失败");
                                    LogUtils.e(e + "");
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onEXECisFalse(String errroMsg) {
                                    ViewInject.toast(errroMsg);
                                    LogUtils.e(errroMsg);
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onEXECSuccess(String result) {
                                    //2个图片都上传成功
                                    try {
                                        backImgUrl = DATAxmlHelper.getAllAttribute(result).get("图片路径");
                                        addStaffSfz();
                                    } catch (DocumentException e) {
                                        onError(e, -1);
                                    }
                                }

                                @Override
                                public void onComplete(String result) {

                                }
                            });
                        } catch (DocumentException e) {
                            onError(e, -1);
                        }

                    } catch (FileNotFoundException e) {
                        onError(e, -1);
                    } finally {
                        FileUtils.closeIO(inputStream2);
                    }
                }

                @Override
                public void onComplete(String result) {

                }
            });
        } catch (FileNotFoundException e) {
            ViewInject.toast("内存介质损坏,操作失败");
        } finally {
            FileUtils.closeIO(inputStream);
        }
    }

    public void addStaffSfz() {
        if (StringUtils.isEmpty(positiveImgUrl) || StringUtils.isEmpty(backImgUrl)) {
            ViewInject.toast("图片未上传成功");
            progressDialog.dismiss();
            return;
        }
        staffService.addSfzImage(WnbApplication.getInstance().getStaff(), positiveImgUrl, backImgUrl, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("信息提交失败");
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
                    WnbApplication.getInstance().getStaff().setPersonIDIsExist("1");
                    ViewInject.toast("提交成功,请等待审核");
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

    @Override
    public String initTitleText() {
        return "身份认证";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (sfzStyle) {
            case POSITIVE:
                mImgUpLoadSfzPositive.setOnActivityResult(requestCode, resultCode, data);
                break;
            case BACK:
                mImgUpLoadSfzBack.setOnActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mImgUpLoadSfzPositive.recycle();
        mImgUpLoadSfzBack.recycle();
        if (compressPositiveFile != null && compressPositiveFile.exists()) {
            compressPositiveFile.delete();
        }
        if (compressBackFile != null &&compressBackFile.exists()) {
            compressBackFile.delete();
        }
        super.onDestroy();
    }
}