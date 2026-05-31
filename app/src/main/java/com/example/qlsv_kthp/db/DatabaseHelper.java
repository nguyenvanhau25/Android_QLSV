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
import com.example.qlsv_kthp.model.TaiLieu;
import com.example.qlsv_kthp.model.ThoiKhoaBieu;
import com.example.qlsv_kthp.model.ThongBao;
import com.example.qlsv_kthp.util.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp hỗ trợ quản lý cơ sở dữ liệu SQLite - Refactored Version 4.2
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLSV.db";
    private static final int DATABASE_VERSION = 11;

    public static final String TABLE_TAI_KHOAN = "TAIKHOAN";
    public static final String TABLE_SINH_VIEN = "SINHVIEN";
    public static final String TABLE_LOP = "LOP";
    public static final String TABLE_MON_HOC = "MONHOC";
    public static final String TABLE_DIEM = "DIEM";
    public static final String TABLE_DIEM_DANH = "DIEMDANH";
    public static final String TABLE_THONG_BAO = "THONGBAO";
    public static final String TABLE_THOI_KHOA_BIEU = "THOIKHOABIEU";
    public static final String TABLE_TAI_LIEU = "TAILIEU";

    // Môn học Columns
    public static final String COL_MH_ID = "maMH";
    public static final String COL_MH_TEN = "tenMH";
    public static final String COL_MH_TINCHI = "soTinChi";
    public static final String COL_MH_GV = "giangVien";
    public static final String COL_MH_LICHHOC = "lichHoc";
    public static final String COL_MH_TAILIEU = "taiLieu";
    public static final String COL_MH_SOLUONGMAX = "soLuongMax";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LOP + " (" +
                "maLop INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenLop TEXT NOT NULL, " +
                "khoaHoc TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SINH_VIEN + " (" +
                "maSV INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "hoTen TEXT NOT NULL, " +
                "ngaySinh TEXT, " +
                "gioiTinh TEXT, " +
                "email TEXT, " +
                "soDienThoai TEXT, " +
                "diaChi TEXT, " +
                "maLop INTEGER, " +
                "FOREIGN KEY(maLop) REFERENCES LOP(maLop))");

        db.execSQL("CREATE TABLE " + TABLE_TAI_KHOAN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "hoTen TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "maSV INTEGER DEFAULT -1)");

        db.execSQL("CREATE TABLE " + TABLE_MON_HOC + " (" +
                COL_MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MH_TEN + " TEXT NOT NULL, " +
                COL_MH_GV + " TEXT, " +
                COL_MH_TINCHI + " INTEGER NOT NULL, " +
                COL_MH_LICHHOC + " TEXT, " +
                COL_MH_TAILIEU + " TEXT, " +
                COL_MH_SOLUONGMAX + " INTEGER DEFAULT 65)");

        db.execSQL("CREATE TABLE " + TABLE_DIEM + " (" +
                "maDiem INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSV INTEGER, " +
                "maMH INTEGER, " +
                "diemGiuaKy REAL, " +
                "diemCuoiKy REAL, " +
                "diemBaiTap REAL, " +
                "hocKy TEXT, " +
                "FOREIGN KEY(maSV) REFERENCES SINHVIEN(maSV), " +
                "FOREIGN KEY(maMH) REFERENCES MONHOC(maMH))");

        db.execSQL("CREATE TABLE " + TABLE_DIEM_DANH + " (" +
                "maDiemDanh INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSV INTEGER, " +
                "maMH INTEGER, " +
                "ngay TEXT, " +
                "trangThai INTEGER, " +
                "FOREIGN KEY(maSV) REFERENCES SINHVIEN(maSV), " +
                "FOREIGN KEY(maMH) REFERENCES MONHOC(maMH))");

        db.execSQL("CREATE TABLE " + TABLE_THONG_BAO + " (" +
                "maThongBao INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tieuDe TEXT NOT NULL, " +
                "noiDung TEXT NOT NULL, " +
                "ngayTao TEXT NOT NULL, " +
                "daDoc INTEGER DEFAULT 0, " +
                "loai TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_THOI_KHOA_BIEU + " (" +
                "maTKB INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSV INTEGER, " +
                "maMH INTEGER, " +
                "thu INTEGER, " +
                "tietBatDau INTEGER, " +
                "tietKetThuc INTEGER, " +
                "phongHoc TEXT, " +
                "tenGiangVien TEXT, " +
                "tuan TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_TAI_LIEU + " (" +
                "maTL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maMH INTEGER, " +
                "tenFile TEXT, " +
                "loaiFile TEXT, " +
                "kichThuoc TEXT, " +
                "ngayDang TEXT, " +
                "urlOrPath TEXT)");

        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 11) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINH_VIEN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM_DANH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THONG_BAO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THOI_KHOA_BIEU);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_LIEU);
            onCreate(db);
        }
    }

    private void insertSampleData(SQLiteDatabase db) {
        ContentValues v = new ContentValues();
        v.put("tenLop", "CNTT K18");
        v.put("khoaHoc", "2023-2027");
        long lopId = db.insert(TABLE_LOP, null, v);

        ContentValues admin = new ContentValues();
        admin.put("hoTen", "Admin User");
        admin.put("role", "admin");
        admin.put("username", "admin");
        admin.put("password", SecurityUtils.sha256("admin123"));
        admin.put("email", "admin@qlsv.com");
        admin.put("maSV", -1);
        db.insert(TABLE_TAI_KHOAN, null, admin);

        ContentValues sv = new ContentValues();
        sv.put("hoTen", "Nguyễn Văn An");
        sv.put("ngaySinh", "01/01/2004");
        sv.put("gioiTinh", "Nam");
        sv.put("email", "sv01@qlsv.com");
        sv.put("soDienThoai", "0987654321");
        sv.put("diaChi", "Hà Nội");
        sv.put("maLop", lopId);
        long maSV = db.insert(TABLE_SINH_VIEN, null, sv);

        ContentValues user = new ContentValues();
        user.put("hoTen", "Nguyễn Văn An");
        user.put("role", "student");
        user.put("username", "sv01");
        user.put("password", SecurityUtils.sha256("123456"));
        user.put("email", "sv01@qlsv.com");
        user.put("maSV", maSV);
        db.insert(TABLE_TAI_KHOAN, null, user);

        ContentValues mh = new ContentValues();
        mh.put(COL_MH_TEN, "Java nâng cao");
        mh.put(COL_MH_GV, "GV Nguyễn Văn B");
        mh.put(COL_MH_TINCHI, 3);
        mh.put(COL_MH_LICHHOC, "Thứ 2 - Tiết 1-3");
        mh.put(COL_MH_TAILIEU, "Tài liệu Java");
        mh.put(COL_MH_SOLUONGMAX, 65);
        long maMH = db.insert(TABLE_MON_HOC, null, mh);

        ContentValues diem = new ContentValues();
        diem.put("maSV", maSV);
        diem.put("maMH", maMH);
        diem.put("hocKy", "HK1-2025");
        diem.put("diemGiuaKy", 8);
        diem.put("diemCuoiKy", 9);
        diem.put("diemBaiTap", 8);
        db.insert(TABLE_DIEM, null, diem);

        // Thứ 2
        ContentValues tkb1 = new ContentValues();
        tkb1.put("maSV", maSV);
        tkb1.put("maMH", maMH);
        tkb1.put("thu", 2);
        tkb1.put("tietBatDau", 1);
        tkb1.put("tietKetThuc", 3);
        tkb1.put("phongHoc", "A101");
        tkb1.put("tenGiangVien", "GV Nguyễn Văn B");
        tkb1.put("tuan", "14/04/2026 - 19/04/2026");
        db.insert(TABLE_THOI_KHOA_BIEU, null, tkb1);

        // Thứ 4
        ContentValues tkb2 = new ContentValues();
        tkb2.put("maSV", maSV);
        tkb2.put("maMH", maMH);
        tkb2.put("thu", 4);
        tkb2.put("tietBatDau", 4);
        tkb2.put("tietKetThuc", 6);
        tkb2.put("phongHoc", "A102");
        tkb2.put("tenGiangVien", "GV Nguyễn Văn B");
        tkb2.put("tuan", "14/04/2026 - 19/04/2026");
        db.insert(TABLE_THOI_KHOA_BIEU, null, tkb2);

        // ================== MÔN HỌC 2 ==================
        ContentValues mh2 = new ContentValues();
        mh2.put(COL_MH_TEN, "Lập trình Android");
        mh2.put(COL_MH_GV, "GV Trần Văn C");
        mh2.put(COL_MH_TINCHI, 3);
        mh2.put(COL_MH_LICHHOC, "Thứ 3 - Tiết 4-6");
        mh2.put(COL_MH_TAILIEU, "Android Studio");
        long maMH2 = db.insert(TABLE_MON_HOC, null, mh2);

        // ================== MÔN HỌC 3 ==================
        ContentValues mh3 = new ContentValues();
        mh3.put(COL_MH_TEN, "Cơ sở dữ liệu");
        mh3.put(COL_MH_GV, "GV Lê Thị D");
        mh3.put(COL_MH_TINCHI, 4);
        mh3.put(COL_MH_LICHHOC, "Thứ 5 - Tiết 1-3");
        mh3.put(COL_MH_TAILIEU, "SQL Server");
        long maMH3 = db.insert(TABLE_MON_HOC, null, mh3);

        // Android
        ContentValues tkb3 = new ContentValues();
        tkb3.put("maSV", maSV);
        tkb3.put("maMH", maMH2);
        tkb3.put("thu", 3);
        tkb3.put("tietBatDau", 4);
        tkb3.put("tietKetThuc", 6);
        tkb3.put("phongHoc", "B201");
        tkb3.put("tenGiangVien", "GV Trần Văn C");
        tkb3.put("tuan", "14/04/2026 - 19/04/2026");
        db.insert(TABLE_THOI_KHOA_BIEU, null, tkb3);

        // CSDL
        ContentValues tkb4 = new ContentValues();
        tkb4.put("maSV", maSV);
        tkb4.put("maMH", maMH3);
        tkb4.put("thu", 5);
        tkb4.put("tietBatDau", 1);
        tkb4.put("tietKetThuc", 3);
        tkb4.put("phongHoc", "C301");
        tkb4.put("tenGiangVien", "GV Lê Thị D");
        tkb4.put("tuan", "14/04/2026 - 19/04/2026");
        db.insert(TABLE_THOI_KHOA_BIEU, null, tkb4);

        ContentValues diem2 = new ContentValues();
        diem2.put("maSV", maSV);
        diem2.put("maMH", maMH2);
        diem2.put("hocKy", "HK1-2025");
        diem2.put("diemGiuaKy", 7.5);
        diem2.put("diemCuoiKy", 8.5);
        diem2.put("diemBaiTap", 9);
        db.insert(TABLE_DIEM, null, diem2);

        ContentValues diem3 = new ContentValues();
        diem3.put("maSV", maSV);
        diem3.put("maMH", maMH3);
        diem3.put("hocKy", "HK1-2025");
        diem3.put("diemGiuaKy", 8);
        diem3.put("diemCuoiKy", 8);
        diem3.put("diemBaiTap", 10);
        db.insert(TABLE_DIEM, null, diem3);

        ContentValues dd1 = new ContentValues();
        dd1.put("maSV", maSV);
        dd1.put("maMH", maMH);
        dd1.put("ngay", "14/04/2026");
        dd1.put("trangThai", 1);
        db.insert(TABLE_DIEM_DANH, null, dd1);

        ContentValues dd2 = new ContentValues();
        dd2.put("maSV", maSV);
        dd2.put("maMH", maMH2);
        dd2.put("ngay", "15/04/2026");
        dd2.put("trangThai", 1);
        db.insert(TABLE_DIEM_DANH, null, dd2);

        ContentValues dd3 = new ContentValues();
        dd3.put("maSV", maSV);
        dd3.put("maMH", maMH3);
        dd3.put("ngay", "16/04/2026");
        dd3.put("trangThai", 0);
        db.insert(TABLE_DIEM_DANH, null, dd3);

        ContentValues tb2 = new ContentValues();
        tb2.put("tieuDe", "Lịch thi giữa kỳ");
        tb2.put("noiDung", "Sinh viên theo dõi lịch thi trên hệ thống.");
        tb2.put("ngayTao", "12/04/2026 09:00");
        tb2.put("daDoc", 0);
        tb2.put("loai", "exam");
        db.insert(TABLE_THONG_BAO, null, tb2);

        ContentValues tb3 = new ContentValues();
        tb3.put("tieuDe", "Nộp bài tập Android");
        tb3.put("noiDung", "Hạn nộp trước ngày 20/04/2026.");
        tb3.put("ngayTao", "13/04/2026 10:30");
        tb3.put("daDoc", 0);
        tb3.put("loai", "assignment");
        db.insert(TABLE_THONG_BAO, null, tb3);

        ContentValues tb = new ContentValues();
        tb.put("tieuDe", "Chào mừng bạn");
        tb.put("noiDung", "Chào mừng bạn đến với hệ thống quản lý sinh viên.");
        tb.put("ngayTao", "01/01/2026 08:00");
        tb.put("daDoc", 0);
        tb.put("loai", "general");
        db.insert(TABLE_THONG_BAO, null, tb);

        // ================== SV02 ==================
        ContentValues sv2 = new ContentValues();
        sv2.put("hoTen", "Trần Thị Mai");
        sv2.put("ngaySinh", "12/03/2004");
        sv2.put("gioiTinh", "Nữ");
        sv2.put("email", "sv02@qlsv.com");
        sv2.put("soDienThoai", "0911111111");
        sv2.put("diaChi", "Hải Phòng");
        sv2.put("maLop", lopId);
        long maSV2 = db.insert(TABLE_SINH_VIEN, null, sv2);

        ContentValues tk2 = new ContentValues();
        tk2.put("hoTen", "Trần Thị Mai");
        tk2.put("role", "student");
        tk2.put("username", "sv02");
        tk2.put("password", SecurityUtils.sha256("123456"));
        tk2.put("email", "sv02@qlsv.com");
        tk2.put("maSV", maSV2);
        db.insert(TABLE_TAI_KHOAN, null, tk2);

        // ================== SV03 ==================
        ContentValues sv3 = new ContentValues();
        sv3.put("hoTen", "Lê Văn Nam");
        sv3.put("ngaySinh", "20/08/2004");
        sv3.put("gioiTinh", "Nam");
        sv3.put("email", "sv03@qlsv.com");
        sv3.put("soDienThoai", "0922222222");
        sv3.put("diaChi", "Nam Định");
        sv3.put("maLop", lopId);
        long maSV3 = db.insert(TABLE_SINH_VIEN, null, sv3);

        ContentValues tk3 = new ContentValues();
        tk3.put("hoTen", "Lê Văn Nam");
        tk3.put("role", "student");
        tk3.put("username", "sv03");
        tk3.put("password", SecurityUtils.sha256("123456"));
        tk3.put("email", "sv03@qlsv.com");
        tk3.put("maSV", maSV3);
        db.insert(TABLE_TAI_KHOAN, null, tk3);

        // ================== SV04 ==================
        ContentValues sv4 = new ContentValues();
        sv4.put("hoTen", "Phạm Minh Đức");
        sv4.put("ngaySinh", "05/11/2004");
        sv4.put("gioiTinh", "Nam");
        sv4.put("email", "sv04@qlsv.com");
        sv4.put("soDienThoai", "0933333333");
        sv4.put("diaChi", "Hà Nội");
        sv4.put("maLop", lopId);
        long maSV4 = db.insert(TABLE_SINH_VIEN, null, sv4);

        ContentValues tk4 = new ContentValues();
        tk4.put("hoTen", "Phạm Minh Đức");
        tk4.put("role", "student");
        tk4.put("username", "sv04");
        tk4.put("password", SecurityUtils.sha256("123456"));
        tk4.put("email", "sv04@qlsv.com");
        tk4.put("maSV", maSV4);
        db.insert(TABLE_TAI_KHOAN, null, tk4);

        // ================== SV05 ==================
        ContentValues sv5 = new ContentValues();
        sv5.put("hoTen", "Nguyễn Thu Hà");
        sv5.put("ngaySinh", "15/01/2004");
        sv5.put("gioiTinh", "Nữ");
        sv5.put("email", "sv05@qlsv.com");
        sv5.put("soDienThoai", "0944444444");
        sv5.put("diaChi", "Quảng Ninh");
        sv5.put("maLop", lopId);
        long maSV5 = db.insert(TABLE_SINH_VIEN, null, sv5);

        ContentValues tk5 = new ContentValues();
        tk5.put("hoTen", "Nguyễn Thu Hà");
        tk5.put("role", "student");
        tk5.put("username", "sv05");
        tk5.put("password", SecurityUtils.sha256("123456"));
        tk5.put("email", "sv05@qlsv.com");
        tk5.put("maSV", maSV5);
        db.insert(TABLE_TAI_KHOAN, null, tk5);

        // ================== SV06 ==================
        ContentValues sv6 = new ContentValues();
        sv6.put("hoTen", "Đỗ Quốc Huy");
        sv6.put("ngaySinh", "30/06/2004");
        sv6.put("gioiTinh", "Nam");
        sv6.put("email", "sv06@qlsv.com");
        sv6.put("soDienThoai", "0955555555");
        sv6.put("diaChi", "Bắc Ninh");
        sv6.put("maLop", lopId);
        long maSV6 = db.insert(TABLE_SINH_VIEN, null, sv6);

        ContentValues tk6 = new ContentValues();
        tk6.put("hoTen", "Đỗ Quốc Huy");
        tk6.put("role", "student");
        tk6.put("username", "sv06");
        tk6.put("password", SecurityUtils.sha256("123456"));
        tk6.put("email", "sv06@qlsv.com");
        tk6.put("maSV", maSV6);
        db.insert(TABLE_TAI_KHOAN, null, tk6);

        // SV02
        insertDiem(db, maSV2, maMH, 8.0, 8.5, 9.0);
        insertDiem(db, maSV2, maMH2, 7.5, 8.0, 8.5);
        insertDiem(db, maSV2, maMH3, 9.0, 9.5, 9.0);

        // SV03
        insertDiem(db, maSV3, maMH, 6.5, 7.0, 8.0);
        insertDiem(db, maSV3, maMH2, 7.0, 7.5, 7.0);
        insertDiem(db, maSV3, maMH3, 8.0, 8.0, 8.5);

        // SV04
        insertDiem(db, maSV4, maMH, 9.0, 9.5, 10.0);
        insertDiem(db, maSV4, maMH2, 8.5, 9.0, 9.5);
        insertDiem(db, maSV4, maMH3, 9.0, 9.0, 9.0);

        // SV05
        insertDiem(db, maSV5, maMH, 7.0, 7.5, 8.0);
        insertDiem(db, maSV5, maMH2, 8.0, 8.0, 8.0);
        insertDiem(db, maSV5, maMH3, 7.5, 8.5, 8.5);

        // SV06
        insertDiem(db, maSV6, maMH, 5.5, 6.5, 7.0);
        insertDiem(db, maSV6, maMH2, 6.0, 7.0, 7.5);
        insertDiem(db, maSV6, maMH3, 7.0, 7.5, 8.0);
    }

    private void insertDiem(SQLiteDatabase db,
                            long maSV,
                            long maMH,
                            double gk,
                            double ck,
                            double bt) {

        ContentValues diem = new ContentValues();
        diem.put("maSV", maSV);
        diem.put("maMH", maMH);
        diem.put("hocKy", "HK1-2025");
        diem.put("diemGiuaKy", gk);
        diem.put("diemCuoiKy", ck);
        diem.put("diemBaiTap", bt);

        db.insert(TABLE_DIEM, null, diem);
    }

    // --- SINH VIÊN QUERIES ---

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "ORDER BY s.maSV DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<SinhVien> searchSinhVien(String queryText) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "WHERE s.hoTen LIKE ? OR s.maSV LIKE ?";
        String wildcardQuery = "%" + queryText + "%";
        Cursor cursor = db.rawQuery(query, new String[]{wildcardQuery, wildcardQuery});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public SinhVien getSinhVienById(int maSV) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "WHERE s.maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        if (cursor != null && cursor.moveToFirst()) {
            SinhVien sv = mapSinhVien(cursor);
            cursor.close();
            return sv;
        }
        return null;
    }

    public List<SinhVien> getSinhVienByMonHoc(int maMH) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "JOIN " + TABLE_DIEM + " d ON s.maSV = d.maSV " +
                "WHERE d.maMH = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maMH)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public int deleteSinhVien(int maSV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_DIEM, "maSV = ?", new String[]{String.valueOf(maSV)});
            db.delete(TABLE_DIEM_DANH, "maSV = ?", new String[]{String.valueOf(maSV)});
            db.delete(TABLE_TAI_KHOAN, "maSV = ?", new String[]{String.valueOf(maSV)});
            db.delete(TABLE_THOI_KHOA_BIEU, "maSV = ?", new String[]{String.valueOf(maSV)});
            int rows = db.delete(TABLE_SINH_VIEN, "maSV = ?", new String[]{String.valueOf(maSV)});
            db.setTransactionSuccessful();
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.endTransaction();
        }
    }

    public List<TaiLieu> getTaiLieuByMonHoc(int maMH, String type) {
        List<TaiLieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "maMH = ?";
        String[] selectionArgs;
        if (type == null || type.equals("Tất cả")) {
            selectionArgs = new String[]{String.valueOf(maMH)};
        } else {
            selection += " AND loaiFile = ?";
            selectionArgs = new String[]{String.valueOf(maMH), type};
        }
        Cursor cursor = db.query(TABLE_TAI_LIEU, null, selection, selectionArgs, null, null, "maTL DESC");
        while (cursor.moveToNext()) {
            list.add(mapTaiLieu(cursor));
        }
        cursor.close();
        return list;
    }

    private TaiLieu mapTaiLieu(Cursor cursor) {
        TaiLieu tl = new TaiLieu();
        tl.setMaTL(cursor.getInt(cursor.getColumnIndexOrThrow("maTL")));
        tl.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
        tl.setTenFile(cursor.getString(cursor.getColumnIndexOrThrow("tenFile")));
        tl.setLoaiFile(cursor.getString(cursor.getColumnIndexOrThrow("loaiFile")));
        tl.setKichThuoc(cursor.getString(cursor.getColumnIndexOrThrow("kichThuoc")));
        tl.setNgayDang(cursor.getString(cursor.getColumnIndexOrThrow("ngayDang")));
        tl.setUrlOrPath(cursor.getString(cursor.getColumnIndexOrThrow("urlOrPath")));
        return tl;
    }

    /**
     * Thêm mới một sinh viên
     * @param sv Đối tượng sinh viên cần thêm
     * @return ID của sinh viên vừa thêm, hoặc -1 nếu lỗi
     */
    public long insertSinhVien(SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoTen", sv.getHoTen());
        values.put("ngaySinh", sv.getNgaySinh());
        values.put("gioiTinh", sv.getGioiTinh());
        values.put("email", sv.getEmail());
        values.put("soDienThoai", sv.getSoDienThoai());
        values.put("diaChi", sv.getDiaChi());
        values.put("maLop", sv.getMaLop());
        return db.insert(TABLE_SINH_VIEN, null, values);
    }

    /**
     * Đếm số lượng bản ghi trong một bảng bất kỳ
     */
    public int getCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    /**
     * Đếm số lượng sinh viên đã đăng ký môn học
     */
    public int countStudentsInSubject(int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_DIEM + " WHERE maMH = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maMH)});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    /**
     * Lấy danh sách môn học sinh viên đã đăng ký
     */
    public List<MonHoc> getRegisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m.* FROM " + TABLE_MON_HOC + " m " +
                "JOIN " + TABLE_DIEM + " d ON m.maMH = d.maMH " +
                "WHERE d.maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        while (cursor.moveToNext()) {
            list.add(mapMonHoc(cursor));
        }
        cursor.close();
        return list;
    }

    /**
     * Lấy danh sách môn học sinh viên chưa đăng ký
     */
    public List<MonHoc> getUnregisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MON_HOC + " WHERE " + COL_MH_ID +
                " NOT IN (SELECT maMH FROM " + TABLE_DIEM + " WHERE maSV = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        while (cursor.moveToNext()) {
            list.add(mapMonHoc(cursor));
        }
        cursor.close();
        return list;
    }

    /**
     * Đăng ký môn học cho sinh viên
     */
    public long registerSubject(int maSV, int maMH) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSV", maSV);
        values.put("maMH", maMH);
        return db.insert(TABLE_DIEM, null, values);
    }

    /**
     * Lấy tất cả môn học
     */
    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null, null, null, null, null, COL_MH_TEN + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapMonHoc(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Xóa môn học
     */
    public boolean deleteMonHoc(int maMH) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MON_HOC, COL_MH_ID + " = ?", new String[]{String.valueOf(maMH)}) > 0;
    }

    /**
     * Lấy thông tin môn học theo ID
     */
    public MonHoc getMonHocById(int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null, COL_MH_ID + " = ?", new String[]{String.valueOf(maMH)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            MonHoc mh = mapMonHoc(cursor);
            cursor.close();
            return mh;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    private MonHoc mapMonHoc(Cursor cursor) {
        MonHoc mh = new MonHoc();
        mh.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)));
        mh.setTenMH(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN)));
        mh.setSoTinChi(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_TINCHI)));
        mh.setGiangVien(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_GV)));
        mh.setLichHoc(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_LICHHOC)));
        mh.setTaiLieu(cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TAILIEU)));
        mh.setSoLuongMax(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_SOLUONGMAX)));
        return mh;
    }

    /**
     * Thêm mới một môn học
     */
    public long insertMonHoc(MonHoc mh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, mh.getTenMH());
        values.put(COL_MH_TINCHI, mh.getSoTinChi());
        values.put(COL_MH_GV, mh.getGiangVien());
        values.put(COL_MH_LICHHOC, mh.getLichHoc());
        values.put(COL_MH_TAILIEU, mh.getTaiLieu());
        values.put(COL_MH_SOLUONGMAX, mh.getSoLuongMax());
        return db.insert(TABLE_MON_HOC, null, values);
    }

    /**
     * Cập nhật thông tin môn học
     */
    public int updateMonHoc(MonHoc mh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, mh.getTenMH());
        values.put(COL_MH_TINCHI, mh.getSoTinChi());
        values.put(COL_MH_GV, mh.getGiangVien());
        values.put(COL_MH_LICHHOC, mh.getLichHoc());
        values.put(COL_MH_TAILIEU, mh.getTaiLieu());
        values.put(COL_MH_SOLUONGMAX, mh.getSoLuongMax());
        return db.update(TABLE_MON_HOC, values, COL_MH_ID + " = ?", new String[]{String.valueOf(mh.getMaMH())});
    }

    /**
     * Lấy lịch sử điểm danh của sinh viên theo môn học
     */
    public List<DiemDanh> getDiemDanhBySinhVien(int maSV, int maMH) {
        List<DiemDanh> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DIEM_DANH + " WHERE maSV = ? AND maMH = ? ORDER BY ngay DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), String.valueOf(maMH)});
        while (cursor.moveToNext()) {
            DiemDanh dd = new DiemDanh();
            dd.setMaDiemDanh(cursor.getInt(cursor.getColumnIndexOrThrow("maDiemDanh")));
            dd.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
            dd.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
            dd.setNgay(cursor.getString(cursor.getColumnIndexOrThrow("ngay")));
            dd.setTrangThai(cursor.getInt(cursor.getColumnIndexOrThrow("trangThai")));
            list.add(dd);
        }
        cursor.close();
        return list;
    }

    public int updateSinhVien(SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoTen", sv.getHoTen());
        values.put("ngaySinh", sv.getNgaySinh());
        values.put("gioiTinh", sv.getGioiTinh());
        values.put("email", sv.getEmail());
        values.put("soDienThoai", sv.getSoDienThoai());
        values.put("diaChi", sv.getDiaChi());
        values.put("maLop", sv.getMaLop());
        return db.update(TABLE_SINH_VIEN, values, "maSV = ?", new String[]{String.valueOf(sv.getMaSV())});
    }

    public float getGPA(int maSV) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT AVG((diemBaiTap * 0.2) + (diemGiuaKy * 0.3) + (diemCuoiKy * 0.5)) FROM " + TABLE_DIEM + " WHERE maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        float gpa = 0;
        if (cursor != null && cursor.moveToFirst()) {
            gpa = cursor.getFloat(0);
            cursor.close();
        }
        return gpa;
    }

    public float getGPAHocKy(int maSV, String hocKy) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT AVG((diemBaiTap * 0.2) + (diemGiuaKy * 0.3) + (diemCuoiKy * 0.5)) " +
                "FROM " + TABLE_DIEM + " " +
                "WHERE maSV = ? AND hocKy = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), hocKy});
        float gpa = 0;
        if (cursor != null && cursor.moveToFirst()) {
            gpa = cursor.getFloat(0);
            cursor.close();
        }
        return gpa;
    }

    // --- LỚP QUERIES ---

    public List<Lop> getAllLop() {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOP, null, null, null, null, null, "tenLop ASC");
        if (cursor.moveToFirst()) {
            do {
                Lop lop = new Lop();
                lop.setMaLop(cursor.getInt(cursor.getColumnIndexOrThrow("maLop")));
                lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow("tenLop")));
                lop.setKhoaHoc(cursor.getString(cursor.getColumnIndexOrThrow("khoaHoc")));
                list.add(lop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- THỜI KHÓA BIỂU ---

    public List<ThoiKhoaBieu> getTKBBySinhVienAndThu(int maSV, int thu) {
        List<ThoiKhoaBieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tkb.*, mh.tenMH FROM " + TABLE_THOI_KHOA_BIEU + " tkb " +
                "LEFT JOIN " + TABLE_MON_HOC + " mh ON tkb.maMH = mh.maMH " +
                "WHERE tkb.maSV = ? AND tkb.thu = ? " +
                "ORDER BY tkb.tietBatDau ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), String.valueOf(thu)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ThoiKhoaBieu tkb = new ThoiKhoaBieu();
                tkb.setMaTKB(cursor.getInt(cursor.getColumnIndexOrThrow("maTKB")));
                tkb.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                tkb.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                tkb.setThu(cursor.getInt(cursor.getColumnIndexOrThrow("thu")));
                tkb.setTietBatDau(cursor.getInt(cursor.getColumnIndexOrThrow("tietBatDau")));
                tkb.setTietKetThuc(cursor.getInt(cursor.getColumnIndexOrThrow("tietKetThuc")));
                tkb.setPhongHoc(cursor.getString(cursor.getColumnIndexOrThrow("phongHoc")));
                tkb.setTenGiangVien(cursor.getString(cursor.getColumnIndexOrThrow("tenGiangVien")));
                tkb.setTenMH(cursor.getString(cursor.getColumnIndexOrThrow("tenMH")));
                list.add(tkb);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // --- THÔNG BÁO ---

    public int markThongBaoAsRead(int maThongBao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("daDoc", 1);
        return db.update(TABLE_THONG_BAO, values, "maThongBao = ?", new String[]{String.valueOf(maThongBao)});
    }

    public long insertThongBao(ThongBao tb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tieuDe", tb.getTieuDe());
        values.put("noiDung", tb.getNoiDung());
        values.put("ngayTao", tb.getNgayTao());
        values.put("daDoc", tb.getDaDoc());
        values.put("loai", tb.getLoai());
        return db.insert(TABLE_THONG_BAO, null, values);
    }

    /**
     * Cập nhật thông báo
     */
    public int updateThongBao(ThongBao tb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tieuDe", tb.getTieuDe());
        values.put("noiDung", tb.getNoiDung());
        values.put("loai", tb.getLoai());
        return db.update(TABLE_THONG_BAO, values, "maThongBao = ?", new String[]{String.valueOf(tb.getMaThongBao())});
    }

    /**
     * Lấy danh sách tất cả thông báo
     */
    public List<ThongBao> getAllThongBao() {
        List<ThongBao> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THONG_BAO, null, null, null, null, null, "maThongBao DESC");
        while (cursor != null && cursor.moveToNext()) {
            list.add(mapThongBao(cursor));
        }
        if (cursor != null) cursor.close();
        return list;
    }

    /**
     * Đếm số lượng thông báo chưa đọc
     */
    public int countUnreadThongBao() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_THONG_BAO + " WHERE daDoc = 0";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    /**
     * Xóa một thông báo
     */
    public int deleteThongBao(int maThongBao) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_THONG_BAO, "maThongBao = ?", new String[]{String.valueOf(maThongBao)});
    }

    private ThongBao mapThongBao(Cursor cursor) {
        ThongBao tb = new ThongBao();
        tb.setMaThongBao(cursor.getInt(cursor.getColumnIndexOrThrow("maThongBao")));
        tb.setTieuDe(cursor.getString(cursor.getColumnIndexOrThrow("tieuDe")));
        tb.setNoiDung(cursor.getString(cursor.getColumnIndexOrThrow("noiDung")));
        tb.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow("ngayTao")));
        tb.setDaDoc(cursor.getInt(cursor.getColumnIndexOrThrow("daDoc")));
        tb.setLoai(cursor.getString(cursor.getColumnIndexOrThrow("loai")));
        return tb;
    }

    // --- ĐIỂM DANH ---

    public long insertDiemDanh(DiemDanh dd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSV", dd.getMaSV());
        values.put("maMH", dd.getMaMH());
        values.put("ngay", dd.getNgay());
        values.put("trangThai", dd.getTrangThai());
        return db.insert(TABLE_DIEM_DANH, null, values);
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, new String[]{"id"}, "username = ?", new String[]{username}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, new String[]{"id"}, "email = ?", new String[]{email}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public boolean resetPassword(String username, String email, String newHashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newHashedPassword);
        return db.update(TABLE_TAI_KHOAN, values, "username = ? AND email = ?", new String[]{username, email}) > 0;
    }

    public TaiKhoan checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null, "username=? AND password=?", new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = mapTaiKhoan(cursor);
            cursor.close();
            return tk;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public TaiKhoan getTaiKhoanById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = mapTaiKhoan(cursor);
            cursor.close();
            return tk;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    /**
     * Đổi mật khẩu cho người dùng
     */
    public boolean changePassword(int userId, String oldHashedPassword, String newHashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newHashedPassword);
        return db.update(TABLE_TAI_KHOAN, values, "id = ? AND password = ?",
                new String[]{String.valueOf(userId), oldHashedPassword}) > 0;
    }

    public long registerStudentAccount(TaiKhoan tk, SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues svValues = new ContentValues();
            svValues.put("hoTen", sv.getHoTen());
            svValues.put("email", sv.getEmail());
            svValues.put("soDienThoai", sv.getSoDienThoai());
            svValues.put("maLop", sv.getMaLop());

            long maSV = db.insert(TABLE_SINH_VIEN, null, svValues);
            if (maSV == -1) return -1;

            ContentValues tkValues = new ContentValues();
            tkValues.put("username", tk.getUsername());
            tkValues.put("password", tk.getPassword());
            tkValues.put("hoTen", tk.getHoTen());
            tkValues.put("email", tk.getEmail());
            tkValues.put("role", tk.getRole());
            tkValues.put("maSV", (int) maSV);

            long accountId = db.insert(TABLE_TAI_KHOAN, null, tkValues);
            if (accountId == -1) return -1;

            db.setTransactionSuccessful();
            return accountId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    public List<Diem> getDiemBySinhVien(int maSV, String hocKy) {
        List<Diem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.*, m.tenMH FROM " + TABLE_DIEM + " d " +
                "LEFT JOIN " + TABLE_MON_HOC + " m ON d.maMH = m.maMH " +
                "WHERE d.maSV = ? AND d.hocKy = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), hocKy});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Diem d = new Diem();
                d.setMaDiem(cursor.getInt(cursor.getColumnIndexOrThrow("maDiem")));
                d.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                d.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                d.setDiemGiuaKy(cursor.getFloat(cursor.getColumnIndexOrThrow("diemGiuaKy")));
                d.setDiemCuoiKy(cursor.getFloat(cursor.getColumnIndexOrThrow("diemCuoiKy")));
                d.setDiemBaiTap(cursor.getFloat(cursor.getColumnIndexOrThrow("diemBaiTap")));
                d.setHocKy(cursor.getString(cursor.getColumnIndexOrThrow("hocKy")));
                int idx = cursor.getColumnIndex("tenMH");
                if (idx != -1) d.setTenMH(cursor.getString(idx));
                list.add(d);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // --- HELPER MAPPERS ---

    private SinhVien mapSinhVien(Cursor cursor) {
        SinhVien sv = new SinhVien();
        sv.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
        sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow("hoTen")));
        sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        sv.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")));
        sv.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow("diaChi")));
        sv.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow("ngaySinh")));
        sv.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow("gioiTinh")));
        sv.setMaLop(cursor.getInt(cursor.getColumnIndexOrThrow("maLop")));
        int idx = cursor.getColumnIndex("tenLop");
        if (idx != -1) sv.setTenLop(cursor.getString(idx));
        return sv;
    }

    private TaiKhoan mapTaiKhoan(Cursor cursor) {
        return new TaiKhoan(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getString(cursor.getColumnIndexOrThrow("hoTen")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("role")),
                cursor.getInt(cursor.getColumnIndexOrThrow("maSV"))
        );
    }
}
