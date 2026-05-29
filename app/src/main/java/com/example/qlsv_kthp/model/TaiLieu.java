package com.example.qlsv_kthp.model;

public class TaiLieu {
    private int maTL;
    private int maMH;
    private String tenFile;
    private String loaiFile;
    private String kichThuoc;
    private String ngayDang;
    private String urlOrPath;

    public TaiLieu() {}

    public int getMaTL() { return maTL; }
    public void setMaTL(int maTL) { this.maTL = maTL; }

    public int getMaMH() { return maMH; }
    public void setMaMH(int maMH) { this.maMH = maMH; }

    public String getTenFile() { return tenFile; }
    public void setTenFile(String tenFile) { this.tenFile = tenFile; }

    public String getLoaiFile() { return loaiFile; }
    public void setLoaiFile(String loaiFile) { this.loaiFile = loaiFile; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getNgayDang() { return ngayDang; }
    public void setNgayDang(String ngayDang) { this.ngayDang = ngayDang; }

    public String getUrlOrPath() { return urlOrPath; }
    public void setUrlOrPath(String urlOrPath) { this.urlOrPath = urlOrPath; }
}
