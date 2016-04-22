package com.pay.chip.easypay.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;


public class AccountUtil {

	public static Account[] getAccounts(Context context) {
		AccountManager accountManager = AccountManager
				.get(context);
		Account[] accounts = null;
		try {
			accounts = accountManager.getAccounts();
		} catch (Exception e) {
		}
		return accounts;
	}

	public static String[] getAccountNames(Context context) {
		String[] names = null;
		Account[] accounts = getAccounts(context);
		if (accounts != null) {
			names = new String[accounts.length];
			for (int i = 0; i < accounts.length; i++) {
				names[i] = accounts[i].name;
			}
		}
		return names;
	}

	public static String[] getAccountTypes(Context context) {
		String[] types = null;
		Account[] accounts = getAccounts(context);
		if (accounts != null) {
			types = new String[accounts.length];
			for (int i = 0; i < accounts.length; i++) {
				types[i] = accounts[i].type;
			}
		}
		return types;
	}

	public static String getDefaultMailAccountName(Context context) {
		String gmailName = "";
		String hotmailName = "";
		String yahooName = "";
		String otherName = "";
		String[] names = getAccountNames(context);
		if (names != null) {
			for (String name : names) {
				if (!TextUtils.isEmpty(name)
						&& isEmail(name)) {
					String lowerName = name.toLowerCase();
					if (TextUtils.isEmpty(gmailName)
							&& -1 != lowerName.lastIndexOf("@gmail.com")) {
						gmailName = lowerName;
					} else if (TextUtils.isEmpty(hotmailName)
							&& -1 != lowerName.lastIndexOf("@hotmail.com")) {
						hotmailName = lowerName;
					} else if (TextUtils.isEmpty(hotmailName)
							&& -1 != lowerName.lastIndexOf("@yahoo.com")) {
						yahooName = lowerName;
					} else {
					    otherName = lowerName;
					}
				}
			}
		}
		String userName = "";
		if (!TextUtils.isEmpty(gmailName)) {
			userName = gmailName;
		} else if (!TextUtils.isEmpty(hotmailName)) {
			userName = hotmailName;
		} else if (!TextUtils.isEmpty(yahooName)) {
			userName = yahooName;
		} else if (!TextUtils.isEmpty(otherName)) {
		    userName = otherName;
		}
		return userName;
	}

  public static boolean isEmail(String email) {
	  if(email == null ||!email.contains("@")){
		  return false;
	  }
        final String mStrEmailAddrRole =
                "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

        boolean ret = false;
        try {
            ret = email.trim().matches(mStrEmailAddrRole);
        } catch (Exception e) {
        }
        return ret;

    }
}
