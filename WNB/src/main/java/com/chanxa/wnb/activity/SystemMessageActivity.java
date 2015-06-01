package com.chanxa.wnb.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.androidService.UpDataDownLoadService;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.utils.SystemTool;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SystemMessageActivity extends DefaultTitleActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        progressDialog = ProgressDialogBuilder.builderDialog(this);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.system_message);
    }

    @Override
    public void initView() {
        super.initView();
        ((TextView)findViewById(R.id.tv_version)).setText("v" + SystemTool.getAppVersion(this));
        findViewById(R.id.container_SystemMessageFeedback).setOnClickListener(this);
        findViewById(R.id.container_SystemMessageCheckUpdata).setOnClickListener(this);
        findViewById(R.id.container_SystemMessageWelComePage).setOnClickListener(this);
        findViewById(R.id.container_SystemMessageShareApp).setOnClickListener(this);
        findViewById(R.id.container_SystemMessageScore).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_SystemMessageScore:
                messageScore();
                return;
            case R.id.container_SystemMessageFeedback:
                intent.setClass(this, FeedbackActivity.class);
                break;
            case R.id.container_SystemMessageCheckUpdata:
                //checkUpdata();
                checkVersion();
                return;
            case R.id.container_SystemMessageWelComePage:
                intent.setClass(this, WelcomepageActivity.class);
                break;
            case R.id.container_SystemMessageShareApp:
                shareApp();
                return;
        }
        startActivity(intent);
    }


    private void messageScore(){
        Uri uri = Uri.parse("market://details?id="+getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isAvilible(this, "com.tencent.android.qqdownloader")){
            intent.setPackage("com.tencent.android.qqdownloader");
        }
        startActivity(intent);
    }

    private boolean isAvilible(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    private void shareApp() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("万能宝应用下载地址：http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.chanxa.wnb");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.chanxa.com/");
        // 启动分享GUI
        oks.show(this);
    }

    //版本检查更新
    private void checkUpdata() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("当前已是最新版本");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //builder.create().dismiss();
            }
        });
        builder.create().show();
    }

    private void checkVersion() {
        progressDialog.show();
        new DataService().appVersonUpdata(this, new DataService.VersonCheckUpdata() {
            @Override
            public void haveUpdata(String newestVerson, String curVerson, final String downLoadUrl) {
                progressDialog.dismiss();
                //ViewInject.toast(downLoadUrl);
                AlertDialog alertDialog = new AlertDialog.Builder(SystemMessageActivity.this).setTitle("版本更新").setMessage("发现新版本\n版本号:" + newestVerson +
                        "\n当前版本号:" + curVerson).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SystemMessageActivity.this, UpDataDownLoadService.class);
                        //intent.setAction("appUpdata");
                        intent.putExtra(AppConfig.DOWNLOAD_URL_KEY,
                                downLoadUrl);
                        startService(intent);
                    }
                }).create();
                alertDialog.show();
            }

            @Override
            public void noUpdata() {
                progressDialog.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(SystemMessageActivity.this);
                builder.setMessage("当前已是最新版本");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //builder.create().dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
}
