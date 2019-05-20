package com.example.mobilfilmprojesi.Model;

public class User {

    private String nameSurname;
    private String password;
        private String secureCode;
        private String userName;

    public User() {
    }

    public User(String nameSurname, String password, String secureCode, String userName) {
        this.nameSurname = nameSurname;
        this.password = password;
        this.secureCode = secureCode;
        this.userName = userName;

    }


    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
