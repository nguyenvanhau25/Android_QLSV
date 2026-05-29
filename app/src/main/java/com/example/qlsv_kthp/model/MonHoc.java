package com.example.qlsv_kthp.model;

/**
 * Model Môn học - Được cập nhật để hỗ trợ chi tiết giảng dạy và quản lý lớp
 */
public class MonHoc {
    private int maMH;
    private String tenMH;
    private int soTinChi;
    private String giangVien;
    private String lichHoc;
    private String taiLieu;
    private int soLuongMax;
    private int soLuongHienTai; // Trường tính toán, không lưu trực tiếp trong bảng MONHOC

    public MonHoc() {
        this.soLuongMax = 65; // Mặc định tối đa 65 sinh viên
    }

    public MonHoc(int maMH, String tenMH, int soTinChi, String giangVien, String lichHoc, String taiLieu, int soLuongMax) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTinChi = soTinChi;
        this.giangVien = giangVien;
        this.lichHoc = lichHoc;
        this.taiLieu = taiLieu;
        this.soLuongMax = soLuongMax;
    }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public String getGiangVien() { return giangVien != null ? giangVien : "Chưa phân công"; }
    public void setGiangVien(String giangVien) { this.giangVien = giangVien; }

    public String getLichHoc() { return lichHoc != null ? lichHoc : "Chưa có lịch"; }
    public void setLichHoc(String lichHoc) { this.lichHoc = lichHoc; }

    public String getTaiLieu() { return taiLieu != null ? taiLieu : ""; }
    public void setTaiLieu(String taiLieu) { this.taiLieu = taiLieu; }

    public int getSoLuongMax() { return soLuongMax; }
    public void setSoLuongMax(int soLuongMax) { this.soLuongMax = soLuongMax; }

    public int getSoLuongHienTai() { return soLuongHienTai; }
    public void setSoLuongHienTai(int soLuongHienTai) { this.soLuongHienTai = soLuongHienTai; }

    @Override
    public String toString() { return tenMH; }
}
