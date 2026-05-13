package com.example.qlsv_kthp.model;

/**
 * Model Tài khoản đăng nhập
 */
public class TaiKhoan {
    private int id;
    private String username;
    private String password; // SHA-256 hash
    private String hoTen;
    private String email;
    private String role; // "admin" hoặc "user"
    private int maSV; // liên kết với sinh viên nếu role=user, -1 nếu admin

    public TaiKhoan() {}

    public TaiKhoan(int id, String username, String password, String hoTen, String email, String role, int maSV) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.email = email;
        this.role = role;
        this.maSV = maSV;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getMaSV() { return maSV; }
    public void setMaSV(int maSV) { this.maSV = maSV; }

    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
}
