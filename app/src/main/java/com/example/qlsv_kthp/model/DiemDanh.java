package com.example.qlsv_kthp.model;

/**
 * Model Điểm danh
 */
public class DiemDanh {
    private int maDiemDanh;
    private int maSV;
    private int maMH;
    private String ngay; // dd/MM/yyyy
    private int trangThai; // 1 = có mặt, 0 = vắng

    // Join fields
    private String tenSV;
    private String tenMH;

    public DiemDanh() {}

    public DiemDanh(int maDiemDanh, int maSV, int maMH, String ngay, int trangThai) {
        this.maDiemDanh = maDiemDanh;
        this.maSV = maSV;
        this.maMH = maMH;
        this.ngay = ngay;
        this.trangThai = trangThai;
    }

    public int getMaDiemDanh() { return maDiemDanh; }
    public void setMaDiemDanh(int maDiemDanh) { this.maDiemDanh = maDiemDanh; }

    public int getMaSV() { return maSV; }
    public void setMaSV(int maSV) { this.maSV = maSV; }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public boolean isPresent() { return trangThai == 1; }

    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
}
