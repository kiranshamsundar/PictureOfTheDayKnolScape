package com.pod.data;

public class PictureInfo {
    private String explanation;
    private String date;
    private String title;

    public PictureInfo(String explanation, String date, String title) {
        this.explanation = explanation;
        this.date = date;
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}
