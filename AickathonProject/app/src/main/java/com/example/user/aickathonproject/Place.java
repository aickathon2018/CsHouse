package com.example.user.aickathonproject;

public class Place {
    private String location;
    private String year;
    private String months;
    private String date;
    private String key_date;
    private String time;
    private String age_below_15,age_15To55,age_above55;
    private String num_female,num_male;
    private String Total_num;

    public String getKey_date() {
        return key_date;
    }

    public void setKey_date(String key_date) {
        this.key_date = key_date;
    }

    public String getTotal_num() {
        return Total_num;
    }

    public void setTotal_num(String total_num) {
        Total_num = total_num;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAge_below_15() {
        return age_below_15;
    }

    public void setAge_below_15(String age_below_15) {
        this.age_below_15 = age_below_15;
    }

    public String getAge_15To55() {
        return age_15To55;
    }

    public void setAge_15To55(String age_15To55) {
        this.age_15To55 = age_15To55;
    }

    public String getAge_above55() {
        return age_above55;
    }

    public void setAge_above55(String age_above55) {
        this.age_above55 = age_above55;
    }

    public String getNum_female() {
        return num_female;
    }

    public void setNum_female(String num_female) {
        this.num_female = num_female;
    }

    public String getNum_male() {
        return num_male;
    }

    public void setNum_male(String num_male) {
        this.num_male = num_male;
    }
}
