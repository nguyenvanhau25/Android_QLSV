package com.example.qlsv_kthp.model;

/**
 * Model Môn học
 */
public class MonHoc {
    private int maMH;
    private String tenMH;
    private int soTinChi;

    public MonHoc() {}

    public MonHoc(int maMH, String tenMH, int soTinChi) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
    }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    @Override
    public String toString() { return tenMH; }
}
