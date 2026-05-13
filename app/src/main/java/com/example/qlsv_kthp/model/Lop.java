package com.example.qlsv_kthp.model;

/**
 * Model Lớp học
 */
public class Lop {
    private int maLop;
    private String tenLop;
    private String khoaHoc; // VD: "2023-2027"
    private int soSinhVien; // count tính từ query

    public Lop() {}

    public Lop(int maLop, String tenLop, String khoaHoc) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoaHoc = khoaHoc;
    }

    public int getMaLop() { return maLop; }
    public void setMaLop(int maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getKhoaHoc() { return khoaHoc; }
    public void setKhoaHoc(String khoaHoc) { this.khoaHoc = khoaHoc; }

    public int getSoSinhVien() { return soSinhVien; }
    public void setSoSinhVien(int soSinhVien) { this.soSinhVien = soSinhVien; }

    @Override
    public String toString() { return tenLop; }
}
