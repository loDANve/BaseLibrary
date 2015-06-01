package com.chanxa.wnb.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.adapter.StoreListViewAdapter;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.MakeService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.view.AddSubView;
import com.chanxa.wnb.view.StretchedListView;
import com.chanxa.wnb.view.popwindow.MakeTimeWheelPopDelegate;
import com.chanxa.wnb.view.popwindow.OneWheelPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MakeDetailsActivity extends DefaultTitleActivity implements View.OnClickListener {

    @InjectView(R.id.tv_makePerson)
    TextView mTvMakePerson;
    @InjectView(R.id.tv_makeName)
    TextView mTvMakeName;
    @InjectView(R.id.img_icon_makeDetailsPhone)
    ImageView mImgIconMakeDetailsPhone;
    @InjectView(R.id.tv_contactName)
    TextView mTvContactName;
    @InjectView(R.id.tv_contactPhone)
    TextView mTvContactPhone;
    @InjectView(R.id.tv_makeStartData)
    TextView mTvMakeStartData;
    @InjectView(R.id.tv_makeStartTime)
    TextView mTvMakeStartTime;
    @InjectView(R.id.tv_makeEndData)
    TextView mTvMakeEndData;
    @InjectView(R.id.tv_makeEndTime)
    TextView mTvMakeEndTime;
    @InjectView(R.id.numberPicker)
    AddSubView mNumberPicker;
    @InjectView(R.id.edit_makeDetailsRemark)
    EditText mEditMakeDetailsRemark;
    @InjectView(R.id.img_make)
    ImageView mImgMake;
    @InjectView(R.id.tv_detailsAddress)
    TextView mTvDetailsAddress;
    @InjectView(R.id.tv_makeDetails)
    TextView mTvMakeDetails;
    @InjectView(R.id.listView_makeDetailsImg)
    StretchedListView mListViewMakeDetailsImg;

    private WBitmap wBitmap;
    private AlertDialog dialog;
    private MakeTimeWheelPopDelegate makeTimeWheelPopDelegate;
    private Store store;
    private EditText edit_MakeDetailsDialogName, edit_MakeDetailsDialogPhone;
    private Button clearName, clearPhone;
    private String currentDate = "";//当前选择的日期
    private String startTime = "";
    private String endTime = "";
    private Goods curSelectGoods;
    private final int requestCode_makePerson = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_details);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.make);
    }

    @Override
    public void initData() {
        super.initData();
        wBitmap = WnbApplication.getInstance().getwBitmap();
        store = (Store) getIntent().getSerializableExtra("store");
        if (store == null) {
            return;
        }

        new StoreService().obtainStoreDetails(getCardToken(), store.getMark(), new BaseCallBack() {
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
            public void onEXECSuccess(String result) {
                try {
                    Document document = DocumentHelper.parseText(result);
                    List<Node> elementArrayList = (ArrayList<Node>) document.selectNodes("//子项");
                    if (elementArrayList.size() <= 0) {
                        return;
                    }
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (Node node : elementArrayList) {
                        Element element = (Element) node;
                        arrayList.add(element.attributeValue("图片路径"));
                    }
                    mListViewMakeDetailsImg.setAdapter(new StoreImgAdapter(getBaseContext(), arrayList));
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
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        findViewById(R.id.container_makeTime).setOnClickListener(this);
        findViewById(R.id.btn_subMake).setOnClickListener(this);
        findViewById(R.id.container_makePerson).setOnClickListener(this);
        findViewById(R.id.container_contactData).setOnClickListener(this);
        Card card = WnbApplication.getInstance().getCard();
        if (card != null) {
            mTvContactName.setText(card.getName());
            mTvContactPhone.setText(card.getPhone());
        }
        wBitmap.display(mImgMake, AppConfig.PATH_PREFIX + store.getImgPath());
        mTvMakeName.setText(store.getName());
        mTvMakeDetails.setText(store.getReMark() + "");
        mTvDetailsAddress.setText(store.getDetailsAddress());
        mImgIconMakeDetailsPhone.setOnClickListener(this);

        makeTimeWheelPopDelegate = new MakeTimeWheelPopDelegate(this);
        makeTimeWheelPopDelegate.setOnButtonclick(new MakeTimeWheelPopDelegate.onDateButtonclick() {
            @Override
            public void onPositivebtnClick(String year, String month, String day, String hour, String minute) {
                //ViewInject.toast(year + ":" + month + ":" + day + ":" + hour + ":" + minute);
                if (month.length() < 2) {
                    month = "0" + month;
                }
                if (day.length() < 2) {
                    day = "0" + day;
                }
                currentDate = year + "-" + month + "-" + day;
                startTime = hour;
                endTime = minute;
                mTvMakeStartData.setText(currentDate);
                mTvMakeEndData.setText(currentDate);
                mTvMakeStartTime.setText(hour);
                mTvMakeEndTime.setText(minute);
            }

            @Override
            public void onNegativebtnClick(String year, String month, String day, String hour, String minute) {

            }
        });
        //init dialog
        View popContentView = LayoutInflater.from(this).inflate(R.layout.pop_data_change, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popContentView);
        dialog = builder.create();
        popContentView.findViewById(R.id.btn_dataCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        popContentView.findViewById(R.id.btn_dataSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edit_MakeDetailsDialogPhone.getText().toString();
                Pattern phonePattern = Pattern
                        .compile("^1[3|4|5|7|8]\\d{9}$");
                if (StringUtils.isEmpty(phone) || !phonePattern.matcher(phone).matches()) {
                    ViewInject.toast("请输入正确的手机号码");
                    return;
                }
                mTvContactName.setText(edit_MakeDetailsDialogName.getText());
                mTvContactPhone.setText(edit_MakeDetailsDialogPhone.getText());
                dialog.dismiss();
            }
        });

        edit_MakeDetailsDialogName = (EditText) popContentView.findViewById(R.id.edit_MakeDetailsDialogName);
        edit_MakeDetailsDialogPhone = (EditText) popContentView.findViewById(R.id.edit_MakeDetailsDialogPhone);

        clearName = (Button) popContentView.findViewById(R.id.btn_clear_name);
        clearPhone = (Button) popContentView.findViewById(R.id.btn_clear_phone);
        clearName.setOnClickListener(this);
        clearPhone.setOnClickListener(this);

        //设置当前时间为默认对话框时间
        String dateStr = SystemTool.getDataTime("yyyyMMddHHmm");
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(4, 6);
        String day = dateStr.substring(6, 8);
        String hour = dateStr.substring(8, 10);
        String minute = dateStr.substring(10, 12);
        startTime = hour;
        endTime = minute;
        makeTimeWheelPopDelegate.setCurrentDate(year + "年", month + "月", day + "日", hour, minute);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode_makePerson) {
            if (resultCode == RESULT_OK) {
                Goods goods = (Goods) data.getSerializableExtra(Goods.class.getSimpleName());
                mTvMakePerson.setText(goods.getName());
                curSelectGoods = goods;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_makeTime:
                makeTimeWheelPopDelegate.show();
                break;
            case R.id.container_makePerson:
                if (store == null || StringUtils.isEmpty(store.getMark())) {
                    return;
                }
                Intent intent = new Intent(this, MakePersonActivity.class);
                intent.putExtra("storeMark", store.getMark());
                startActivityForResult(intent, requestCode_makePerson);
                break;
            case R.id.btn_subMake:
                subMake();
                break;
            case R.id.btn_clear_name:
                edit_MakeDetailsDialogName.setText("");
                return;
            case R.id.btn_clear_phone:
                edit_MakeDetailsDialogPhone.setText("");
                return;
            case R.id.container_contactData:
                //弹出对话框
                if (dialog != null) {
                    edit_MakeDetailsDialogName.setText(mTvContactName.getText() + "");
                    edit_MakeDetailsDialogPhone.setText(mTvContactPhone.getText() + "");
                    dialog.show();
                }
                break;
            case R.id.img_icon_makeDetailsPhone:
                //打电话
                Intent intentPhone = new Intent();
                intentPhone.setAction("android.intent.action.DIAL");
                intentPhone.setData(Uri.parse("tel:" + store.getPhone()));
                startActivity(intentPhone);
                break;
        }
    }

    /**
     * 提交预约
     */
    private void subMake() {
        if (curSelectGoods == null) {
            ViewInject.toast("请选择预约人员");
            return;
        }
        String reMark = StringUtils.isEmpty(mEditMakeDetailsRemark.getText().toString()) ? "" : mEditMakeDetailsRemark.getText().toString();
        String contactName = mTvContactName.getText().toString();
        String contactPhone = mTvContactPhone.getText().toString();
        if (StringUtils.isEmpty(contactName)) {
            ViewInject.toast("联系人不能为空");
            return;
        }
        if (StringUtils.isEmpty(contactPhone)) {
            ViewInject.toast("联系人电话不能为空");
            return;
        }
        if (StringUtils.isEmpty(currentDate) || StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            ViewInject.toast("预约时间不能为空");
            return;
        }
     /*   if (curSelectGoods == null) {
            ViewInject.toast("预约人员不能为空");
            return;
        }*/

        new MakeService().addOnLineMake(getCardToken(), store, Integer.parseInt(curSelectGoods.getMark()), contactName, contactPhone, currentDate, startTime + "-" + endTime, mNumberPicker.getNumber() + "", reMark, new BaseCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                ViewInject.toast("预约失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                ViewInject.toast("预约成功");
                startActivity(new Intent(MakeDetailsActivity.this, MyMakeActivity.class));
            }

            @Override
            public void onComplete(String result) {

            }
        });
    }

    private class StoreImgAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> pathArraylist;

        private StoreImgAdapter(Context context, ArrayList<String> pathArraylist) {
            this.context = context;
            this.pathArraylist = pathArraylist;
        }

        @Override
        public int getCount() {
            return pathArraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(context);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, DeviceUtils.dip2px(getBaseContext(), 20), 0, 0);
            convertView.setLayoutParams(layoutParams);
            convertView.setPadding(0, 0, 0, 0);
            ((ImageView) convertView).setScaleType(ImageView.ScaleType.FIT_XY);
            String path = pathArraylist.get(position);
            if (!StringUtils.isEmpty(path)) {
                if (path.startsWith(AppConfig.PATH_PREFIX)) {
                    wBitmap.display(convertView, path);
                } else {
                    wBitmap.display(convertView, AppConfig.PATH_PREFIX + path);
                }
            }
            return convertView;
        }
    }
}
