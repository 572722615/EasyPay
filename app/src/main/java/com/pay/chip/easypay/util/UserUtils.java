package com.pay.chip.easypay.util;

import android.text.TextUtils;
import android.widget.Button;


public class UserUtils {
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 检验邮箱合法性
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        if (!AccountUtil.isEmail(email)) {
            return false;
        }
        return true;
    }

    /**
     * 检验密码合法性，密码长度范围6~20
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }

        if (password.length() < PASSWORD_MIN_LENGTH
                || password.length() > PASSWORD_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * 用于判断用户输入的密码是否是特殊字符
     *
     * @param inputStr
     * @return 是否为特殊字符，是=true，否=false
     */
    public static boolean isPasswordSpacialWord(CharSequence inputStr) {
        boolean isSpacialWord = false;

        if (TextUtils.isEmpty(inputStr)) {
            return isSpacialWord;
        }

        try {
            isSpacialWord = !inputStr.toString().matches("[0-9|a-z|A-Z|@|//.|//_|//-]+");
        } catch (Exception e) {
        }

        return isSpacialWord;
    }


    /**
     * 用于判断用户输入的昵称是否由数字 字母和汉字组成
     *
     * @param inputStr
     * @return 是否满足条件 是=true，否=false
     */
    public static boolean checkNickname(CharSequence inputStr) {
        boolean isValidWord = false;

        if (TextUtils.isEmpty(inputStr)) {
            return isValidWord;
        }

        try {
            isValidWord = inputStr.toString().matches("[0-9|a-z|A-Z|//_|\\u4E00-\\u9FA5]+");
        } catch (Exception e) {
        }

        return isValidWord;
    }

    /**
     * 用于判断用户输入的昵称是否全部汉字组成
     *
     * @param inputStr
     * @return 是否满足条件 是=true，否=false
     */
    public static boolean checkIsAllChineseCharacters(CharSequence inputStr) {
        boolean isValidWord = false;

        if (TextUtils.isEmpty(inputStr)) {
            return isValidWord;
        }

        try {
            isValidWord = inputStr.toString().matches("[\\u4E00-\\u9FA5]+");
        } catch (Exception e) {
        }

        return isValidWord;
    }

    //按钮改变背景
    public static void changeButtonBg(Button btn, int background) {
        btn.setBackgroundResource(background);
        btn.setPadding(10, 10, 10, 10);
    }
}
