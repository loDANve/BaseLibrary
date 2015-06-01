package com.chanxa.wnb.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.androidService.UpDataDownLoadService;
import com.chanxa.wnb.bean.Goods;
import com.chanxa.wnb.bean.User;
import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.dao.UserDAO;
import com.chanxa.wnb.service.CardService;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.utils.encryption.RSAEncrypt;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.ObjStorageTool;
import com.wtm.library.utils.ResourceManager;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends WnbBaseActivity {

    public WelcomeActivity() {
        setHiddenActionBar(true);
        setAllowFullScreen(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                User user = UserDAO.read(UserDAO.TAG.MEMBERTAG, WelcomeActivity.this);
                if (user != null) {
                    String name = user.getCardNumber();
                    String pwd = user.getPwd();
                    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
                        startActivity(new Intent(WelcomeActivity.this, UserLoginActivity.class));
                        finish();
                        return;
                    }
                    new CardService().login(name,pwd,true,new ServiceCallBack() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(Exception e, int stateCode) {
                            startActivity(new Intent(WelcomeActivity.this, UserLoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onEXECisFalse(String errroMsg) {
                            onError(new RuntimeException(),-1);
                        }

                        @Override
                        public void onEXECSuccess(Object resultObj) {
                            List<Goods> arrayList2 = new Select().from(Goods.class).where("1=1").execute();
                            LogUtils.e("size2:" + arrayList2.size());
                            List<Goods> arrayList = new Select().from(Goods.class).where("cardNumber = " + WnbApplication.getInstance().getCard().getCardNumber()).execute();
                            LogUtils.e("size:" + arrayList.size());
                            WnbApplication.getInstance().getGoodsCart().setGoodsList((ArrayList<Goods>) arrayList);
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        }
                        @Override
                        public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

                        }
                    });
                }
            } catch (Exception e) {
                startActivity(new Intent(WelcomeActivity.this, UserLoginActivity.class));
                finish();
            }
        }
    };

    @Override
    public void initView() {
        super.initView();
        new DataService().obtainConfig(new ServiceCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
                startNextActivity();
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                startNextActivity();
            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                WnbApplication.getInstance().setAdvertisingArrayList((java.util.ArrayList<com.chanxa.wnb.bean.Advertising>) resultObj);
                startNextActivity();
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {

            }
        });
    }

    public void startNextActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputStream inputStream = null;
                ObjectInputStream objIn = null;
                try {
                    inputStream = getAssets().open("area.obj");
                    objIn = new ObjectInputStream(inputStream);
                    Area area = (Area) objIn.readObject();
                    WnbApplication.getInstance().setArea(area);
                } catch (Exception e) {
                    LogUtils.e("area load failure");
                } finally {
                    FileUtils.closeIO(inputStream, objIn);
                    handler.sendEmptyMessage(1);
                }
            }
        }, 500);
    }
}