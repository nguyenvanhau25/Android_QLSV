package com.example.qlsv_kthp.model;

public class ThoiKhoaBieu {
    private int maTKB;
    private int maSV;
    private int maMH;
    private int thu; // 2-7
    private int tietBatDau;
    private int tietKetThuc;
    private String phongHoc;
    private String tenGiangVien;
    private String tuan;
    
    // Join field
    private String tenMH;

    public ThoiKhoaBieu() {}

    public int getMaTKB() { return maTKB; }
    public void setMaTKB(int maTKB) { this.maTKB = maTKB; }

    public int getMaSV() { return maSV; }
    public void setMaSV(int maSV) { this.maSV = maSV; }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public int getThu() { return thu; }
    public void setThu(int thu) { this.thu = thu; }

    public int getTietBatDau() { return tietBatDau; }
    public void setTietBatDau(int tietBatDau) { this.tietBatDau = tietBatDau; }

    public int getTietKetThuc() { return tietKetThuc; }
    public void setTietKetThuc(int tietKetThuc) { this.tietKetThuc = tietKetThuc; }

    public String getPhongHoc() { return phongHoc; }
    public void setPhongHoc(String phongHoc) { this.phongHoc = phongHoc; }

    public String getTenGiangVien() { return tenGiangVien; }
    public void setTenGiangVien(String tenGiangVien) { this.tenGiangVien = tenGiangVien; }

    public String getTuan() { return tuan; }
    public void setTuan(String tuan) { this.tuan = tuan; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
}
