package com.afroplatypus.oliniaExpertos;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "OliniaExpertosAuth";
    private static final String KEY_LOGGED_IN = "LoggedIn";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_ID = "UserID";
    // Shared pref mode
    private static int PRIVATE_MODE = 0;
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean getLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }

    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserID() {
        return pref.getString(KEY_USER_ID, null);
    }

    public void setUserID(String userID) {
        editor.putString(KEY_USER_ID, userID);
        editor.commit();
    }
}
