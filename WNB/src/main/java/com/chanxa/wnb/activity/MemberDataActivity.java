package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.Bank;
import com.chanxa.wnb.bean.Member;
import com.chanxa.wnb.bean.MemberExpansion;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.BankDao;
import com.chanxa.wnb.service.MemberService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.view.BankEditText;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.UploadImageView;
import com.chanxa.wnb.view.popwindow.OneWheelPopDelegate;
import com.chanxa.wnb.view.popwindow.YearMonthDayPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.chanxa.wnb.xml.DATAxmlHelper;
import com.chanxa.wnb.xml.XmlUtils;
import com.chanxa.wnb.xml.mapping.Mapping;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.IOUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


public class MemberDataActivity extends WnbBaseActivity {
    ProgressDialog progressDialog;
    private OneWheelPopDelegate popDelegate;
    private TextView tv_memberGender;
    private String[] genderArr = new String[]{"女", "男"};
    private UploadImageView photo_member;
    private YearMonthDayPopDelegate yearMonthDayPopDelegate;
    private ImageView img_memberDataUpdata;
    private EditText edit_memberName;
    private TextView edit_memberPhone;
    private EditText edit_memberPersonID;
    private BankEditText edit_memberBlankNumber;
    private EditText edit_blankName;
    private TextView tv_memberBirthday;
    private TextView tv_memberBlank;
    private Member member = new Member();
    private boolean isUpLoadImg = true;
    private HashMap xmlAtt = new HashMap();
    private OneWheelPopDelegate blankPopDelegate;
    private String[] blankArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data);
    }

    //获取会员拓张属性
    private void obtainExpansionData() {
        new MemberService().obtainExpansionData(getCardToken(),getCard().getMemberMark(), new ServiceCallBack<ArrayList<MemberExpansion>>() {
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
            public void onEXECSuccess(ArrayList<MemberExpansion> resultObj) {
                ArrayList<MemberExpansion> memberExpansionArrayList = resultObj;
                if (memberExpansionArrayList.size() == 0) {
                    member.setMemberExpansionArrayList(memberExpansionArrayList);
                    return;
                }
                member.setMemberExpansionArrayList(memberExpansionArrayList);
                String str = "EXT-";
                for (MemberExpansion memberExpansion : memberExpansionArrayList) {
                    if (memberExpansion.geteETP_FieldName().equals("银行卡号")) {
                        String value = (String) xmlAtt.get(str + memberExpansion.geteETP_Id());
                        edit_memberBlankNumber.setText(value);
                    }
                    if (memberExpansion.geteETP_FieldName().equals("开户银行")) {
                        String value = (String) xmlAtt.get(str + memberExpansion.geteETP_Id());
                        tv_memberBlank.setText(value);
                    }
                    if (memberExpansion.geteETP_FieldName().equals("户名")) {
                        String value = (String) xmlAtt.get(str + memberExpansion.geteETP_Id());
                        edit_blankName.setText(value);
                    }
                }
            }

            @Override
            public void onEXECSuccess(ArrayList<MemberExpansion> resultObj, int pageSize, int dataSize) {

            }
        });
    }

    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }

    @Override
    public void initView() {
        super.initView();

        edit_memberName = (EditText) findViewById(R.id.edit_memberName);
        edit_memberPhone = (TextView) findViewById(R.id.edit_memberPhone);
        edit_memberPersonID = (EditText) findViewById(R.id.edit_memberPersonID);
        edit_memberBlankNumber = (BankEditText) findViewById(R.id.edit_memberBlankNumber);
        edit_memberBlankNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String bankCardCode = edit_memberBlankNumber.getText().toString();
                Pattern phonePattern = Pattern.compile("^[0-9]{3,9}$");
                if (!phonePattern.matcher(bankCardCode).matches()) {
                    return;
                }

                int bankCode = Integer.parseInt(edit_memberBlankNumber.getText().toString().replace(" ", ""));
                quertBank(bankCode);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*final String phoneNum = et_import_phoneNum.getText().toString();
                if (phoneNum.length() > 0) {
                    btn_clear_phone.setVisibility(View.VISIBLE);
                } else {
                    btn_clear_phone.setVisibility(View.INVISIBLE);
                }
                if (phoneNum.length() < 11 || phoneNum.length() < 1) {
                    tv_ownershipOfLand.setText("");
                    tv_recharge_products.setText("");
                    rechargeProductsArrayList.clear();
                }*/
            }
        });
        edit_blankName = (EditText) findViewById(R.id.edit_blankName);
        tv_memberBirthday = (TextView) findViewById(R.id.tv_memberBirthday);
        tv_memberBlank = (TextView) findViewById(R.id.tv_memberBlank);

        img_memberDataUpdata = (ImageView) findViewById(R.id.img_memberDataUpdata);
        img_memberDataUpdata.setOnClickListener(this);
        photo_member = (UploadImageView) findViewById(R.id.photo_member);
        photo_member.setActivity(this);
        photo_member.setOnUploadImageListener(new UploadImageView.OnUploadImageListener() {
            @Override
            public void uploadImage(boolean success,Bitmap bitmap,String path) {
                photo_member.setImageBitmap(bitmap);
            }
        });
        tv_memberGender = (TextView) findViewById(R.id.tv_memberGender);
        findViewById(R.id.container_memberGender).setOnClickListener(this);
        findViewById(R.id.container_memberBirthday).setOnClickListener(this);
        findViewById(R.id.btn_loginOut).setOnClickListener(this);
        findViewById(R.id.container_memberBlabk).setOnClickListener(this);
        popDelegate = new OneWheelPopDelegate(this, genderArr);
        popDelegate.setOnButtonClick(new OneWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1) {
                tv_memberGender.setText(genderArr[wheelView1.getSelectedItemPosition()]);
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1) {
            }
        });

        blankPopDelegate = new OneWheelPopDelegate(this, blankArr);
        blankPopDelegate.setOnButtonClick(new OneWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1) {
                tv_memberBlank.setText(blankArr[wheelView1.getSelectedItemPosition()]);
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1) {
            }
        });

        yearMonthDayPopDelegate = new YearMonthDayPopDelegate(this);
        yearMonthDayPopDelegate.setOnYMDButtonclick(new YearMonthDayPopDelegate.onYMDButtonclick() {
            @Override
            public void onPositivebtnClick(int year, int month, int day) {
                String monthStr = "";
                String dayStr = "";
                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = month + "";
                }
                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = day + "";
                }
                String ymdStr = year + "-" + monthStr + "-" + dayStr;
                tv_memberBirthday.setText(ymdStr);
            }

            @Override
            public void onNegativebtnClick(int year, int month, int day) {

            }
        });
        progressDialog = ProgressDialogBuilder.builderDialog(this);
    }

    @Override
    public void initData() {
        super.initData();
        //blankArr = getResources().getStringArray(R.array.bankNameArr);
        getBanks();
        memberDataInit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_memberGender:
                popDelegate.show();
                break;
            case R.id.container_memberBlabk:
                blankPopDelegate.show();
                break;
            case R.id.container_memberBirthday:
                String ymStr = tv_memberBirthday.getText().toString();
                if (!StringUtils.isEmpty(ymStr)) {
                    String year = ymStr.substring(0, 4);
                    String month = ymStr.substring(5, 7);
                    String day = ymStr.substring(8, 10);
                    yearMonthDayPopDelegate.setCurrentYearMonth(year, month, day);
                }
                yearMonthDayPopDelegate.show();
                break;
            case R.id.btn_loginOut:
                WnbApplication.getInstance().clearCache();
                ViewInject.toast("退出登录");
                WActivityManager.getInstance().finishAllActivity();
                System.gc();
                startActivity(new Intent(this, UserLoginActivity.class));
                break;
            case R.id.img_memberDataUpdata:
                //如果用户刚注册没有图片则需要上传一个图片
                if (isUpLoadImg) {
                    memberImgSubmit();
                } else {
                    updataMemberData();
                }
                break;
        }
    }

    /**
     * 提交用户图片数据
     */
    private void memberImgSubmit() {
        String currentPath = "";
        //需要先提交图片
        ArrayList<String> pathArrayList = photo_member.getPathArrayList();
        LogUtils.d(pathArrayList.size() + "");
        if (pathArrayList.size() <= 0) {
            ViewInject.toast("请选择一张图片");
            return;
        }
        currentPath = pathArrayList.get(pathArrayList.size() - 1);
        File file = new File(currentPath);
        if (file.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                String imgData = Base64Helper.encode(FileUtils.input2byte(inputStream));
                BaseService service = new BaseService();
                HashMap map = new HashMap();
                map.put("文件数据", imgData);

                service.doRequest("上传照片", map, new BaseAsynCallBack(new BaseCallBack() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onError(Exception e, int stateCode) {
                        LogUtils.e(e + "");
                        ViewInject.toast("提交数据失败");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onEXECisFalse(String errroMsg) {
                        LogUtils.e(errroMsg + "");
                        ViewInject.toast(errroMsg);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onEXECSuccess(String result) {
                        LogUtils.e(result + "");
                        try {
                            String photoPath = DATAxmlHelper.getAllAttribute(result).get("照片路径");
                            member.setPhotoPath(photoPath);
                            //这里去提交用户的全部数据
                            updataMemberData();
                        } catch (DocumentException e) {
                            onError(e, 0);
                        }
                    }

                    @Override
                    public void onComplete(String result) {
                        LogUtils.e(result + "");
                    }
                }));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

        } else {
            ViewInject.toast("请检查sd卡是否正常工作后重新选择图片");
        }

    }

    /**
     * 提交用户的所有数据
     */
    private void updataMemberData() {
        if (!view2Member()) {
            ViewInject.toast("用户数据缺少必填项目,出生日期和身份证可以不填");
            return;
        }
        memberExpansionInit();
        new MemberService().updataMember(getCardToken(), getCard().getMemberMark(), member, new BaseCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                LogUtils.e(stateCode + ":" + e);
                ViewInject.toast("提交用户数据失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(String result) {
                ViewInject.toast("更新成功");
                //memberDataInit();
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onComplete(String result) {
                LogUtils.d(result);
            }
        });
    }

    /**
     * 从view提取member并验证是否已经填了必填项
     *
     * @return
     */
    private boolean view2Member() {
        boolean flag = true;
        member.setName(
                edit_memberName.getText().toString());
        //界面没有对应输入,使用NULL代替必填项目
        member.setAddress("NULL");
        member.setRemarks("NULL");

        String gender = tv_memberGender.getText().toString();
        if (gender.equals("女")) {
            member.setAppellation("0");
        }
        if (gender.equals("男")) {
            member.setAppellation("1");
        }
        member.setBirhday(tv_memberBirthday.getText().toString());
        member.setMemberMark(getCard().getMemberMark());
        member.setPersonID(edit_memberPersonID.getText().toString());
        return member.verification();
    }

    private void member2View(Member member) {
        edit_memberName.setText(member.getName());
        edit_memberPhone.setText(member.getPhone());
        tv_memberGender.setText(genderArr[Integer.parseInt(member.getAppellation())]);
        if (!StringUtils.isEmpty(member.getBirhday())) {
            tv_memberBirthday.setText(member.getBirhday());
        }
        if (!StringUtils.isEmpty(member.getPersonID())) {
            edit_memberPersonID.setText(member.getPersonID());
        }
        if (!StringUtils.isEmpty(member.getPhotoPath())) {
            WnbApplication.getInstance().getwBitmap().display(photo_member, AppConfig.PATH_PREFIX + member.getPhotoPath());
        }
    }

    /**
     * 初始化用户的数据信息,从服务器拉取
     */
    private void memberDataInit() {
        new MemberService().requestMemberData(getCardToken(),getCard().getMemberMark(), new ServiceCallBack() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("获取用户数据失败");
                progressDialog.dismiss();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                //获取用户数据成功,设置到view中
                try {
                    ArrayList<Member> memberArrayList = (ArrayList<Member>) XmlUtils.readXML2BeanList(resultObj.toString(), Member.class, "DATA", Mapping.memberMapping);
                    member = memberArrayList.get(0);
                    member2View(member);
                    isUpLoadImg = member == null || StringUtils.isEmpty(member.getPhotoPath());
                    xmlAtt = DATAxmlHelper.getAllAttribute(resultObj.toString());
                    obtainExpansionData();
                } catch (Exception e) {
                    onError(e, -1);
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo_member.setOnActivityResult(requestCode, resultCode, data);
        isUpLoadImg = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除本地截图的残余图片
        ArrayList<String> imgPathList = photo_member.getPathArrayList();
        for (String imgPath : imgPathList) {
            if (!imgPath.contains("temp")) continue;
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void memberExpansionInit() {
        ArrayList<MemberExpansion> memberExpansionList = new ArrayList<>();
        ArrayList<MemberExpansion> memberExpansionList2 = member.getMemberExpansionArrayList();
        for (MemberExpansion expansion : memberExpansionList2) {
            if (expansion.geteETP_FieldName().equals("银行卡号")) {
                expansion.seteETV_Value(edit_memberBlankNumber.getBank());
            }
            if (expansion.geteETP_FieldName().equals("开户银行")) {
                expansion.seteETV_Value(tv_memberBlank.getText().toString());
            }
            if (expansion.geteETP_FieldName().equals("户名")) {
                expansion.seteETV_Value(edit_blankName.getText().toString());
            }
            memberExpansionList.add(expansion);
        }

        member.setMemberExpansionArrayList(null);
        member.setMemberExpansionArrayList(memberExpansionList);
    }

    private void getBanks() {
        ArrayList<Bank> bankArrayList = new ArrayList<>();
        try {
            BankDao bankDao = new BankDao(this.getApplicationContext());
            bankArrayList = bankDao.getBanks();
            blankArr = new String[bankArrayList.size()];
            for (int i = 0; i < bankArrayList.size(); i++){
                blankArr[i] = bankArrayList.get(i).getBankName();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void quertBank(int checkCode){
        String bankName = null;
        try {
            BankDao bankDao = new BankDao(this.getApplicationContext());
            bankName = bankDao.quertBank(checkCode);
            if (!StringUtils.isEmpty(bankName)){
                tv_memberBlank.setText(bankName);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
