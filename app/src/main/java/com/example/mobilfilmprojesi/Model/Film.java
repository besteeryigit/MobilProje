package com.example.mobilfilmprojesi.Model;

public class Film {
    private String ad, aciklama,fragmanLink, teknikBilgi,filmMenuId,tur,fotograf,foto1,foto2,foto3;

    public Film() {
    }

    public Film(String ad, String aciklama, String fragmanLink, String teknikBilgi, String filmMenuId, String tur, String fotograf, String foto1, String foto2, String foto3) {
        this.ad = ad;
        this.aciklama = aciklama;
        this.fragmanLink = fragmanLink;
        this.teknikBilgi = teknikBilgi;
        this.filmMenuId = filmMenuId;
        this.tur = tur;
        this.fotograf = fotograf;
        this.foto1 = foto1;
        this.foto2 = foto2;
        this.foto3 = foto3;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getFragmanLink() {
        return fragmanLink;
    }

    public void setFragmanLink(String fragmanLink) {
        this.fragmanLink = fragmanLink;
    }

    public String getTeknikBilgi() {
        return teknikBilgi;
    }

    public void setTeknikBilgi(String teknikBilgi) {
        this.teknikBilgi = teknikBilgi;
    }

    public String getFilmMenuId() {
        return filmMenuId;
    }

    public void setFilmMenuId(String filmMenuId) {
        this.filmMenuId = filmMenuId;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getFotograf() {
        return fotograf;
    }

    public void setFotograf(String fotograf) {
        this.fotograf = fotograf;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }
}
