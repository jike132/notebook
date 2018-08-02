package com.example.lenovo.notebook.bean;

import java.sql.Time;

public class Note {
    private String time;
    private String Acc;
    private String content;
    private String tittle;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }

    public String getText() {
        return content;
    }

    public void setText(String content) {
        this.content = content;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
