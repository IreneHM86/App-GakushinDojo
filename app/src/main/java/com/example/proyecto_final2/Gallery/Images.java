package com.example.proyecto_final2.Gallery;

public class Images {
    //variables
    private String description, url;

    public Images() {

    }
    //getters & setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Images(String description, String url) {
        this.description = description;
        this.url = url;
    }
}
