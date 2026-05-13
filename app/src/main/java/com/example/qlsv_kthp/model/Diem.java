package com.example.qlsv_kthp.model;

/**
 * Model Điểm số
 */
public class Diem {
    private int maDiem;
    private int maSV;
    private int maMH;
    private float diemGiuaKy;
    private float diemCuoiKy;
    private float diemBaiTap;
    private String hocKy; // VD: "HK1-2024"

    // Join fields
    private String tenSV;
    private String tenMH;
    private int soTinChi;

    public Diem() {}

    public Diem(int maDiem, int maSV, int maMH, float diemGiuaKy,
                float diemCuoiKy, float diemBaiTap, String hocKy) {
        this.maDiem = maDiem;
        this.maSV = maSV;
        this.maMH = maMH;
        this.diemGiuaKy = diemGiuaKy;
        this.diemCuoiKy = diemCuoiKy;
        this.diemBaiTap = diemBaiTap;
        this.hocKy = hocKy;
    }

    /** Tính điểm trung bình môn: BT 20% + GK 30% + CK 50% */
    public float getDiemTrungBinh() {
        return diemBaiTap * 0.2f + diemGiuaKy * 0.3f + diemCuoiKy * 0.5f;
    }

    /** Xếp loại theo điểm trung bình */
    public String getXepLoai() {
        float dtb = getDiemTrungBinh();
        if (dtb >= 8.5) return "Giỏi";
        if (dtb >= 7.0) return "Khá";
        if (dtb >= 5.0) return "Trung bình";
        return "Yếu";
    }

    // Getters & Setters
    public int getMaDiem() { return maDiem; }
    public void setMaDiem(int maDiem) { this.maDiem = maDiem; }

    public int getMaSV() { return maSV; }
    public void setMaSV(int maSV) { this.maSV = maSV; }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public float getDiemGiuaKy() { return diemGiuaKy; }
    public void setDiemGiuaKy(float diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    public float getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(float diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    public float getDiemBaiTap() { return diemBaiTap; }
    public void setDiemBaiTap(float diemBaiTap) { this.diemBaiTap = diemBaiTap; }

    public String getHocKy() { return hocKy; }
    public void setHocKy(String hocKy) { this.hocKy = hocKy; }

    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }
}
