package com.sairaj.pagespeed_insights.model;

public class PageSpeedResult {
    private String url;
    private double performance;
    private double fcp;
    private double lcp;
    private double tbt;
    private double cls;

    public PageSpeedResult() {}

    public PageSpeedResult(String url, double performance, double fcp, double lcp, double tbt, double cls) {
        this.url = url;
        this.performance = performance;
        this.fcp = fcp;
        this.lcp = lcp;
        this.tbt = tbt;
        this.cls = cls;
    }

    public String getUrl() {
        return url;
    }

    public double getPerformance() {
        return performance;
    }

    public double getFcp() {
        return fcp;
    }

    public double getLcp() {
        return lcp;
    }

    public double getTbt() {
        return tbt;
    }

    public double getCls() {
        return cls;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public void setFcp(double fcp) {
        this.fcp = fcp;
    }

    public void setLcp(double lcp) {
        this.lcp = lcp;
    }

    public void setTbt(double tbt) {
        this.tbt = tbt;
    }

    public void setCls(double cls) {
        this.cls = cls;
    }
}
