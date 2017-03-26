package com.afroplatypus.olinia;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by omarsaucedo on 21/03/17.
 */

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared pref mode
    private static int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "OliniaAuth";

    private static final String KEY_LOGGED_IN = "LoggedIn";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_ID = "UserID";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public void setUserID(String userID) {
        editor.putString(KEY_USER_ID, userID);
        editor.commit();
    }

    public boolean getLoggedIn(){
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getUserName(){
        return pref.getString(KEY_USER_NAME, null);
    }

    public String getUserID(){
        return pref.getString(KEY_USER_ID, null);
    }
}
