package com.example.qlsv_kthp.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public final class RoleAccessManager {

    private RoleAccessManager() {
    }

    public static boolean isAdmin(Context context) {
        return new SessionManager(context).isAdmin();
    }

    public static boolean isStudent(Context context) {
        return !isAdmin(context);
    }

    public static boolean canAccessStudentRecord(Context context, int maSV) {
        SessionManager session = new SessionManager(context);
        return session.isAdmin() || session.getMaSV() == maSV;
    }

    public static boolean requireAdmin(Activity activity) {
        if (isAdmin(activity)) {
            return true;
        }
        Toast.makeText(activity, "Bạn không có quyền truy cập chức năng quản trị", Toast.LENGTH_SHORT).show();
        activity.finish();
        return false;
    }

    public static boolean requireStudentRecordAccess(Activity activity, int maSV) {
        if (canAccessStudentRecord(activity, maSV)) {
            return true;
        }
        Toast.makeText(activity, "Bạn không có quyền xem dữ liệu của sinh viên khác", Toast.LENGTH_SHORT).show();
        activity.finish();
        return false;
    }
}
