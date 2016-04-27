package com.pay.chip.easypay.pages.person.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.person.activity.AboutUsAcitivity;
import com.pay.chip.easypay.pages.person.activity.LoginActivity;
import com.pay.chip.easypay.pages.person.activity.NewsActivity;
import com.pay.chip.easypay.pages.person.activity.ShareActivity;
import com.pay.chip.easypay.pages.person.activity.UserFeedBackActivity;
import com.pay.chip.easypay.pages.person.activity.UserHelpActivity;
import com.pay.chip.easypay.pages.person.activity.UserInfoActivity;
import com.pay.chip.easypay.pages.person.event.LoginOutEvent;
import com.pay.chip.easypay.pages.person.model.LoginUserInfo;
import com.pay.chip.easypay.util.AsyncCircleImageView;
import com.pay.chip.easypay.util.CustomToast;
import com.pay.chip.easypay.util.EBErrorCode;
import com.pay.chip.easypay.util.LoginDataHelper;

import de.greenrobot.event.EventBus;


public class PersonFragment extends Fragment implements View.OnClickListener {

    private View mRootView = null;

    private RelativeLayout userLoginLayout;
    private TextView titleText;
    private AsyncCircleImageView userCenterIcon;
    private ImageView headIconArr;
    private TextView userCenterName;
    private RelativeLayout userNew;
    private ImageView myOrderIV;
    private ImageView myNewRedDotIV;
    private TextView myShare;
    private RelativeLayout userShare;
    private TextView userShareIcon;
    private ImageView userShareArr;
    private RelativeLayout userOption;
    private TextView userOptionIcon;
    private ImageView userOptionArr;
    private RelativeLayout userHelp;
    private TextView userHelpIcon;
    private ImageView userHelpArr;
    private RelativeLayout userVersion;
    private TextView userVersionIcon;
    private ImageView userVersionArr;
    private TextView versionCode;
    private RelativeLayout userAbout;
    private TextView userAboutIcon;
    private ImageView userAboutArr;

    private LoginUserInfo loginUserInfo;

    boolean isLogin;
    Bitmap bitmap;


    private void initView() {
        userLoginLayout = (RelativeLayout) mRootView.findViewById(R.id.user_login_layout);
        userLoginLayout.setOnClickListener(this);
        userCenterName = (TextView) mRootView.findViewById(R.id.user_center_name);
        userCenterName.setOnClickListener(this);
        userCenterIcon = (AsyncCircleImageView) mRootView.findViewById(R.id.user_center_icon);
        userCenterIcon.setOnClickListener(this);

        titleText = (TextView) mRootView.findViewById(R.id.title_text);

        headIconArr = (ImageView) mRootView.findViewById(R.id.head_icon_arr);
        userNew = (RelativeLayout) mRootView.findViewById(R.id.user_new);
        userNew.setOnClickListener(this);
        myOrderIV = (ImageView) mRootView.findViewById(R.id.myOrderIV);
        myNewRedDotIV = (ImageView) mRootView.findViewById(R.id.myNewRedDotIV);
        myShare = (TextView) mRootView.findViewById(R.id.my_share);
        myShare.setOnClickListener(this);
        userShare = (RelativeLayout) mRootView.findViewById(R.id.user_share);
        userShareIcon = (TextView) mRootView.findViewById(R.id.user_share_icon);
        userShareArr = (ImageView) mRootView.findViewById(R.id.user_share_arr);
        userOption = (RelativeLayout) mRootView.findViewById(R.id.user_option);
        userOption.setOnClickListener(this);
        userOptionIcon = (TextView) mRootView.findViewById(R.id.user_option_icon);
        userOptionArr = (ImageView) mRootView.findViewById(R.id.user_option_arr);
        userHelp = (RelativeLayout) mRootView.findViewById(R.id.user_help);
        userHelp.setOnClickListener(this);
        userHelpIcon = (TextView) mRootView.findViewById(R.id.user_help_icon);
        userHelpArr = (ImageView) mRootView.findViewById(R.id.user_help_arr);
        userVersion = (RelativeLayout) mRootView.findViewById(R.id.user_version);
        userVersion.setOnClickListener(this);
        userVersionIcon = (TextView) mRootView.findViewById(R.id.user_version_icon);
        userVersionArr = (ImageView) mRootView.findViewById(R.id.user_version_arr);
        versionCode = (TextView) mRootView.findViewById(R.id.version_code);
        userAbout = (RelativeLayout) mRootView.findViewById(R.id.user_about);
        userAbout.setOnClickListener(this);
        userAboutIcon = (TextView) mRootView.findViewById(R.id.user_about_icon);
        userAboutArr = (ImageView) mRootView.findViewById(R.id.user_about_arr);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        isLogin = LoginDataHelper.getInstance().isLogin();
        if(isLogin){
            loginUserInfo = LoginDataHelper.getInstance().getLoginUserInfo();
            userCenterName.setText(loginUserInfo.telno);
            if(loginUserInfo.head!=null){
                userCenterIcon.setImageURL(loginUserInfo.head,false);
            }
        }
        userCenterIcon.setDrawingCacheEnabled(true);
        bitmap = ((BitmapDrawable) userCenterIcon.getDrawable()).getBitmap();
        Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant =
                                palette.getVibrantSwatch();
                        userLoginLayout.setBackgroundColor(
                                vibrant.getRgb());
                        userCenterName.setTextColor(
                                vibrant.getTitleTextColor());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.personal_main_fragment, container, false);
        initView();
        isLogin = LoginDataHelper.getInstance().isLogin();
        if(isLogin){
            loginUserInfo = LoginDataHelper.getInstance().getLoginUserInfo();
            userCenterName.setText(loginUserInfo.telno);
            if(loginUserInfo.head!=null){
                userCenterIcon.setImageURL(loginUserInfo.head,false);
                userCenterIcon.setDrawingCacheEnabled(true);
                bitmap = ((BitmapDrawable) userCenterIcon.getDrawable()).getBitmap();
                Palette.generateAsync(bitmap,
                        new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant =
                                        palette.getVibrantSwatch();
                                userLoginLayout.setBackgroundColor(
                                        vibrant.getRgb());
                                userCenterName.setTextColor(
                                        vibrant.getTitleTextColor());
                            }
                        });
            }
        }

        return mRootView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.user_about:
                startActivity(new Intent(getActivity(), AboutUsAcitivity.class));
                break;
            case R.id.user_new:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.my_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.user_help:
                startActivity(new Intent(getActivity(), UserHelpActivity.class));
                break;
            case R.id.user_option:
                startActivity(new Intent(getActivity(), UserFeedBackActivity.class));
                break;
            case R.id.user_center_name:
            case R.id.user_login_layout:
            case R.id.user_center_icon:
                /*if (isLogin) {
                    startActivityByFlag(getActivity(), EBConstant.UserFlag.USER_INFO.toString());
                } else {
                    startActivityByFlag(getActivity(), EBConstant.UserFlag.USER_LOGIN.toString());
                }*/
                if(isLogin){
                    String loginInfo = LoginUserInfo.toJsonString(loginUserInfo);
                    UserInfoActivity.startUserInfoActivity(getActivity(),loginInfo);
                }else{
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }

    }


    public void onEventMainThread(LoginOutEvent event) {
        int code = event.code;
        if (code == EBErrorCode.EB_CODE_SUCCESS) {
            userCenterIcon.setImageResource(R.drawable.default_icon);
            String username;
            username = getResources().getString(R.string.click_login);
            userCenterName.setText(username);
            CustomToast.showToast(getResources().getString(R.string.login_out_success));
            return;
        }
    }
}
