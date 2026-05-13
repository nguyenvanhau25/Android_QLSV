package com.example.qlsv_kthp.model;

/**
 * Model Thông báo
 */
public class ThongBao {
    private int maThongBao;
    private String tieuDe;
    private String noiDung;
    private String ngayTao; // dd/MM/yyyy HH:mm
    private int daDoc; // 0 = chưa đọc, 1 = đã đọc
    private String loai; // "general", "score", "attendance"

    public ThongBao() {}

    public ThongBao(int maThongBao, String tieuDe, String noiDung,
                    String ngayTao, int daDoc, String loai) {
        this.maThongBao = maThongBao;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.daDoc = daDoc;
        this.loai = loai;
    }

    public int getMaThongBao() { return maThongBao; }
    public void setMaThongBao(int maThongBao) { this.maThongBao = maThongBao; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public int getDaDoc() { return daDoc; }
    public void setDaDoc(int daDoc) { this.daDoc = daDoc; }

    public boolean isRead() { return daDoc == 1; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
}
