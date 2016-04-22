package com.pay.chip.easypay.pages.person.fragment;

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
import com.pay.chip.easypay.util.AsyncCircleImageView;


public class PersonFragment extends Fragment {

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

    private void initView() {
        userLoginLayout = (RelativeLayout) mRootView.findViewById(R.id.user_login_layout);
        titleText = (TextView) mRootView.findViewById(R.id.title_text);
        userCenterIcon = (AsyncCircleImageView)mRootView. findViewById(R.id.user_center_icon);

        headIconArr = (ImageView) mRootView.findViewById(R.id.head_icon_arr);
        userCenterName = (TextView) mRootView.findViewById(R.id.user_center_name);
        userNew = (RelativeLayout) mRootView.findViewById(R.id.user_new);
        myOrderIV = (ImageView)mRootView. findViewById(R.id.myOrderIV);
        myNewRedDotIV = (ImageView)mRootView. findViewById(R.id.myNewRedDotIV);
        myShare = (TextView) mRootView.findViewById(R.id.my_share);
        userShare = (RelativeLayout)mRootView. findViewById(R.id.user_share);
        userShareIcon = (TextView)mRootView. findViewById(R.id.user_share_icon);
        userShareArr = (ImageView) mRootView.findViewById(R.id.user_share_arr);
        userOption = (RelativeLayout)mRootView. findViewById(R.id.user_option);
        userOptionIcon = (TextView) mRootView.findViewById(R.id.user_option_icon);
        userOptionArr = (ImageView)mRootView. findViewById(R.id.user_option_arr);
        userHelp = (RelativeLayout)mRootView. findViewById(R.id.user_help);
        userHelpIcon = (TextView)mRootView. findViewById(R.id.user_help_icon);
        userHelpArr = (ImageView)mRootView. findViewById(R.id.user_help_arr);
        userVersion = (RelativeLayout) mRootView.findViewById(R.id.user_version);
        userVersionIcon = (TextView) mRootView.findViewById(R.id.user_version_icon);
        userVersionArr = (ImageView)mRootView. findViewById(R.id.user_version_arr);
        versionCode = (TextView)mRootView. findViewById(R.id.version_code);
        userAbout = (RelativeLayout) mRootView.findViewById(R.id.user_about);
        userAboutIcon = (TextView)mRootView. findViewById(R.id.user_about_icon);
        userAboutArr = (ImageView)mRootView. findViewById(R.id.user_about_arr);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.personal_main_fragment, container, false);
        initView();
        Bitmap bitmap = ((BitmapDrawable)userCenterIcon.getDrawable()).getBitmap();

        Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant =
                                palette.getVibrantSwatch();
                        // If we have a vibrant color
                        // update the title TextView
                        userLoginLayout.setBackgroundColor(
                                vibrant.getRgb());
                        userCenterName.setTextColor(
                                vibrant.getTitleTextColor());
                    }
                });
        return mRootView;
    }





}
