package com.example.qlsv_kthp.model;

/**
 * Lớp đại diện cho đối tượng Sinh viên
 */
public class SinhVien {
    private int maSV;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String email;
    private String soDienThoai;
    private String diaChi;
    private int maLop;
    private String tenLop;

    public SinhVien() {}

    public SinhVien(int maSV, String hoTen, String ngaySinh, String gioiTinh, String email, String soDienThoai, String diaChi, int maLop) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.maLop = maLop;
    }

    // Lấy ký tự đầu của tên để làm avatar text
    public String getInitial() {
        if (hoTen == null || hoTen.isEmpty()) return "NA";
        String[] parts = hoTen.split(" ");
        return parts[parts.length - 1].substring(0, 1).toUpperCase();
    }

    public int getMaSV() { return maSV; }
    public void setMaSV(int maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getMaLop() { return maLop; }
    public void setMaLop(int maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
}
