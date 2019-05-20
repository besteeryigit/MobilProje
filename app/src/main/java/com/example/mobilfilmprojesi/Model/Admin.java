package com.example.mobilfilmprojesi.Model;

public class Admin {
    private String adminAdSoyad;
    private String adminSifre;


    public Admin() {
    }

    public Admin(String adminAdSoyad, String adminSifre) {
        this.adminAdSoyad = adminAdSoyad;
        this.adminSifre = adminSifre;
    }

    public String getAdminAdSoyad() {
        return adminAdSoyad;
    }

    public void setAdminAdSoyad(String adminAdSoyad) {
        this.adminAdSoyad = adminAdSoyad;
    }

    public String getAdminSifre() {
        return adminSifre;
    }

    public void setAdminSifre(String adminSifre) {
        this.adminSifre = adminSifre;
    }
}

