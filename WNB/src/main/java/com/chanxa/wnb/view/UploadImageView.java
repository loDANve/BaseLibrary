package com.chanxa.wnb.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.StringUtils;


public class UploadImageView extends ImageView implements OnClickListener,
        OnDismissListener, OnActivityResultListener {

    // private static final int NONE = 0X0000;
    // private static final int PHOTOHRAPH = 0X1101;// 拍照
    // private static final int PHOTOZOOM = 0X1102; // 缩放
    // private static final int PHOTORESOULT = 0X1103;// 结果

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String path;
    private OnUploadImageListener listener;
    // 图像的截图比例
    private int aspectX = 1;
    private int aspectY = 1;

    public int getOutputX() {
        return outputX;
    }

    public void setOutputX(int outputX) {
        this.outputX = outputX;
    }

    public int getOutputY() {
        return outputY;
    }

    public void setOutputY(int outputY) {
        this.outputY = outputY;
    }

    // 图像的截图宽高
    private int outputX = 300;
    private int outputY = 300;

    private Context context;

    public PopupWindow popupWindow;

    private TextView tv_sex_1;
    private TextView tv_sex_2;
    private TextView tv_cancel;

    private ArrayList<String> pathArrayList = new ArrayList<String>();

    private Activity activity;

    public UploadImageViewOnClick getUploadImageViewOnClick() {
        return uploadImageViewOnClick;
    }

    public void setUploadImageViewOnClick(UploadImageViewOnClick uploadImageViewOnClick) {
        this.uploadImageViewOnClick = uploadImageViewOnClick;
    }

    private UploadImageViewOnClick uploadImageViewOnClick;

    //是否需要裁剪
    private boolean isCutting = true;

    public UploadImageView(Context context) {
        super(context);
        this.setContext(context);
        setOnClickListener(this);
        init();
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setContext(context);
        setOnClickListener(this);
        init();
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setContext(context);
        setOnClickListener(this);
        init();
    }

    /**
     * 初始化参数
     */
    private void init() {
        initPath();
        initPopupWindow();
    }

    private void initPath() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/temp" + StringUtils.md5(UUID.randomUUID() + "") + ".jpg";
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopupWindow() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.pop_select_sex, null);
        PopItemClick pItemClick = new PopItemClick();
        tv_sex_1 = (TextView) view.findViewById(R.id.tv_sex_1);
        tv_sex_2 = (TextView) view.findViewById(R.id.tv_sex_2);
        tv_cancel = (TextView) view.findViewById(R.id.tv_sex_cancel);
        tv_sex_1.setOnClickListener(pItemClick);
        tv_sex_2.setOnClickListener(pItemClick);
        tv_cancel.setOnClickListener(pItemClick);
        popupWindow = new PopupWindow(view,
                DeviceUtils.getScreenW(WActivityManager.getInstance()
                        .topActivity()),
                DeviceUtils.getScreenH(WActivityManager.getInstance()
                        .topActivity()));
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x70000000));
        // 设置PopupWindow的弹出和消失效果
        // popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setOnDismissListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        if (uploadImageViewOnClick != null) {
            uploadImageViewOnClick.onclick(v);
        }
    }

    public interface UploadImageViewOnClick{
        void onclick(View v);
    }

    @Override
    public void onDismiss() {
        popupWindow.dismiss();
    }

    /**
     * 选择头像
     *
     * @author Administrator
     *
     */
    private class PopItemClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            initPath();
            // 类型码
            int REQUEST_CODE;
            // ViewInject.toast(path);
            switch (v.getId()) {
                case R.id.tv_sex_1:
                    try {
                        Log.d("WTM", "openCameraIntent:" + "try");
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        openCameraIntent.putExtra("return-data", true);
                        Uri imageUri = Uri.fromFile(new File(path));
                        openCameraIntent
                                .putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        activity.startActivityForResult(openCameraIntent, CROP);
                    } catch (Exception e) {
                        Log.d("WTM", e + "");
                    }
                    break;
                case R.id.tv_sex_2:
                    Intent openAlbumIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    openAlbumIntent
                            .setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                    //选择图片
                    activity.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                    break;
                case R.id.tv_sex_cancel:
                    break;
            }
            popupWindow.dismiss();
        }
    }

    /**
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        // ((Activity) getContext()).startActivityForResult(intent,
        // PHOTORESOULT);
        activity.startActivityForResult(intent, CROP_PICTURE);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

	/* 对外提供的结果************************************************************ */

    /**
     * 回调接口
     *
     * @author len
     *
     */
    public interface OnUploadImageListener {
        public void uploadImage(boolean success, Bitmap bitmap, String path);
    }

    public void setOnUploadImageListener(OnUploadImageListener listener) {
        this.listener = listener;
    }

    public void setOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri1 = null;
                    if (data != null) {
                        uri1 = data.getData();
                    } else {
                        uri1 = Uri.fromFile(new File(path));
                    }
                    if (isCutting) {
                        startPhotoZoom(uri1);
                    } else {
                        //bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = ((Activity)context).managedQuery(uri1, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        if (listener != null) {
                            //Bitmap photo = BitmapFactory.decodeFile(path);
                            listener.uploadImage(true, null, path);
                            //savePic(photo, path);
                            pathArrayList.add(path);
                        }
                    }
                    break;
                case CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        uri = Uri.fromFile(new File(path));
                    }
                    if (isCutting) {
                        startPhotoZoom(uri);
                    } else {
                        if (listener != null) {
                            Bitmap photo = BitmapFactory.decodeFile(uri.getPath());
                            listener.uploadImage(true, photo, path);
                            //savePic(photo, path);
                            pathArrayList.add(path);
                        }
                    }
                    break;

                case CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap) extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    // this.setImageBitmap(photo);
                    if (listener != null) {
                        listener.uploadImage(true, photo, path);
                        savePic(photo, path);
                        pathArrayList.add(path);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public ArrayList<String> getPathArrayList() {
        return pathArrayList;
    }

    public void setPathArrayList(ArrayList<String> pathArrayList) {
        this.pathArrayList = pathArrayList;
    }

    // 是否将选择图片设置为背景
    private boolean setBackground = true;

    /**
     * @return the setBackground
     */
    public boolean isSetBackground() {
        return setBackground;
    }

    /**
     * @param setBackground
     *            the setBackground to set
     */
    public void setSetBackground(boolean setBackground) {
        this.setBackground = setBackground;
    }

    /**
     * 设置截图的宽高比
     *
     * @param aspectX
     * @param aspectY
     */
    public void setImageMatrix(int aspectX, int aspectY) {
        if (aspectX > 0 && aspectY > 0) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
        }

    }

    /**
     * 设置裁剪图片宽高
     *
     * @param outputX
     * @param outputY
     */
    public void setIamgeOutputXY(int outputX, int outputY) {
        if (outputX > 0 && outputY > 0) {
            this.outputX = outputX;
            this.outputY = outputY;
        }
    }

    public static void savePic(Bitmap b, String filePath) {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Log.d("WTM", "SD卡不能用！");
            ViewInject.toast("SD卡不可用");
        }

        File directory = new File(filePath).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.d("WTM", "文件夹不存在不能用！");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * @param context
     *            the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }

    protected String getSystemPhotoPath(Intent data) {
        Uri originalUri = data.getData();
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(originalUri, proj, null, null,
                null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isCutting() {
        return isCutting;
    }

    public void setCutting(boolean isCutting) {
        this.isCutting = isCutting;
    }

    public void recycle() {
        for (String path : pathArrayList) {
            if (path.contains("temp")) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }


}
