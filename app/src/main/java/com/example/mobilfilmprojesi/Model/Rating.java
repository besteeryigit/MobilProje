package com.example.mobilfilmprojesi.Model;

public class Rating {

    private  String userName;
    private  String filmId;
    private  String rateValue;
    private  String comment;

    public Rating(String userName, String filmId, String rateValue, String comment) {
        this.userName = userName;
        this.filmId = filmId;
        this.rateValue = rateValue;
        this.comment = comment;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
