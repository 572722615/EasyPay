package com.pay.chip.easypay.pages.main.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.discount.fragment.DiscountFragment;
import com.pay.chip.easypay.pages.main.view.MainActNavigateView;
import com.pay.chip.easypay.pages.merchant.fragment.MerchantFragment;
import com.pay.chip.easypay.pages.person.fragment.PersonFragment;

public class MainActivity extends FragmentActivity {

    private MerchantFragment merchantFragment = null;
    private DiscountFragment discountFragment = null;
    private PersonFragment personFragment = null;

    private NavigateTab curBottomTab = null;

    private MainActNavigateView mainActNavigateView;

    public enum NavigateTab {
        NAVIGATE_MERCHANT,
        NAVIGATE_DISCOUNT,
        NAVIGATE_PERSON
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        merchantFragment = new MerchantFragment();
        discountFragment = new DiscountFragment();
        personFragment = new PersonFragment();

        curBottomTab = NavigateTab.NAVIGATE_MERCHANT;
        initView();
        initFragment();
    }

    private void initView() {
        mainActNavigateView = (MainActNavigateView) findViewById(R.id.mainActNavigateView);
        mainActNavigateView.setCallback(new MainActNavigateView.ICallback() {

            @Override
            public void onClicked(byte component) {
                switch (component) {
                    case MainActNavigateView.Component.TAB_MERCHANT: {
                        switchToNavTab(NavigateTab.NAVIGATE_MERCHANT);
                        break;
                    }
                    case MainActNavigateView.Component.TAB_DISCOUNT: {
                        switchToNavTab(NavigateTab.NAVIGATE_DISCOUNT);
                        break;
                    }
                    case MainActNavigateView.Component.TAB_PERSON: {
                        switchToNavTab(NavigateTab.NAVIGATE_PERSON);
                        break;
                    }

                }
            }
        });
    }

    private void initFragment() {
        switchToNavTab(curBottomTab);
    }

    public void switchToNavTab(NavigateTab tab) {
        if (tab == null) {
            return;
        }
        curBottomTab = tab;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (tab == NavigateTab.NAVIGATE_MERCHANT) {

                String wifiTag = MerchantFragment.class.getSimpleName();

                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag(wifiTag) == null) {
                    ft.add(R.id.main_activity_fragment_container,
                            merchantFragment, wifiTag);
                }

                ft.show(merchantFragment);
                ft.hide(discountFragment);
                ft.hide(personFragment);
                ft.commitAllowingStateLoss();
                mainActNavigateView.setSelected(MainActNavigateView.Component.TAB_MERCHANT);
            } else if (tab == NavigateTab.NAVIGATE_DISCOUNT) {

                String wifiTag = DiscountFragment.class.getSimpleName();

                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag(wifiTag) == null) {
                    ft.add(R.id.main_activity_fragment_container,
                            discountFragment, wifiTag);
                }

                ft.hide(merchantFragment);
                ft.hide(personFragment);
                ft.show(discountFragment);
                ft.commitAllowingStateLoss();
                mainActNavigateView.setSelected(MainActNavigateView.Component.TAB_DISCOUNT);
            } else if (tab == NavigateTab.NAVIGATE_PERSON) {

                String wifiTag = PersonFragment.class.getSimpleName();

                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag(wifiTag) == null) {
                    ft.add(R.id.main_activity_fragment_container,
                            personFragment, wifiTag);
                }

                ft.hide(merchantFragment);
                ft.hide(discountFragment);
                ft.show(personFragment);
                ft.commitAllowingStateLoss();
                mainActNavigateView.setSelected(MainActNavigateView.Component.TAB_PERSON);
            }
        }
    }

}
