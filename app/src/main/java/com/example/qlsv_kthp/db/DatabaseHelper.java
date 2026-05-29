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
 * Lớp hỗ trợ quản lý cơ sở dữ liệu SQLite - Refactored Version 4.1
 * Tối ưu truy vấn và bổ sung các tính năng học vụ.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLSV.db";
    private static final int DATABASE_VERSION = 4;

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
        if (oldVersion < 4) {
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

    // --- MON HOC QUERIES ---

    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MON_HOC,
                null,
                null,
                null,
                null,
                null,
                COL_MH_TEN + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(mapMonHoc(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public MonHoc getMonHocById(int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MON_HOC,
                null,
                COL_MH_ID + "=?",
                new String[]{String.valueOf(maMH)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            MonHoc mh = mapMonHoc(cursor);
            cursor.close();
            return mh;
        }

        if (cursor != null) {
            cursor.close();
        }

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
        mh.setSoLuongHienTai(countStudentsInSubject(mh.getMaMH()));

        return mh;
    }

    public int countStudentsInSubject(int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(DISTINCT maSV) FROM " + TABLE_DIEM + " WHERE maMH = ?",
                new String[]{String.valueOf(maMH)}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public List<SinhVien> getSinhVienByMonHoc(int maMH) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "JOIN " + TABLE_DIEM + " d ON s.maSV = d.maSV " +
                "WHERE d.maMH = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maMH)});

        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public String registerSubject(int maSV, int maMH) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (isSubjectRegistered(maSV, maMH)) {
            return "Môn học này đã được đăng ký!";
        }

        MonHoc mh = getMonHocById(maMH);

        if (mh == null) {
            return "Môn học không tồn tại!";
        }

        if (countStudentsInSubject(maMH) >= mh.getSoLuongMax()) {
            return "Lớp đã đạt giới hạn tối đa " + mh.getSoLuongMax() + " SV!";
        }

        ContentValues values = new ContentValues();
        values.put("maSV", maSV);
        values.put("maMH", maMH);
        values.put("hocKy", "HK1-2025");
        values.put("diemGiuaKy", 0);
        values.put("diemCuoiKy", 0);
        values.put("diemBaiTap", 0);

        long result = db.insert(TABLE_DIEM, null, values);

        return result != -1 ? "SUCCESS" : "Lỗi hệ thống khi đăng ký!";
    }

    public boolean isSubjectRegistered(int maSV, int maMH) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_DIEM + " WHERE maSV = ? AND maMH = ?",
                new String[]{String.valueOf(maSV), String.valueOf(maMH)}
        );

        boolean registered = cursor.moveToFirst();
        cursor.close();

        return registered;
    }

    // --- SINH VIEN QUERIES ---

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "ORDER BY s.hoTen ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<Lop> getAllLop() {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_LOP,
                null,
                null,
                null,
                null,
                null,
                "tenLop ASC"
        );

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

        return db.update(
                TABLE_SINH_VIEN,
                values,
                "maSV = ?",
                new String[]{String.valueOf(sv.getMaSV())}
        );
    }

    public List<SinhVien> searchSinhVien(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String searchKey = "%" + keyword + "%";

        String query = "SELECT s.*, l.tenLop FROM " + TABLE_SINH_VIEN + " s " +
                "LEFT JOIN " + TABLE_LOP + " l ON s.maLop = l.maLop " +
                "WHERE s.hoTen LIKE ? " +
                "OR s.email LIKE ? " +
                "OR s.soDienThoai LIKE ? " +
                "OR l.tenLop LIKE ? " +
                "ORDER BY s.hoTen ASC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{searchKey, searchKey, searchKey, searchKey}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(mapSinhVien(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
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

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public int deleteSinhVien(int maSV) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {
            // Xóa dữ liệu điểm của sinh viên
            db.delete(
                    TABLE_DIEM,
                    "maSV = ?",
                    new String[]{String.valueOf(maSV)}
            );

            // Xóa dữ liệu điểm danh của sinh viên
            db.delete(
                    TABLE_DIEM_DANH,
                    "maSV = ?",
                    new String[]{String.valueOf(maSV)}
            );

            // Xóa tài khoản gắn với sinh viên
            db.delete(
                    TABLE_TAI_KHOAN,
                    "maSV = ?",
                    new String[]{String.valueOf(maSV)}
            );

            // Xóa sinh viên
            int rows = db.delete(
                    TABLE_SINH_VIEN,
                    "maSV = ?",
                    new String[]{String.valueOf(maSV)}
            );

            db.setTransactionSuccessful();
            return rows;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.endTransaction();
        }
    }

    private SinhVien mapSinhVien(Cursor cursor) {
        SinhVien sv = new SinhVien();

        sv.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
        sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow("hoTen")));
        sv.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow("ngaySinh")));
        sv.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow("gioiTinh")));
        sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        sv.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")));
        sv.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow("diaChi")));
        sv.setMaLop(cursor.getInt(cursor.getColumnIndexOrThrow("maLop")));

        int idx = cursor.getColumnIndex("tenLop");
        if (idx != -1) {
            sv.setTenLop(cursor.getString(idx));
        }

        return sv;
    }

    // --- TAI KHOAN / AUTH QUERIES ---

    public TaiKhoan checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TAI_KHOAN,
                null,
                "username=? AND password=?",
                new String[]{username, password},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = mapTaiKhoan(cursor);
            cursor.close();
            return tk;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public TaiKhoan getTaiKhoanById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TAI_KHOAN,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan tk = mapTaiKhoan(cursor);
            cursor.close();
            return tk;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TAI_KHOAN,
                new String[]{"id"},
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );

        boolean exists = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TAI_KHOAN,
                new String[]{"id"},
                "email = ?",
                new String[]{email},
                null,
                null,
                null
        );

        boolean exists = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    public long registerStudentAccount(TaiKhoan tk, SinhVien sv) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues svValues = new ContentValues();
            svValues.put("hoTen", sv.getHoTen());
            svValues.put("ngaySinh", sv.getNgaySinh());
            svValues.put("gioiTinh", sv.getGioiTinh());
            svValues.put("email", sv.getEmail());
            svValues.put("soDienThoai", sv.getSoDienThoai());
            svValues.put("diaChi", sv.getDiaChi());
            svValues.put("maLop", sv.getMaLop());

            long maSV = db.insert(TABLE_SINH_VIEN, null, svValues);

            if (maSV == -1) {
                return -1;
            }

            ContentValues tkValues = new ContentValues();
            tkValues.put("username", tk.getUsername());
            tkValues.put("password", tk.getPassword());
            tkValues.put("hoTen", tk.getHoTen());
            tkValues.put("email", tk.getEmail());
            tkValues.put("role", tk.getRole());
            tkValues.put("maSV", maSV);

            long accountId = db.insert(TABLE_TAI_KHOAN, null, tkValues);

            if (accountId == -1) {
                return -1;
            }

            db.setTransactionSuccessful();
            return accountId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    public boolean changePassword(int userId, String oldHashedPassword, String newHashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_TAI_KHOAN,
                new String[]{"id"},
                "id = ? AND password = ?",
                new String[]{String.valueOf(userId), oldHashedPassword},
                null,
                null,
                null
        );

        boolean isOldPasswordCorrect = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        if (!isOldPasswordCorrect) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("password", newHashedPassword);

        int rows = db.update(
                TABLE_TAI_KHOAN,
                values,
                "id = ?",
                new String[]{String.valueOf(userId)}
        );

        return rows > 0;
    }
    public boolean resetPassword(String username, String email, String newHashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", newHashedPassword);

        int rows = db.update(
                TABLE_TAI_KHOAN,
                values,
                "username = ? AND email = ?",
                new String[]{username, email}
        );

        return rows > 0;
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

    // --- DIEM DANH QUERIES ---

    public List<DiemDanh> getDiemDanhBySinhVien(int maSV, int maMH) {
        List<DiemDanh> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT dd.*, sv.hoTen AS tenSV, mh.tenMH AS tenMH " +
                "FROM " + TABLE_DIEM_DANH + " dd " +
                "LEFT JOIN " + TABLE_SINH_VIEN + " sv ON dd.maSV = sv.maSV " +
                "LEFT JOIN " + TABLE_MON_HOC + " mh ON dd.maMH = mh.maMH " +
                "WHERE dd.maSV = ? AND dd.maMH = ? " +
                "ORDER BY dd.ngay DESC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(maSV), String.valueOf(maMH)}
        );

        if (cursor.moveToFirst()) {
            do {
                DiemDanh dd = new DiemDanh();

                dd.setMaDiemDanh(cursor.getInt(cursor.getColumnIndexOrThrow("maDiemDanh")));
                dd.setMaSV(cursor.getInt(cursor.getColumnIndexOrThrow("maSV")));
                dd.setMaMH(cursor.getInt(cursor.getColumnIndexOrThrow("maMH")));
                dd.setNgay(cursor.getString(cursor.getColumnIndexOrThrow("ngay")));
                dd.setTrangThai(cursor.getInt(cursor.getColumnIndexOrThrow("trangThai")));

                int tenSVIndex = cursor.getColumnIndex("tenSV");
                if (tenSVIndex != -1) {
                    dd.setTenSV(cursor.getString(tenSVIndex));
                }

                int tenMHIndex = cursor.getColumnIndex("tenMH");
                if (tenMHIndex != -1) {
                    dd.setTenMH(cursor.getString(tenMHIndex));
                }

                list.add(dd);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
    public long insertDiemDanh(DiemDanh dd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maSV", dd.getMaSV());
        values.put("maMH", dd.getMaMH());
        values.put("ngay", dd.getNgay());
        values.put("trangThai", dd.getTrangThai());

        return db.insert(TABLE_DIEM_DANH, null, values);
    }

    // --- THONG BAO QUERIES ---

    public int countUnreadThongBao() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_THONG_BAO + " WHERE daDoc = 0",
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }
    public List<ThongBao> getAllThongBao() {
        List<ThongBao> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_THONG_BAO,
                null,
                null,
                null,
                null,
                null,
                "maThongBao DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                ThongBao tb = new ThongBao();

                tb.setMaThongBao(cursor.getInt(cursor.getColumnIndexOrThrow("maThongBao")));
                tb.setTieuDe(cursor.getString(cursor.getColumnIndexOrThrow("tieuDe")));
                tb.setNoiDung(cursor.getString(cursor.getColumnIndexOrThrow("noiDung")));
                tb.setNgayTao(cursor.getString(cursor.getColumnIndexOrThrow("ngayTao")));
                tb.setDaDoc(cursor.getInt(cursor.getColumnIndexOrThrow("daDoc")));
                tb.setLoai(cursor.getString(cursor.getColumnIndexOrThrow("loai")));

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

        return db.update(
                TABLE_THONG_BAO,
                values,
                "maThongBao = ?",
                new String[]{String.valueOf(maThongBao)}
        );
    }

    public int markAllThongBaoAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("daDoc", 1);

        return db.update(
                TABLE_THONG_BAO,
                values,
                null,
                null
        );
    }

    public boolean deleteThongBao(int maThongBao) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_THONG_BAO,
                "maThongBao = ?",
                new String[]{String.valueOf(maThongBao)}
        ) > 0;
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

    // --- DIEM / MON HOC EXTRA ---

    public List<Diem> getDiemBySinhVien(int maSV, String hocKy) {
        List<Diem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT d.*, m.tenMH FROM " + TABLE_DIEM + " d " +
                "LEFT JOIN " + TABLE_MON_HOC + " m ON d.maMH = m.maMH " +
                "WHERE d.maSV = ? AND d.hocKy = ? " +
                "ORDER BY m.tenMH ASC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(maSV), hocKy}
        );

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

                int tenMHIndex = cursor.getColumnIndex("tenMH");
                if (tenMHIndex != -1) {
                    diem.setTenMH(cursor.getString(tenMHIndex));
                }

                list.add(diem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public float getGPAHocKy(int maSV, String hocKy) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT AVG((diemBaiTap * 0.2) + (diemGiuaKy * 0.3) + (diemCuoiKy * 0.5)) " +
                "FROM " + TABLE_DIEM + " " +
                "WHERE maSV = ? AND hocKy = ?";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(maSV), hocKy}
        );

        float gpa = 0;

        if (cursor.moveToFirst()) {
            gpa = cursor.getFloat(0);
        }

        cursor.close();
        return gpa;
    }
    public float getGPA(int maSV) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT AVG((diemBaiTap * 0.2) + (diemGiuaKy * 0.3) + (diemCuoiKy * 0.5)) " +
                "FROM " + TABLE_DIEM + " WHERE maSV = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});

        float gpa = 0;

        if (cursor.moveToFirst()) {
            gpa = cursor.getFloat(0);
        }

        cursor.close();
        return gpa;
    }

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

    public int updateMonHoc(MonHoc mh) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_MH_TEN, mh.getTenMH());
        values.put(COL_MH_TINCHI, mh.getSoTinChi());
        values.put(COL_MH_GV, mh.getGiangVien());
        values.put(COL_MH_LICHHOC, mh.getLichHoc());
        values.put(COL_MH_TAILIEU, mh.getTaiLieu());
        values.put(COL_MH_SOLUONGMAX, mh.getSoLuongMax());

        return db.update(
                TABLE_MON_HOC,
                values,
                COL_MH_ID + "=?",
                new String[]{String.valueOf(mh.getMaMH())}
        );
    }

    public List<TaiLieu> getTaiLieuByMonHoc(int maMH, String loaiFile) {
        List<TaiLieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;

        if (loaiFile == null || loaiFile.equalsIgnoreCase("Tất cả")) {
            cursor = db.query(
                    TABLE_TAI_LIEU,
                    null,
                    "maMH = ?",
                    new String[]{String.valueOf(maMH)},
                    null,
                    null,
                    "maTL DESC"
            );
        } else {
            cursor = db.query(
                    TABLE_TAI_LIEU,
                    null,
                    "maMH = ? AND loaiFile = ?",
                    new String[]{String.valueOf(maMH), loaiFile},
                    null,
                    null,
                    "maTL DESC"
            );
        }

        if (cursor.moveToFirst()) {
            do {
                TaiLieu tl = new TaiLieu();

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
    public boolean deleteMonHoc(int maMH) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                TABLE_DIEM,
                "maMH=?",
                new String[]{String.valueOf(maMH)}
        );

        return db.delete(
                TABLE_MON_HOC,
                "maMH=?",
                new String[]{String.valueOf(maMH)}
        ) > 0;
    }

    public List<MonHoc> getRegisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.* FROM " + TABLE_MON_HOC + " m " +
                "JOIN " + TABLE_DIEM + " d ON m.maMH = d.maMH " +
                "WHERE d.maSV = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maSV)});

        if (cursor.moveToFirst()) {
            do {
                list.add(mapMonHoc(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<MonHoc> getUnregisteredSubjects(int maSV) {
        List<MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MON_HOC + " " +
                "WHERE maMH NOT IN (" +
                "SELECT maMH FROM " + TABLE_DIEM + " WHERE maSV = ?" +
                ") " +
                "ORDER BY " + COL_MH_TEN + " ASC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(maSV)}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(mapMonHoc(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<ThoiKhoaBieu> getTKBBySinhVienAndThu(int maSV, int thu) {
        List<ThoiKhoaBieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_THOI_KHOA_BIEU +
                " WHERE maSV = ? AND thu = ? " +
                " ORDER BY tietBatDau ASC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{String.valueOf(maSV), String.valueOf(thu)}
        );

        if (cursor.moveToFirst()) {
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
                tkb.setTuan(cursor.getString(cursor.getColumnIndexOrThrow("tuan")));

                list.add(tkb);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public int getCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + tableName,
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
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
        diem.put("diemGiuaKy", 0);
        diem.put("diemCuoiKy", 0);
        diem.put("diemBaiTap", 0);
        db.insert(TABLE_DIEM, null, diem);

        ContentValues tb = new ContentValues();
        tb.put("tieuDe", "Chào mừng bạn");
        tb.put("noiDung", "Chào mừng bạn đến với hệ thống quản lý sinh viên.");
        tb.put("ngayTao", "01/01/2026 08:00");
        tb.put("daDoc", 0);
        tb.put("loai", "general");
        db.insert(TABLE_THONG_BAO, null, tb);
    }
}