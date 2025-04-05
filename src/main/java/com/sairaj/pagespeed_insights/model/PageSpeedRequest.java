package com.sairaj.pagespeed_insights.model;

import java.util.List;

public class PageSpeedRequest {
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}