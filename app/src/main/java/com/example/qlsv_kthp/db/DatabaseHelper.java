package com.example.qlsv_kthp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qlsv_kthp.model.Diem;
import com.example.qlsv_kthp.model.DiemDanh;
import com.example.qlsv_kthp.model.Lop;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.model.ThongBao;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLSV.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_TAI_KHOAN = "TAIKHOAN";
    public static final String TABLE_SINH_VIEN = "SINHVIEN";
    public static final String TABLE_LOP = "LOP";
    public static final String TABLE_MON_HOC = "MONHOC";
    public static final String TABLE_DIEM = "DIEM";
    public static final String TABLE_DIEM_DANH = "DIEMDANH";
    public static final String TABLE_THONG_BAO = "THONGBAO";

    // Table TAIKHOAN Columns
    public static final String COL_TK_ID = "id";
    public static final String COL_TK_USERNAME = "username";
    public static final String COL_TK_PASSWORD = "password";
    public static final String COL_TK_HOTEN = "hoTen";
    public static final String COL_TK_EMAIL = "email";
    public static final String COL_TK_ROLE = "role";
    public static final String COL_TK_MASV = "maSV";

    // Table LOP Columns
    public static final String COL_LOP_ID = "maLop";
    public static final String COL_LOP_TEN = "tenLop";
    public static final String COL_LOP_KHOA = "khoaHoc";

    // Table SINHVIEN Columns
    public static final String COL_SV_ID = "maSV";
    public static final String COL_SV_HOTEN = "hoTen";
    public static final String COL_SV_NGAYSINH = "ngaySinh";
    public static final String COL_SV_GIOITINH = "gioiTinh";
    public static final String COL_SV_EMAIL = "email";
    public static final String COL_SV_SDT = "soDienThoai";
    public static final String COL_SV_DIACHI = "diaChi";
    public static final String COL_SV_MALOP = "maLop";

    // Table MONHOC Columns
    public static final String COL_MH_ID = "maMH";
    public static final String COL_MH_TEN = "tenMH";
    public static final String COL_MH_TINCHI = "soTinChi";

    // Table DIEM Columns
    public static final String COL_DIEM_ID = "maDiem";
    public static final String COL_DIEM_MASV = "maSV";
    public static final String COL_DIEM_MAMH = "maMH";
    public static final String COL_DIEM_GK = "diemGiuaKy";
    public static final String COL_DIEM_CK = "diemCuoiKy";
    public static final String COL_DIEM_BT = "diemBaiTap";
    public static final String COL_DIEM_HOCKY = "hocKy";

    // Table DIEMDANH Columns
    public static final String COL_DD_ID = "maDiemDanh";
    public static final String COL_DD_MASV = "maSV";
    public static final String COL_DD_MAMH = "maMH";
    public static final String COL_DD_NGAY = "ngay";
    public static final String COL_DD_TRANGTHAI = "trangThai";

    // Table THONGBAO Columns
    public static final String COL_TB_ID = "maThongBao";
    public static final String COL_TB_TIEUDE = "tieuDe";
    public static final String COL_TB_NOIDUNG = "noiDung";
    public static final String COL_TB_NGAYTAO = "ngayTao";
    public static final String COL_TB_DADOC = "daDoc";
    public static final String COL_TB_LOAI = "loai";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create LOP table
        db.execSQL("CREATE TABLE " + TABLE_LOP + "(" +
                COL_LOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LOP_TEN + " TEXT, " +
                COL_LOP_KHOA + " TEXT)");

        // Create SINHVIEN table
        db.execSQL("CREATE TABLE " + TABLE_SINH_VIEN + "(" +
                COL_SV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SV_HOTEN + " TEXT, " +
                COL_SV_NGAYSINH + " TEXT, " +
                COL_SV_GIOITINH + " TEXT, " +
                COL_SV_EMAIL + " TEXT, " +
                COL_SV_SDT + " TEXT, " +
                COL_SV_DIACHI + " TEXT, " +
                COL_SV_MALOP + " INTEGER, " +
                "FOREIGN KEY(" + COL_SV_MALOP + ") REFERENCES " + TABLE_LOP + "(" + COL_LOP_ID + "))");

        // Create TAIKHOAN table
        db.execSQL("CREATE TABLE " + TABLE_TAI_KHOAN + "(" +
                COL_TK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TK_USERNAME + " TEXT UNIQUE, " +
                COL_TK_PASSWORD + " TEXT, " +
                COL_TK_HOTEN + " TEXT, " +
                COL_TK_EMAIL + " TEXT, " +
                COL_TK_ROLE + " TEXT, " +
                COL_TK_MASV + " INTEGER)");

        // Create MONHOC table
        db.execSQL("CREATE TABLE " + TABLE_MON_HOC + "(" +
                COL_MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MH_TEN + " TEXT, " +
                COL_MH_TINCHI + " INTEGER)");

        // Create DIEM table
        db.execSQL("CREATE TABLE " + TABLE_DIEM + "(" +
                COL_DIEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DIEM_MASV + " INTEGER, " +
                COL_DIEM_MAMH + " INTEGER, " +
                COL_DIEM_GK + " REAL, " +
                COL_DIEM_CK + " REAL, " +
                COL_DIEM_BT + " REAL, " +
                COL_DIEM_HOCKY + " TEXT, " +
                "FOREIGN KEY(" + COL_DIEM_MASV + ") REFERENCES " + TABLE_SINH_VIEN + "(" + COL_SV_ID + "), " +
                "FOREIGN KEY(" + COL_DIEM_MAMH + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + "))");

        // Create DIEMDANH table
        db.execSQL("CREATE TABLE " + TABLE_DIEM_DANH + "(" +
                COL_DD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DD_MASV + " INTEGER, " +
                COL_DD_MAMH + " INTEGER, " +
                COL_DD_NGAY + " TEXT, " +
                COL_DD_TRANGTHAI + " INTEGER, " +
                "FOREIGN KEY(" + COL_DD_MASV + ") REFERENCES " + TABLE_SINH_VIEN + "(" + COL_SV_ID + "), " +
                "FOREIGN KEY(" + COL_DD_MAMH + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + "))");

        // Create THONGBAO table
        db.execSQL("CREATE TABLE " + TABLE_THONG_BAO + "(" +
                COL_TB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TB_TIEUDE + " TEXT, " +
                COL_TB_NOIDUNG + " TEXT, " +
                COL_TB_NGAYTAO + " TEXT, " +
                COL_TB_DADOC + " INTEGER DEFAULT 0, " +
                COL_TB_LOAI + " TEXT)");

        // Insert Sample Data
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Lớp
        db.execSQL("INSERT INTO " + TABLE_LOP + " (tenLop, khoaHoc) VALUES ('CNTT K14', '2020-2024')");
        db.execSQL("INSERT INTO " + TABLE_LOP + " (tenLop, khoaHoc) VALUES ('Kế toán K15', '2021-2025')");

        // Tài khoản Admin (Mật khẩu 'admin123' sau khi hash)
        String adminPass = com.example.qlsv_kthp.util.SecurityUtils.sha256("admin123");
        db.execSQL("INSERT INTO " + TABLE_TAI_KHOAN + " (username, password, hoTen, email, role, maSV) VALUES ('admin', '" + adminPass + "', 'Quản trị viên', 'admin@school.edu.vn', 'admin', -1)");

        // Môn học
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (tenMH, soTinChi) VALUES ('Lập trình Android', 3)");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (tenMH, soTinChi) VALUES ('Cấu trúc dữ liệu', 4)");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (tenMH, soTinChi) VALUES ('Cơ sở dữ liệu', 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINH_VIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM_DANH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THONG_BAO);
        onCreate(db);
    }

    // --- Authentication ---
    public TaiKhoan checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null,
                COL_TK_USERNAME + "=? AND " + COL_TK_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = new TaiKhoan(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_HOTEN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_ROLE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TK_MASV))
            );
            cursor.close();
            return tk;
        }
        return null;
    }

    // --- Student Management ---
    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SinhVien sv = new SinhVien();
                sv.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SV_ID)));
                sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_HOTEN)));
                sv.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_NGAYSINH)));
                sv.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_GIOITINH)));
                sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_EMAIL)));
                sv.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_SDT)));
                sv.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_DIACHI)));
                sv.setMaLop(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SV_MALOP)));
                sv.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOP_TEN)));
                list.add(sv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long insertSinhVien(SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SV_HOTEN, sv.getHoTen());
        values.put(COL_SV_NGAYSINH, sv.getNgaySinh());
        values.put(COL_SV_GIOITINH, sv.getGioiTinh());
        values.put(COL_SV_EMAIL, sv.getEmail());
        values.put(COL_SV_SDT, sv.getSoDienThoai());
        values.put(COL_SV_DIACHI, sv.getDiaChi());
        values.put(COL_SV_MALOP, sv.getMaLop());
        return db.insert(TABLE_SINH_VIEN, null, values);
    }

    public int updateSinhVien(SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SV_HOTEN, sv.getHoTen());
        values.put(COL_SV_NGAYSINH, sv.getNgaySinh());
        values.put(COL_SV_GIOITINH, sv.getGioiTinh());
        values.put(COL_SV_EMAIL, sv.getEmail());
        values.put(COL_SV_SDT, sv.getSoDienThoai());
        values.put(COL_SV_DIACHI, sv.getDiaChi());
        values.put(COL_SV_MALOP, sv.getMaLop());
        return db.update(TABLE_SINH_VIEN, values, COL_SV_ID + "=?", new String[]{String.valueOf(sv.getMaSV())});
    }

    public void deleteSinhVien(int maSV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SINH_VIEN, COL_SV_ID + "=?", new String[]{String.valueOf(maSV)});
    }

    // --- Subject Management ---
    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                MonHoc mh = new MonHoc(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_TINCHI))
                );
                list.add(mh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- Class Management ---
    public List<Lop> getAllLop() {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOP, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Lop lop = new Lop(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LOP_TEN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_LOP_KHOA))
                );
                list.add(lop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- Statistics ---
    public int getCount(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public float getGPA(int maSV) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT AVG((diemBaiTap * 0.2) + (diemGiuaKy * 0.3) + (diemCuoiKy * 0.5)) FROM " + TABLE_DIEM + " WHERE maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        float gpa = 0;
        if (cursor.moveToFirst()) {
            gpa = cursor.getFloat(0);
        }
        cursor.close();
        return gpa;
    }
}
