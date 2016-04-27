package com.pay.chip.easypay.pages.person.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.person.event.ChangeInfoEvent;
import com.pay.chip.easypay.pages.person.model.HeadInfo;
import com.pay.chip.easypay.pages.person.model.LoginUserInfo;
import com.pay.chip.easypay.pages.person.view.UserPopWindow;
import com.pay.chip.easypay.util.AsyncCircleImageView;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.SelectIconHelper;

import org.apache.http.Header;

import java.io.File;
import java.io.UnsupportedEncodingException;

import de.greenrobot.event.EventBus;

import static com.pay.chip.easypay.util.UserUtils.stripSAE;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout userInfoLayout;
    private RelativeLayout userIconLayout;
    private TextView title;
    private ImageView tag;
    private AsyncCircleImageView userIconText;
    private ProgressBar loadingProgressbar;
    private RelativeLayout userName;
    private TextView userNameIcon;
    private ImageView userNameArr;
    private TextView userNameText;
    private RelativeLayout userPhone;
    private TextView userPhoneIcon;
    private ImageView userPhoneArr;
    private TextView userPhoneText;
    private Button loginOut;

    private TextView reg_head;
    private FrameLayout user_back;

    // 弹出窗口
    private UserPopWindow menuWindow;

    private ProgressDialog mDialog ;

    private String phone;
    private String name;
    private String head;
    private Uri headUri;

    private void initView() {
        userInfoLayout = (LinearLayout) findViewById(R.id.user_info_layout);
        userIconLayout = (RelativeLayout) findViewById(R.id.user_icon_layout);
        userIconLayout.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        tag = (ImageView) findViewById(R.id.tag);
        userIconText = (AsyncCircleImageView) findViewById(R.id.user_icon_text);
        loadingProgressbar = (ProgressBar) findViewById(R.id.loadingProgressbar);
        userName = (RelativeLayout) findViewById(R.id.user_name);
        userNameIcon = (TextView) findViewById(R.id.user_name_icon);
        userNameArr = (ImageView) findViewById(R.id.user_name_arr);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhone = (RelativeLayout) findViewById(R.id.user_phone);
        userPhoneIcon = (TextView) findViewById(R.id.user_phone_icon);
        userPhoneArr = (ImageView) findViewById(R.id.user_phone_arr);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        loginOut = (Button) findViewById(R.id.login_out);
        loginOut.setOnClickListener(this);

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.info));

        mDialog = new ProgressDialog(this) ;
        mDialog.setCanceledOnTouchOutside(false);

    }



    LoginUserInfo loginUserInfo;

    public static void startUserInfoActivity(Context context, String loginUserInfo) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle b = new Bundle();
        b.putString(Constant.KEY_CARD_LOGIN_INFO, loginUserInfo);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    private void perseIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        Bundle b = intent.getExtras();
        loginUserInfo = LoginUserInfo.getFromJson(b.getString(Constant.KEY_CARD_LOGIN_INFO));
        phone = loginUserInfo.telno;
        name = loginUserInfo.name;
        head = loginUserInfo.head;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        perseIntent();
        initView();
        fillText();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void fillText() {
        if (phone != null) {
            userPhoneText.setText(phone);
        }

        if (name != null) {
            userNameText.setText(name);
        }
        if (head != null) {
            userIconText.setImageURL(head, false);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.login_out:
                LoginDataHelper.getInstance().doLogout(UserInfoActivity.this);
                break;

            case R.id.leftFL:
                finish();
                break;

            case R.id.user_icon_layout:
                    popWindowForIcon();
                    break;
        }
    }

    private void popWindowForIcon() {
        // 实例化SelectPicPopupWindow
        menuWindow = new UserPopWindow(UserInfoActivity.this, (android.view.View.OnClickListener) itemsOnClick);
        // 显示窗口
        menuWindow.showAtLocation(
                        findViewById(R.id.user_info_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private android.view.View.OnClickListener itemsOnClick = new android.view.View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.user_changeFromCamera:
                    // 拍照获取
                    SelectIconHelper.getInstance().showCameraDialog(UserInfoActivity.this);
                    break;
                // 选择本地
                case R.id.user_changeFromSD:
                    SelectIconHelper.getInstance().showSettingFaceDialog(UserInfoActivity.this);
                    break;
                default:
                    break;
            }
        }

    };

    public void onEventMainThread(ChangeInfoEvent event) {
        if(event==null){
            return;
        }
        userIconText.setImageURL(event.info.getData().toString(),false);
        LoginUserInfo data = LoginDataHelper.getInstance().getLoginUserInfo();
        data.head = event.info.getData();
        String dataInfo = LoginUserInfo.toJsonString(data);
        LoginDataHelper.getInstance().setLoginUserInfo(dataInfo);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SelectIconHelper.SELECT_PIC_CODE:
                    if (data != null) {
                        headUri = data.getData();
                        if(headUri!=null){
                            try
                            {
                                uploadImg(headUri);
                            }catch (Exception  e){

                            }
                        }
                    }
                    break;
                case SelectIconHelper.SELECT_CAMERA_CODE:

                    headUri = data.getData();
                    if(headUri!=null){
                        try
                        {
                            uploadImg(headUri);
                        }catch (Exception  e){

                        }
                    }
                    break;
                default:
                    break;
            }
        }



        }

    public void uploadImg(Uri bitmap) throws Exception {
        mDialog.setMessage("图片上传中...");
        mDialog.show();



        Uri uri = bitmap;

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);

        File file = new File(img_path);

        postFile(file);




    }


    public void postFile(File file) throws Exception{
        if(file.exists() && file.length()>0){
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("head", file);
            params.put("telno", phone);
            client.post(Constant.UPLOAD_HEAD_URL, params,new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        String s = new String(responseBody, "utf-8");
                        String res = stripSAE(s);
                        HeadInfo info = HeadInfo.getFromJson(res);
                        Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new ChangeInfoEvent(info));

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mDialog.dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(this, "文件不存在",  Toast.LENGTH_LONG).show();
        }

    }


}
