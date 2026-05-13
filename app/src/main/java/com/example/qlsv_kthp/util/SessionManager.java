package com.example.qlsv_kthp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qlsv_kthp.model.TaiKhoan;

public class SessionManager {
    private static final String PREF_NAME = "QLSV_Session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_ROLE = "role";
    private static final String KEY_MASV = "maSV";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(TaiKhoan user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_FULLNAME, user.getHoTen());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putInt(KEY_MASV, user.getMaSV());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public String getUserRole() {
        return pref.getString(KEY_ROLE, "user");
    }

    public String getFullName() {
        return pref.getString(KEY_FULLNAME, "");
    }

    public int getMaSV() {
        return pref.getInt(KEY_MASV, -1);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(getUserRole());
    }
}
