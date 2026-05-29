package com.example.qlsv_kthp.model;

/**
 * Model Môn học
 */
public class MonHoc {
    private int maMH;
    private String tenMH;
    private int soTinChi;
    private String giangVien;

    public MonHoc() {}

    public MonHoc(int maMH, String tenMH, int soTinChi, String giangVien) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
        this.giangVien = giangVien;
    }

    public MonHoc(int maMH, String tenMH, int soTinChi) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
        this.giangVien = "Chưa phân công";
    }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public String getGiangVien() { return giangVien; }
    public void setGiangVien(String giangVien) { this.giangVien = giangVien; }

    @Override
    public String toString() { return tenMH; }
}
