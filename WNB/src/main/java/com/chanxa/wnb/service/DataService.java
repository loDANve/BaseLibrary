package com.chanxa.wnb.service;

import android.content.Context;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.service.base.BaseAsynCallBack;
import com.chanxa.wnb.service.base.BaseCallBack;
import com.chanxa.wnb.service.base.BaseService;
import com.chanxa.wnb.utils.encryption.Base64Helper;
import com.chanxa.wnb.xml.AreaXmlPullParser;
import com.chanxa.wnb.xml.XmlUtils;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.http.HttpAsynCallBack;
import com.wtm.library.http.HttpResponseCallBack;
import com.wtm.library.http.HttpTask;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.GZipUtils;
import com.wtm.library.utils.ObjStorageTool;
import com.wtm.library.utils.SystemTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 数据相关业务
 * Created by CHANXA on 2014/12/27.
 */
public class DataService extends BaseService {

    private final String action_obtaionArea = "获取省市区县";
    private final String action_config = "Config";

    /**
     * 从网络读取省市区县信息
     *
     * @param serviceCallBack
     */
    public void obtaionArea(final ServiceCallBack serviceCallBack) {
        doRequest(action_obtaionArea, new HashMap<String, String>(), new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                serviceCallBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                serviceCallBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                serviceCallBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                int start = result.indexOf(">");
                int end = result.lastIndexOf("<");
                String data = result.substring(start, end);
                try {
                    String dataStr = new String(GZipUtils.decompress(Base64Helper.decode(data)));
                    Area area = new AreaXmlPullParser().parserArea(dataStr);
                    serviceCallBack.onEXECSuccess(area);
                    boolean flag = ObjStorageTool.saveObjToFile(area, obtainAreaObjPath());
                } catch (Exception e) {
                    //获取省市区数据失败
                    serviceCallBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

    /**
     * 获取area对象在本地存储的路径
     *
     * @return
     */
    private String obtainAreaObjPath() {
        String path = "";
        String parentPath = "";
        if (FileUtils.checkSDcard()) {
            parentPath = FileUtils.getSDPATH() + "wnb";
        } else {
            parentPath = WActivityManager.getInstance().topActivity().getCacheDir().getAbsolutePath() + File.separator + "wnb";
        }
        File parentDir = new File(parentPath);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        return parentDir.getAbsolutePath() + File.separator + "area.obj";
    }

    /**
     * 从Sd卡读取area
     *
     * @return
     */
    public Area readAreaFromSDCard() {
        return (Area) ObjStorageTool.readObjFromFile(obtainAreaObjPath());
    }

    /**
     * 获取配置参数
     * 当前用于获取广告
     * @param callBack
     */
    public void obtainConfig(final ServiceCallBack callBack) {
        doRequest(action_config, new HashMap<String, String>(), new BaseAsynCallBack(new BaseCallBack() {
            @Override
            public void onStart() {
                callBack.onStart();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                callBack.onError(e, stateCode);
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                callBack.onEXECisFalse(errroMsg);
            }

            @Override
            public void onEXECSuccess(String result) {
                try {
                    ArrayList<Advertising> advertisingArrayList = (ArrayList<Advertising>) XmlUtils.readXML2BeanList(result, Advertising.class, "项目", null);
                    callBack.onEXECSuccess(advertisingArrayList);
                } catch (Exception e) {
                    callBack.onError(e, -1);
                }
            }

            @Override
            public void onComplete(String result) {

            }
        }));
    }

    public interface VersonCheckUpdata {
        void haveUpdata(String neweVerson,String currentVerson,String downLoadUrl);

        void noUpdata();
    }

    public void appVersonUpdata(final Context context, final VersonCheckUpdata versonCheckUpdata) {
        new HttpTask().urlGet("http://www.bi-uc.com/%E4%B8%87%E8%83%BD%E5%AE%9D/version_info.txt", new HttpAsynCallBack(new HttpResponseCallBack() {
            @Override
            public void complete(byte[] data) {
                String dataStr = new String(data);
                try {
                    JSONObject json = new JSONObject(dataStr);
                    String onlineVerson = json.getString("version");
                    String downloadUrl = json.getString("download_url");
                    StringBuffer sb1 = new StringBuffer(onlineVerson);
                    StringBuffer sb2 = new StringBuffer(SystemTool.getAppVersion(context));
                    for (int i = 0; i < sb1.length(); i++) {
                        char c1 = sb1.charAt(i);
                        char c2 = sb2.charAt(i);
                        if (c1 > c2) {
                            versonCheckUpdata.haveUpdata(onlineVerson,SystemTool.getAppVersion(context),downloadUrl);
                            return;
                        } else if (c1 < c2) {
                            versonCheckUpdata.noUpdata();
                            return;
                        }
                    }
                    versonCheckUpdata.noUpdata();
                } catch (JSONException e) {
                    error(e, "数据转json错误", -1);
                }
            }

            @Override
            public void onLoad(long count, long current, float currentDownLoadScale) {

            }

            @Override
            public void error(Exception e, Object msg, int responseCode) {
                versonCheckUpdata.noUpdata();
            }

            @Override
            public void onStart() {

            }
        }));
    }

}