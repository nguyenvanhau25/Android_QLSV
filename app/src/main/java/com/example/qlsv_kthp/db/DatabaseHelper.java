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
import com.example.qlsv_kthp.util.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Lop ho tro quan ly co so du lieu SQLite cho he thong QLSV.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLSV.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_TAI_KHOAN = "TAIKHOAN";
    public static final String TABLE_SINH_VIEN = "SINHVIEN";
    public static final String TABLE_LOP = "LOP";
    public static final String TABLE_MON_HOC = "MONHOC";
    public static final String TABLE_DIEM = "DIEM";
    public static final String TABLE_DIEM_DANH = "DIEMDANH";
    public static final String TABLE_THONG_BAO = "THONGBAO";
    public static final String TABLE_THOI_KHOA_BIEU = "THOIKHOABIEU";
    public static final String TABLE_TAI_LIEU = "TAILIEU";

    public static final String COL_TK_ID = "id";
    public static final String COL_TK_USERNAME = "username";
    public static final String COL_TK_PASSWORD = "password";
    public static final String COL_TK_HOTEN = "hoTen";
    public static final String COL_TK_EMAIL = "email";
    public static final String COL_TK_ROLE = "role";
    public static final String COL_TK_MASV = "maSV";

    public static final String COL_LOP_ID = "maLop";
    public static final String COL_LOP_TEN = "tenLop";
    public static final String COL_LOP_KHOA = "khoaHoc";

    public static final String COL_SV_ID = "maSV";
    public static final String COL_SV_HOTEN = "hoTen";
    public static final String COL_SV_NGAYSINH = "ngaySinh";
    public static final String COL_SV_GIOITINH = "gioiTinh";
    public static final String COL_SV_EMAIL = "email";
    public static final String COL_SV_SDT = "soDienThoai";
    public static final String COL_SV_DIACHI = "diaChi";
    public static final String COL_SV_MALOP = "maLop";

    public static final String COL_MH_ID = "maMH";
    public static final String COL_MH_TEN = "tenMH";
    public static final String COL_MH_TINCHI = "soTinChi";
    public static final String COL_MH_GV = "giangVien";

    public static final String COL_DIEM_ID = "maDiem";
    public static final String COL_DIEM_MASV = "maSV";
    public static final String COL_DIEM_MAMH = "maMH";
    public static final String COL_DIEM_GK = "diemGiuaKy";
    public static final String COL_DIEM_CK = "diemCuoiKy";
    public static final String COL_DIEM_BT = "diemBaiTap";
    public static final String COL_DIEM_HOCKY = "hocKy";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LOP + " (" +
                COL_LOP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LOP_TEN + " TEXT NOT NULL, " +
                COL_LOP_KHOA + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SINH_VIEN + " (" +
                COL_SV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SV_HOTEN + " TEXT NOT NULL, " +
                COL_SV_NGAYSINH + " TEXT, " +
                COL_SV_GIOITINH + " TEXT, " +
                COL_SV_EMAIL + " TEXT, " +
                COL_SV_SDT + " TEXT, " +
                COL_SV_DIACHI + " TEXT, " +
                COL_SV_MALOP + " INTEGER, " +
                "FOREIGN KEY(" + COL_SV_MALOP + ") REFERENCES " + TABLE_LOP + "(" + COL_LOP_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_TAI_KHOAN + " (" +
                COL_TK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TK_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_TK_PASSWORD + " TEXT NOT NULL, " +
                COL_TK_HOTEN + " TEXT NOT NULL, " +
                COL_TK_EMAIL + " TEXT NOT NULL, " +
                COL_TK_ROLE + " TEXT NOT NULL, " +
                COL_TK_MASV + " INTEGER DEFAULT -1)");

        db.execSQL("CREATE TABLE " + TABLE_MON_HOC + " (" +
                COL_MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MH_TEN + " TEXT NOT NULL, " +
                COL_MH_GV + " TEXT, " +
                COL_MH_TINCHI + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_DIEM + " (" +
                COL_DIEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DIEM_MASV + " INTEGER, " +
                COL_DIEM_MAMH + " INTEGER, " +
                COL_DIEM_GK + " REAL, " +
                COL_DIEM_CK + " REAL, " +
                COL_DIEM_BT + " REAL, " +
                COL_DIEM_HOCKY + " TEXT, " +
                "FOREIGN KEY(" + COL_DIEM_MASV + ") REFERENCES " + TABLE_SINH_VIEN + "(" + COL_SV_ID + "), " +
                "FOREIGN KEY(" + COL_DIEM_MAMH + ") REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_DIEM_DANH + " (" +
                "maDiemDanh INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSV INTEGER, " +
                "maMH INTEGER, " +
                "ngay TEXT, " +
                "trangThai INTEGER, " +
                "FOREIGN KEY(maSV) REFERENCES " + TABLE_SINH_VIEN + "(" + COL_SV_ID + "), " +
                "FOREIGN KEY(maMH) REFERENCES " + TABLE_MON_HOC + "(" + COL_MH_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_THONG_BAO + " (" +
                "maThongBao INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tieuDe TEXT NOT NULL, " +
                "noiDung TEXT NOT NULL, " +
                "ngayTao TEXT NOT NULL, " +
                "daDoc INTEGER DEFAULT 0, " +
                "loai TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_THOI_KHOA_BIEU + " (" +
                "maTKB INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSV INTEGER, maMH INTEGER, " +
                "thu INTEGER, " +
                "tietBatDau INTEGER, tietKetThuc INTEGER, " +
                "phongHoc TEXT, tenGiangVien TEXT, " +
                "tuan TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_TAI_LIEU + " (" +
                "maTL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maMH INTEGER, tenFile TEXT, " +
                "loaiFile TEXT, " +
                "kichThuoc TEXT, ngayDang TEXT, " +
                "urlOrPath TEXT)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        long cnttK14 = insertClass(db, "CNTT K18", "2023-2027");
        long cnttK15 = insertClass(db, "CNTT K19", "2024-2028");
        long qtkdK15 = insertClass(db, "QTKD K20", "2025-2029");
        long ktnnK16 = insertClass(db, "Ngon ngu Anh K19", "2024-2028");

        long android = insertSubject(db, "Lap trinh Android", 3, "Le Van Giang");
        long ctDl = insertSubject(db, "Cau truc du lieu", 4, "Nguyen Van B");
        long csdl = insertSubject(db, "Co so du lieu", 3, "Tran Thi C");
        long web = insertSubject(db, "Phat trien Web", 3, "Pham Van D");
        long mang = insertSubject(db, "Mang may tinh", 3, "Hoang Thi E");
        long xstk = insertSubject(db, "Xac suat thong ke", 2, "Vu Van F");

        long sv1 = insertStudent(db, "Nguyen Minh Anh", "12/03/2003", "Nu",
                "minhanh@sv.edu.vn", "0901234567", "Thu Duc, TP.HCM", (int) cnttK14);
        long sv2 = insertStudent(db, "Tran Quoc Bao", "28/08/2002", "Nam",
                "quocbao@sv.edu.vn", "0912345678", "Di An, Binh Duong", (int) cnttK14);
        long sv3 = insertStudent(db, "Le Hoang Nam", "15/11/2003", "Nam",
                "hoangnam@sv.edu.vn", "0983123456", "Bien Hoa, Dong Nai", (int) cnttK15);
        long sv4 = insertStudent(db, "Pham Thu Trang", "04/02/2004", "Nu",
                "thutrang@sv.edu.vn", "0977123456", "Go Vap, TP.HCM", (int) cnttK15);
        long sv5 = insertStudent(db, "Vo Gia Huy", "20/07/2003", "Nam",
                "giahuy@sv.edu.vn", "0938456123", "Tan Binh, TP.HCM", (int) qtkdK15);
        long sv6 = insertStudent(db, "Bui Khanh Linh", "09/09/2004", "Nu",
                "khanhlinh@sv.edu.vn", "0967456123", "Nha Be, TP.HCM", (int) ktnnK16);

        insertAccount(db, "admin", "admin123", "Quan tri vien", "admin@school.edu.vn", "admin", -1);
        insertAccount(db, "sv001", "giangvien123", "Le Van Giang", "giangvien@school.edu.vn", "admin", -1);
        insertAccount(db, "sv001", "sv123456", "Nguyen Minh Anh", "minhanh@sv.edu.vn", "student", (int) sv1);
        insertAccount(db, "sv002", "sv123456", "Tran Quoc Bao", "quocbao@sv.edu.vn", "student", (int) sv2);
        insertAccount(db, "sv003", "sv123456", "Le Hoang Nam", "hoangnam@sv.edu.vn", "student", (int) sv3);
        insertAccount(db, "sv004", "sv123456", "Pham Thu Trang", "thutrang@sv.edu.vn", "student", (int) sv4);

        insertScore(db, (int) sv1, (int) android, 8.0f, 8.5f, 9.0f, "HK1-2025");
        insertScore(db, (int) sv1, (int) ctDl, 7.5f, 8.0f, 8.5f, "HK1-2025");
        insertScore(db, (int) sv1, (int) csdl, 8.0f, 9.0f, 8.0f, "HK2-2025");
        insertScore(db, (int) sv2, (int) android, 6.5f, 7.0f, 7.5f, "HK1-2025");
        insertScore(db, (int) sv2, (int) web, 7.5f, 8.0f, 8.0f, "HK2-2025");
        insertScore(db, (int) sv3, (int) csdl, 8.5f, 8.5f, 8.0f, "HK1-2025");
        insertScore(db, (int) sv3, (int) mang, 7.0f, 7.5f, 7.0f, "HK2-2025");
        insertScore(db, (int) sv4, (int) web, 9.0f, 9.0f, 8.5f, "HK1-2025");
        insertScore(db, (int) sv5, (int) xstk, 7.0f, 8.0f, 7.5f, "HK1-2025");
        insertScore(db, (int) sv6, (int) xstk, 8.5f, 8.0f, 8.5f, "HK2-2025");

        insertAttendance(db, (int) sv1, (int) android, "2026-05-05", 1);
        insertAttendance(db, (int) sv1, (int) android, "2026-05-12", 1);
        insertAttendance(db, (int) sv2, (int) android, "2026-05-05", 0);
        insertAttendance(db, (int) sv2, (int) android, "2026-05-12", 1);
        insertAttendance(db, (int) sv3, (int) csdl, "2026-05-06", 1);
        insertAttendance(db, (int) sv4, (int) web, "2026-05-07", 1);
        insertAttendance(db, (int) sv5, (int) xstk, "2026-05-08", 0);
        insertAttendance(db, (int) sv6, (int) xstk, "2026-05-09", 1);

        insertNotification(db, "Thong bao lich hoc tuan 20",
                "Tat ca cac lop hoc ca sang ngay thu 6 se chuyen sang phong B205 do bao tri toa nha A.",
                "2026-05-14 08:30", 0, "general");
        insertNotification(db, "Mo dang ky mon hoc he",
                "Cong dang ky hoc phan he mo tu 08:00 ngay 16/05/2026 den 17:00 ngay 20/05/2026. Sinh vien vui long dang ky dung han.",
                "2026-05-13 15:45", 0, "general");
        insertNotification(db, "Cap nhat diem Android",
                "Diem giua ky mon Lap trinh Android da duoc cap nhat. Sinh vien co the vao muc Diem so de kiem tra.",
                "2026-05-12 10:15", 1, "score");
        insertNotification(db, "Nhac nho hoc phi hoc ky 2",
                "Han dong hoc phi hoc ky 2 nam hoc 2025-2026 la ngay 25/05/2026. Sau thoi han tren, he thong se tam khoa dang ky hoc phan.",
                "2026-05-11 09:00", 0, "general");
        insertNotification(db, "Canh bao vang hoc",
                "Sinh vien vang qua 2 buoi o mon Lap trinh Android can lien he giang vien de bo sung bai tap va xac nhan chuyen can.",
                "2026-05-10 18:20", 0, "attendance");

        // Insert TKB & TaiLieu
        insertTKB(db, (int) sv1, (int) android, 2, 1, 3, "P.A101", "Lê Văn Giang", "all");
        insertTKB(db, (int) sv1, (int) ctDl, 4, 4, 6, "P.B202", "Nguyễn Văn B", "all");
        insertTKB(db, (int) sv1, (int) csdl, 6, 7, 9, "P.C303", "Trần Thị C", "all");
        
        insertTaiLieu(db, (int) android, "Slide_Bai1_Android.pdf", "PDF", "2.5MB", "10/05/2026", "url");
        insertTaiLieu(db, (int) android, "Video_HuongDan_CaiDat.mp4", "Video", "150MB", "12/05/2026", "url");
    }

    private long insertClass(SQLiteDatabase db, String tenLop, String khoaHoc) {
        ContentValues values = new ContentValues();
        values.put(COL_LOP_TEN, tenLop);
        values.put(COL_LOP_KHOA, khoaHoc);
        return db.insert(TABLE_LOP, null, values);
    }

    private long insertStudent(SQLiteDatabase db, String hoTen, String ngaySinh, String gioiTinh,
                               String email, String soDienThoai, String diaChi, int maLop) {
        ContentValues values = new ContentValues();
        values.put(COL_SV_HOTEN, hoTen);
        values.put(COL_SV_NGAYSINH, ngaySinh);
        values.put(COL_SV_GIOITINH, gioiTinh);
        values.put(COL_SV_EMAIL, email);
        values.put(COL_SV_SDT, soDienThoai);
        values.put(COL_SV_DIACHI, diaChi);
        values.put(COL_SV_MALOP, maLop);
        return db.insert(TABLE_SINH_VIEN, null, values);
    }

    private void insertAccount(SQLiteDatabase db, String username, String rawPassword, String hoTen,
                               String email, String role, int maSV) {
        ContentValues values = new ContentValues();
        values.put(COL_TK_USERNAME, username);
        values.put(COL_TK_PASSWORD, SecurityUtils.sha256(rawPassword));
        values.put(COL_TK_HOTEN, hoTen);
        values.put(COL_TK_EMAIL, email);
        values.put(COL_TK_ROLE, role);
        values.put(COL_TK_MASV, maSV);
        db.insert(TABLE_TAI_KHOAN, null, values);
    }

    private long insertSubject(SQLiteDatabase db, String tenMH, int soTinChi, String giangVien) {
        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, tenMH);
        values.put(COL_MH_TINCHI, soTinChi);
        values.put(COL_MH_GV, giangVien);
        return db.insert(TABLE_MON_HOC, null, values);
    }

    private void insertScore(SQLiteDatabase db, int maSV, int maMH, float gk, float ck, float bt, String hocKy) {
        ContentValues values = new ContentValues();
        values.put(COL_DIEM_MASV, maSV);
        values.put(COL_DIEM_MAMH, maMH);
        values.put(COL_DIEM_GK, gk);
        values.put(COL_DIEM_CK, ck);
        values.put(COL_DIEM_BT, bt);
        values.put(COL_DIEM_HOCKY, hocKy);
        db.insert(TABLE_DIEM, null, values);
    }

    private void insertAttendance(SQLiteDatabase db, int maSV, int maMH, String ngay, int trangThai) {
        ContentValues values = new ContentValues();
        values.put("maSV", maSV);
        values.put("maMH", maMH);
        values.put("ngay", ngay);
        values.put("trangThai", trangThai);
        db.insert(TABLE_DIEM_DANH, null, values);
    }

    private void insertNotification(SQLiteDatabase db, String tieuDe, String noiDung,
                                    String ngayTao, int daDoc, String loai) {
        ContentValues values = new ContentValues();
        values.put("tieuDe", tieuDe);
        values.put("noiDung", noiDung);
        values.put("ngayTao", ngayTao);
        values.put("daDoc", daDoc);
        values.put("loai", loai);
        db.insert(TABLE_THONG_BAO, null, values);
    }

    public List<com.example.qlsv_kthp.model.ThoiKhoaBieu> getTKBBySinhVienAndThu(int maSV, int thu) {
        List<com.example.qlsv_kthp.model.ThoiKhoaBieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, m.tenMH FROM " + TABLE_THOI_KHOA_BIEU + " t " +
                "JOIN " + TABLE_MON_HOC + " m ON t.maMH = m.maMH " +
                "WHERE t.maSV = ? AND t.thu = ? ORDER BY t.tietBatDau ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), String.valueOf(thu)});
        if (cursor.moveToFirst()) {
            do {
                com.example.qlsv_kthp.model.ThoiKhoaBieu tkb = new com.example.qlsv_kthp.model.ThoiKhoaBieu();
                tkb.setMaTKB(cursor.getInt(cursor.getColumnIndexOrThrow("maTKB")));
                tkb.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                tkb.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                tkb.setThu(cursor.getInt(cursor.getColumnIndexOrThrow("thu")));
                tkb.setTietBatDau(cursor.getInt(cursor.getColumnIndexOrThrow("tietBatDau")));
                tkb.setTietKetThuc(cursor.getInt(cursor.getColumnIndexOrThrow("tietKetThuc")));
                tkb.setPhongHoc(cursor.getString(cursor.getColumnIndexOrThrow("phongHoc")));
                tkb.setTenGiangVien(cursor.getString(cursor.getColumnIndexOrThrow("tenGiangVien")));
                tkb.setTuan(cursor.getString(cursor.getColumnIndexOrThrow("tuan")));
                tkb.setTenMH(cursor.getString(cursor.getColumnIndexOrThrow("tenMH")));
                list.add(tkb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<com.example.qlsv_kthp.model.TaiLieu> getTaiLieuByMonHoc(int maMH, String loaiFile) {
        List<com.example.qlsv_kthp.model.TaiLieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query;
        String[] args;
        if (loaiFile == null || loaiFile.isEmpty() || loaiFile.equalsIgnoreCase("Tất cả")) {
            query = "SELECT * FROM " + TABLE_TAI_LIEU + " WHERE maMH = ? ORDER BY ngayDang DESC";
            args = new String[]{String.valueOf(maMH)};
        } else {
            query = "SELECT * FROM " + TABLE_TAI_LIEU + " WHERE maMH = ? AND loaiFile = ? ORDER BY ngayDang DESC";
            args = new String[]{String.valueOf(maMH), loaiFile};
        }
        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                com.example.qlsv_kthp.model.TaiLieu tl = new com.example.qlsv_kthp.model.TaiLieu();
                tl.setMaTL(cursor.getInt(cursor.getColumnIndexOrThrow("maTL")));
                tl.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                tl.setTenFile(cursor.getString(cursor.getColumnIndexOrThrow("tenFile")));
                tl.setLoaiFile(cursor.getString(cursor.getColumnIndexOrThrow("loaiFile")));
                tl.setKichThuoc(cursor.getString(cursor.getColumnIndexOrThrow("kichThuoc")));
                tl.setNgayDang(cursor.getString(cursor.getColumnIndexOrThrow("ngayDang")));
                tl.setUrlOrPath(cursor.getString(cursor.getColumnIndexOrThrow("urlOrPath")));
                list.add(tl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private void insertTKB(SQLiteDatabase db, int maSV, int maMH, int thu, int tietBatDau, int tietKetThuc, String phongHoc, String tenGiangVien, String tuan) {
        ContentValues values = new ContentValues();
        values.put("maSV", maSV);
        values.put("maMH", maMH);
        values.put("thu", thu);
        values.put("tietBatDau", tietBatDau);
        values.put("tietKetThuc", tietKetThuc);
        values.put("phongHoc", phongHoc);
        values.put("tenGiangVien", tenGiangVien);
        values.put("tuan", tuan);
        db.insert(TABLE_THOI_KHOA_BIEU, null, values);
    }

    private void insertTaiLieu(SQLiteDatabase db, int maMH, String tenFile, String loaiFile, String kichThuoc, String ngayDang, String urlOrPath) {
        ContentValues values = new ContentValues();
        values.put("maMH", maMH);
        values.put("tenFile", tenFile);
        values.put("loaiFile", loaiFile);
        values.put("kichThuoc", kichThuoc);
        values.put("ngayDang", ngayDang);
        values.put("urlOrPath", urlOrPath);
        db.insert(TABLE_TAI_LIEU, null, values);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THOI_KHOA_BIEU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_LIEU);
        onCreate(db);
    }

    public TaiKhoan checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null,
                COL_TK_USERNAME + "=? AND " + COL_TK_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = mapTaiKhoan(cursor);
            cursor.close();
            return tk;
        }
        return null;
    }

    public TaiKhoan getTaiKhoanById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null, COL_TK_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan taiKhoan = mapTaiKhoan(cursor);
            cursor.close();
            return taiKhoan;
        }
        return null;
    }

    private TaiKhoan mapTaiKhoan(Cursor cursor) {
        return new TaiKhoan(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_TK_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_HOTEN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TK_ROLE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_TK_MASV))
        );
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_TAI_KHOAN + " WHERE " + COL_TK_USERNAME + "=?",
                new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_TAI_KHOAN + " WHERE " + COL_TK_EMAIL + "=?",
                new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public long registerStudentAccount(TaiKhoan taiKhoan, SinhVien sinhVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            if (isUsernameExists(taiKhoan.getUsername()) || isEmailExists(taiKhoan.getEmail())) {
                return -1;
            }

            ContentValues studentValues = new ContentValues();
            studentValues.put(COL_SV_HOTEN, sinhVien.getHoTen());
            studentValues.put(COL_SV_NGAYSINH, sinhVien.getNgaySinh());
            studentValues.put(COL_SV_GIOITINH, sinhVien.getGioiTinh());
            studentValues.put(COL_SV_EMAIL, sinhVien.getEmail());
            studentValues.put(COL_SV_SDT, sinhVien.getSoDienThoai());
            studentValues.put(COL_SV_DIACHI, sinhVien.getDiaChi());
            studentValues.put(COL_SV_MALOP, sinhVien.getMaLop());
            long maSV = db.insert(TABLE_SINH_VIEN, null, studentValues);

            if (maSV <= 0) {
                return -1;
            }

            ContentValues accountValues = new ContentValues();
            accountValues.put(COL_TK_USERNAME, taiKhoan.getUsername());
            accountValues.put(COL_TK_PASSWORD, taiKhoan.getPassword());
            accountValues.put(COL_TK_HOTEN, taiKhoan.getHoTen());
            accountValues.put(COL_TK_EMAIL, taiKhoan.getEmail());
            accountValues.put(COL_TK_ROLE, taiKhoan.getRole());
            accountValues.put(COL_TK_MASV, maSV);
            long accountId = db.insert(TABLE_TAI_KHOAN, null, accountValues);

            if (accountId <= 0) {
                return -1;
            }

            insertNotification(db,
                    "Chao mung tai khoan moi",
                    "Tai khoan sinh vien " + taiKhoan.getUsername() + " da duoc tao thanh cong. Ban co the dang nhap ngay de xem du lieu mau.",
                    "2026-05-14 20:00", 0, "general");

            db.setTransactionSuccessful();
            return accountId;
        } finally {
            db.endTransaction();
        }
    }

    public boolean resetPassword(String username, String email, String newPassHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TK_PASSWORD, newPassHash);
        int rows = db.update(TABLE_TAI_KHOAN, values,
                COL_TK_USERNAME + "=? AND " + COL_TK_EMAIL + "=?",
                new String[]{username, email});
        return rows > 0;
    }

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
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

    public List<Lop> getAllLop() {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOP, null, null, null, null, null, COL_LOP_TEN + " ASC");
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

    public SinhVien getSinhVienById(int maSV) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop WHERE s.maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        SinhVien sv = null;
        if (cursor != null && cursor.moveToFirst()) {
            sv = mapSinhVien(cursor);
            cursor.close();
        }
        return sv;
    }

    private SinhVien mapSinhVien(Cursor cursor) {
        SinhVien sv = new SinhVien();
        sv.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SV_ID)));
        sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_HOTEN)));
        sv.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_NGAYSINH)));
        sv.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_GIOITINH)));
        sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_EMAIL)));
        sv.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_SDT)));
        sv.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow(COL_SV_DIACHI)));
        sv.setMaLop(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SV_MALOP)));
        int tenLopIndex = cursor.getColumnIndex(COL_LOP_TEN);
        if (tenLopIndex >= 0) {
            sv.setTenLop(cursor.getString(tenLopIndex));
        }
        return sv;
    }

    public List<SinhVien> searchSinhVien(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "WHERE s.hoTen LIKE ? OR CAST(s.maSV AS TEXT) LIKE ?";
        String like = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(query, new String[]{like, like});
        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int deleteSinhVien(int maSV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIEM, "maSV=?", new String[]{String.valueOf(maSV)});
        db.delete(TABLE_DIEM_DANH, "maSV=?", new String[]{String.valueOf(maSV)});
        db.delete(TABLE_TAI_KHOAN, "maSV=?", new String[]{String.valueOf(maSV)});
        return db.delete(TABLE_SINH_VIEN, "maSV=?", new String[]{String.valueOf(maSV)});
    }

    public List<SinhVien> getSinhVienByLop(int maLop) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop WHERE s.maLop = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maLop)});
        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null, null, null, null, null, COL_MH_TEN + " ASC");
        if (cursor.moveToFirst()) {
            do {
                MonHoc mh = new MonHoc(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_TINCHI)),
                        cursor.getColumnIndex(COL_MH_GV) >= 0 && !cursor.isNull(cursor.getColumnIndex(COL_MH_GV)) ? cursor.getString(cursor.getColumnIndex(COL_MH_GV)) : "Chưa phân công"
                );
                list.add(mh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long insertMonHoc(MonHoc mh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, mh.getTenMH());
        values.put(COL_MH_TINCHI, mh.getSoTinChi());
        values.put(COL_MH_GV, mh.getGiangVien());
        return db.insert(TABLE_MON_HOC, null, values);
    }

    public List<MonHoc> getRegisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT m.* FROM " + TABLE_MON_HOC + " m " +
                "INNER JOIN " + TABLE_DIEM + " d ON m.maMH = d.maMH " +
                "WHERE d.maSV = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        if (cursor.moveToFirst()) {
            do {
                MonHoc mh = new MonHoc(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_TINCHI)),
                        cursor.getColumnIndex(COL_MH_GV) >= 0 && !cursor.isNull(cursor.getColumnIndex(COL_MH_GV)) ? cursor.getString(cursor.getColumnIndex(COL_MH_GV)) : "Chưa phân công"
                );
                list.add(mh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int countStudentsInSubject(int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT maSV) FROM " + TABLE_DIEM + " WHERE maMH = ?",
                new String[]{String.valueOf(maMH)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public boolean isSubjectRegistered(int maSV, int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_DIEM + " WHERE maSV = ? AND maMH = ?",
                new String[]{String.valueOf(maSV), String.valueOf(maMH)});
        boolean registered = cursor.moveToFirst();
        cursor.close();
        return registered;
    }

    public void registerSubject(int maSV, int maMH) {
        if (!isSubjectRegistered(maSV, maMH)) {
            insertScore(this.getWritableDatabase(), maSV, maMH, 0, 0, 0, "HK1-2025");
        }
    }

    public List<MonHoc> getUnregisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MON_HOC + " WHERE maMH NOT IN " +
                "(SELECT maMH FROM " + TABLE_DIEM + " WHERE maSV = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});
        if (cursor.moveToFirst()) {
            do {
                MonHoc mh = new MonHoc(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MH_TEN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MH_TINCHI)),
                        cursor.getColumnIndex(COL_MH_GV) >= 0 && !cursor.isNull(cursor.getColumnIndex(COL_MH_GV)) ? cursor.getString(cursor.getColumnIndex(COL_MH_GV)) : "Chưa phân công"
                );
                list.add(mh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int updateMonHoc(MonHoc mh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, mh.getTenMH());
        values.put(COL_MH_TINCHI, mh.getSoTinChi());
        values.put(COL_MH_GV, mh.getGiangVien());
        return db.update(TABLE_MON_HOC, values, COL_MH_ID + "=?", new String[]{String.valueOf(mh.getMaMH())});
    }

    public boolean deleteMonHoc(int maMH) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DIEM + " WHERE maMH=?",
                new String[]{String.valueOf(maMH)});
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        if (count > 0) {
            return false;
        }
        db.delete(TABLE_DIEM_DANH, "maMH=?", new String[]{String.valueOf(maMH)});
        db.delete(TABLE_MON_HOC, "maMH=?", new String[]{String.valueOf(maMH)});
        return true;
    }

    public List<Diem> getDiemBySinhVien(int maSV, String hocKy) {
        List<Diem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.*, m.tenMH, m.soTinChi FROM " + TABLE_DIEM + " d " +
                "JOIN " + TABLE_MON_HOC + " m ON d.maMH = m.maMH " +
                "WHERE d.maSV = ? AND d.hocKy = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), hocKy});
        if (cursor.moveToFirst()) {
            do {
                Diem diem = new Diem();
                diem.setMaDiem(cursor.getInt(cursor.getColumnIndexOrThrow("maDiem")));
                diem.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                diem.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                diem.setDiemGiuaKy(cursor.getFloat(cursor.getColumnIndexOrThrow("diemGiuaKy")));
                diem.setDiemCuoiKy(cursor.getFloat(cursor.getColumnIndexOrThrow("diemCuoiKy")));
                diem.setDiemBaiTap(cursor.getFloat(cursor.getColumnIndexOrThrow("diemBaiTap")));
                diem.setHocKy(cursor.getString(cursor.getColumnIndexOrThrow("hocKy")));
                diem.setTenMH(cursor.getString(cursor.getColumnIndexOrThrow("tenMH")));
                diem.setSoTinChi(cursor.getInt(cursor.getColumnIndexOrThrow("soTinChi")));
                list.add(diem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long insertOrUpdateDiem(Diem diem) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT maDiem FROM " + TABLE_DIEM + " WHERE maSV=? AND maMH=? AND hocKy=?",
                new String[]{String.valueOf(diem.getMaSV()), String.valueOf(diem.getMaMH()), diem.getHocKy()});
        ContentValues values = new ContentValues();
        values.put("maSV", diem.getMaSV());
        values.put("maMH", diem.getMaMH());
        values.put("diemGiuaKy", diem.getDiemGiuaKy());
        values.put("diemCuoiKy", diem.getDiemCuoiKy());
        values.put("diemBaiTap", diem.getDiemBaiTap());
        values.put("hocKy", diem.getHocKy());
        long result;
        if (c.moveToFirst()) {
            int id = c.getInt(0);
            result = db.update(TABLE_DIEM, values, "maDiem=?", new String[]{String.valueOf(id)});
        } else {
            result = db.insert(TABLE_DIEM, null, values);
        }
        c.close();
        return result;
    }

    public float getGPAHocKy(int maSV, String hocKy) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM((d.diemBaiTap*0.2 + d.diemGiuaKy*0.3 + d.diemCuoiKy*0.5) * m.soTinChi) / " +
                "SUM(m.soTinChi) FROM " + TABLE_DIEM + " d " +
                "JOIN " + TABLE_MON_HOC + " m ON d.maMH = m.maMH " +
                "WHERE d.maSV=? AND d.hocKy=?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(maSV), hocKy});
        float gpa = 0;
        if (c.moveToFirst()) {
            gpa = c.getFloat(0);
        }
        c.close();
        return gpa;
    }

    public long insertDiemDanh(DiemDanh dd) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT maDiemDanh FROM " + TABLE_DIEM_DANH + " WHERE maSV=? AND maMH=? AND ngay=?",
                new String[]{String.valueOf(dd.getMaSV()), String.valueOf(dd.getMaMH()), dd.getNgay()});
        ContentValues values = new ContentValues();
        values.put("maSV", dd.getMaSV());
        values.put("maMH", dd.getMaMH());
        values.put("ngay", dd.getNgay());
        values.put("trangThai", dd.getTrangThai());
        long result;
        if (c.moveToFirst()) {
            int id = c.getInt(0);
            result = db.update(TABLE_DIEM_DANH, values, "maDiemDanh=?", new String[]{String.valueOf(id)});
        } else {
            result = db.insert(TABLE_DIEM_DANH, null, values);
        }
        c.close();
        return result;
    }

    public List<DiemDanh> getDiemDanhBySinhVien(int maSV, int maMH) {
        List<DiemDanh> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT dd.*, m.tenMH FROM " + TABLE_DIEM_DANH + " dd " +
                "JOIN " + TABLE_MON_HOC + " m ON dd.maMH = m.maMH " +
                "WHERE dd.maSV=? AND dd.maMH=? ORDER BY dd.ngay DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV), String.valueOf(maMH)});
        if (cursor.moveToFirst()) {
            do {
                DiemDanh dd = new DiemDanh();
                dd.setMaDiemDanh(cursor.getInt(cursor.getColumnIndexOrThrow("maDiemDanh")));
                dd.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                dd.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                dd.setNgay(cursor.getString(cursor.getColumnIndexOrThrow("ngay")));
                dd.setTrangThai(cursor.getInt(cursor.getColumnIndexOrThrow("trangThai")));
                dd.setTenMH(cursor.getString(cursor.getColumnIndexOrThrow("tenMH")));
                list.add(dd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int countVangMat(int maSV, int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_DIEM_DANH + " WHERE maSV=? AND maMH=? AND trangThai=0",
                new String[]{String.valueOf(maSV), String.valueOf(maMH)});
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public List<ThongBao> getAllThongBao() {
        List<ThongBao> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THONG_BAO, null, null, null, null, null, "ngayTao DESC");
        if (cursor.moveToFirst()) {
            do {
                ThongBao tb = new ThongBao(
                        cursor.getInt(cursor.getColumnIndexOrThrow("maThongBao")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tieuDe")),
                        cursor.getString(cursor.getColumnIndexOrThrow("noiDung")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ngayTao")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("daDoc")),
                        cursor.getString(cursor.getColumnIndexOrThrow("loai"))
                );
                list.add(tb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int markThongBaoAsRead(int maThongBao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("daDoc", 1);
        return db.update(TABLE_THONG_BAO, values, "maThongBao=?", new String[]{String.valueOf(maThongBao)});
    }

    public void markAllAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("daDoc", 1);
        db.update(TABLE_THONG_BAO, values, null, null);
    }

    public int countUnreadThongBao() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_THONG_BAO + " WHERE daDoc=0", null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
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

    public boolean changePassword(int userId, String oldPassHash, String newPassHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM " + TABLE_TAI_KHOAN + " WHERE id=? AND password=?",
                new String[]{String.valueOf(userId), oldPassHash});
        if (!c.moveToFirst()) {
            c.close();
            return false;
        }
        c.close();
        ContentValues values = new ContentValues();
        values.put("password", newPassHash);
        db.update(TABLE_TAI_KHOAN, values, "id=?", new String[]{String.valueOf(userId)});
        return true;
    }
}
