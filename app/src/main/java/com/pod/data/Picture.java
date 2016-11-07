package com.pod.data;

public class Picture {

    private String concepts;
    private String date;
    private String explanation;
    private String media_type;
    private String service_version;
    private String title;
    private String url;
    private String hdurl;

    public Picture(String concepts, String date, String media_type, String explanation, String service_version, String title, String url, String hdurl) {
        this.concepts = concepts;
        this.date = date;
        this.media_type = media_type;
        this.explanation = explanation;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
        this.hdurl = hdurl;
    }

    public String getConcepts() {
        return concepts;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getService_version() {
        return service_version;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getHdurl() {
        return hdurl;
    }
}
